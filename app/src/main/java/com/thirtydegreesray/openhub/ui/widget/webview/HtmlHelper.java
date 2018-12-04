

package com.thirtydegreesray.openhub.ui.widget.webview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thirtydegreesray.openhub.mvp.model.GitHubName;
import com.thirtydegreesray.openhub.util.GitHubHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 2017/8/20 12:58:43
 * Copied from Copyright (C) 2017 Kosh.
 * Modified by Copyright (C) 2017 ThirtyDegreesRay.
 */

class HtmlHelper {

    private final static List<String> SUPPORTED_CODE_FILE_EXTENSIONS = Arrays.asList(
            "bsh", "c", "cc", "cpp", "cs", "csh", "cyc", "cv", "htm", "html", "java",
            "js", "m", "mxml", "perl", "pl", "pm", "py", "rb", "sh", "xhtml", "xml",
            "xsl"
    );

    private static Pattern LINK_PATTERN = Pattern.compile("href=\"(.*?)\"");
    private static Pattern IMAGE_PATTERN = Pattern.compile("src=\"(.*?)\"");

    static String generateImageHtml(@NonNull String imageUrl, @NonNull String backgroundColor) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "img{height: auto; width: 100%;}" +
                "body{background: " + backgroundColor + ";}" +
                "</style>" +
                "</head>" +
                "<body><img src=\"" + imageUrl + "\"/></body>" +
                "</html>";
    }

    static String generateHtmlSourceHtml(@NonNull String htmlSource, @NonNull String backgroundColor,
                                         @NonNull String accentColor) {
        return "<html>" +
                    "<head>" +
                        "<meta charset=\"utf-8\" />\n" +
                        "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0;\"/>" +
                        "<style>" +
                            "body{background: " + backgroundColor + ";}" +
                            "a {color:" + accentColor + " !important;}" +
                        "</style>" +
                    "</head>" +
                    "<body>" +
                        htmlSource +
                    "</body>" +
                "</html>";
    }

    static String generateCodeHtml(@NonNull String codeSource, @Nullable String extension,
                                   boolean isDark, @NonNull String backgroundColor,
                                   boolean wrap, boolean lineNums) {
        String skin = isDark ? "sons-of-obsidian" : "prettify";
        return generateCodeHtml(codeSource, extension, skin, backgroundColor, wrap, lineNums);
    }

    private static String generateCodeHtml(@NonNull String codeSource, @Nullable String extension,
                                           @Nullable String skin, @NonNull String backgroundColor,
                                           boolean wrap, boolean lineNums) {
        return "<html>\n" +
                    "<head>\n" +
                        "<meta charset=\"utf-8\" />\n" +
                        "<title>Code View</title>\n" +
                        "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0;\"/>" +
                        "<script src=\"./core/run_prettify.js?autoload=true&amp;" +
                            "skin=" + skin + "&amp;" +
                            "lang=" + getExtension(extension) + "&amp;\" defer></script>\n" +
                        "<style>" +
                            "body {background: " + backgroundColor + ";}" +
                            ".prettyprint {background: " + backgroundColor + ";}" +
                            "pre.prettyprint {" +
                                " word-wrap: " + (wrap ? "break-word" : "normal") + "; " +
                                " white-space: " + (wrap ? "pre-wrap" : "no-wrap") + "; " +
                            "}" +
                        "</style>" +
                    "</head>\n" +
                    "<body>\n" +
                        "<?prettify lang=" + getExtension(extension) + " linenums=" + lineNums + "?>\n" +
                        "<pre class=\"prettyprint\">\n" +
                            formatCode(codeSource) +
                        "</pre>\n" +
                    "</body>\n" +
                "</html>";
    }

    static String generateMdHtml(@NonNull String mdSource, @Nullable String baseUrl,
                                 boolean isDark, @NonNull String backgroundColor,
                                 @NonNull String accentColor, boolean wrapCode) {
        String skin = isDark ? "markdown_dark.css" : "markdown_white.css";
        mdSource = StringUtils.isBlank(baseUrl) ? mdSource : fixLinks(mdSource, baseUrl);
        //fix wiki inner url like this "href="/robbyrussell/oh-my-zsh/wiki/Themes"" 
        mdSource = fixWikiLinks(mdSource);
        return generateMdHtml(mdSource, skin, backgroundColor, accentColor, wrapCode);
    }

    private static String generateMdHtml(@NonNull String mdSource, String skin,
                                         @NonNull String backgroundColor,
                                         @NonNull String accentColor, boolean wrapCode) {
        return "<html>\n" +
                    "<head>\n" +
                        "<meta charset=\"utf-8\" />\n" +
                        "<title>MD View</title>\n" +
                        "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\"/>" +
                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"./" + skin + "\">\n" +
                        "<style>" +
                            "body{background: " + backgroundColor + ";}" +
                            "a {color:" + accentColor + " !important;}" +
                            ".highlight pre, pre {" +
                            " word-wrap: " + (wrapCode ? "break-word" : "normal") + "; " +
                            " white-space: " + (wrapCode ? "pre-wrap" : "pre") + "; " +
                            "}" +
                        "</style>" +
                    "</head>\n" +
                    "<body>\n" +
                        mdSource +
                    "</body>\n" +
                "</html>";
    }

    private static String getExtension(@Nullable String extension) {
        return SUPPORTED_CODE_FILE_EXTENSIONS.contains(extension) ? extension : "";
    }

    private static String formatCode(@NonNull String codeSource) {
        if (StringUtils.isBlank(codeSource)) return codeSource;
        return codeSource.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    private static String fixWikiLinks(@NonNull String source) {
        Matcher linksMatcher = LINK_PATTERN.matcher(source);
        while (linksMatcher.find()) {
            while (linksMatcher.find()) {
                String oriUrl = linksMatcher.group(1);
                String fixedUrl;
                if (oriUrl.startsWith("/") && oriUrl.contains("/wiki/")) {
                    fixedUrl = "https://github.com" + oriUrl;
                    source = source.replace("href=\"" + oriUrl + "\"", "href=\"" + fixedUrl + "\"");
                }
            }
        }
        return source;
    }

    private static String fixLinks(@NonNull String source, @NonNull String baseUrl) {
        GitHubName gitHubName = GitHubName.fromUrl(baseUrl);
        if (gitHubName == null) return source;
        String owner = gitHubName.getUserName();
        String repo = gitHubName.getRepoName();
        String branch = baseUrl.substring(baseUrl.indexOf("blob") + 5, baseUrl.lastIndexOf("/"));

        Matcher linksMatcher = LINK_PATTERN.matcher(source);
        while (linksMatcher.find()) {
            String oriUrl = linksMatcher.group(1);
            if (oriUrl.contains("http://") || oriUrl.contains("https://")
                    || oriUrl.startsWith("#") //filter markdown inner link
                    ) {
                continue;
            }

            String subUrl = oriUrl.startsWith("/") ? oriUrl : "/".concat(oriUrl);
            String fixedUrl;
            if (!GitHubHelper.isImage(oriUrl)) {
                fixedUrl = "https://github.com/" + owner + "/" + repo + "/blob/" + branch + subUrl;
            } else {
                //if link url is a image
                fixedUrl = "https://raw.githubusercontent.com/" + owner + "/" + repo + "/" + branch + subUrl;
            }
            source = source.replace("href=\"" + oriUrl + "\"", "href=\"" + fixedUrl + "\"");
        }

        Matcher imagesMatcher = IMAGE_PATTERN.matcher(source);
        while (imagesMatcher.find()) {
            String oriUrl = imagesMatcher.group(1);
            if (oriUrl.contains("http://") || oriUrl.contains("https://")) {
                continue;
            }

            String subUrl = oriUrl.startsWith("/") ? oriUrl : "/".concat(oriUrl);
            String fixedUrl = "https://raw.githubusercontent.com/" + owner + "/" + repo + "/" + branch + subUrl;
            source = source.replace("src=\"" + oriUrl + "\"", "src=\"" + fixedUrl + "\"");
        }

        return source;
    }

    static String generateDiffHtml(@NonNull String diffSource, boolean isDark,
                                   @NonNull String backgroundColor, boolean wrap) {
        String skin = isDark ? "diff_dark.css" : "diff_light.css";
        return generateDiffHtml(diffSource, backgroundColor, skin, wrap);
    }

    private static String generateDiffHtml(@NonNull String diffSource, @NonNull String backgroundColor,
                                           String skin, boolean wrap) {
        return "<html>\n" +
                    "<head>\n" +
                        "<meta charset=\"utf-8\" />\n" +
                        "<title>Diff View</title>\n" +
                        "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0;\"/>" +
                        "<link rel=\"stylesheet\" type=\"text/css\" href=\"./" + skin + "\">\n" +
                        "<style>" +
                            "body {background: " + backgroundColor + ";}\n" +
                            ".pre {\n" +
                                    "background: " + backgroundColor + ";\n" +
                                    " word-wrap: " + (wrap ? "break-word" : "normal") + ";\n" +
                                    " white-space: " + (wrap ? "pre-wrap" : "pre") + ";\n" +
                            "}\n" +
                        "</style>\n" +
                    "</head>\n" +

                    "<body>\n" +
                        "<pre class=\"pre\">\n" +
                            parseDiffSource(formatCode(diffSource), wrap) +
                        "</pre>\n" +
                    "</body>\n" +
                "</html>";
    }

    private static String parseDiffSource(@NonNull String diffSource, boolean wrap) {
        StringBuilder source = new StringBuilder();
        String[] lines = diffSource.split("\\n");

        int addStartLine = -1;
        int removeStartLine = -1;
        int addLineNum = 0;
        int removeLineNum = 0;
        int normalLineNum = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            String lineNumberStr = "";
            String classStr = "";
            int curAddNumber = -1;
            int curRemoveNumber = -1;

            if (line.startsWith("+")) {
                classStr = "class=\"add\";";
                curAddNumber = addStartLine + normalLineNum + addLineNum;
                addLineNum++;
            } else if (line.startsWith("-")) {
                classStr = "class=\"remove\";";
                curRemoveNumber = removeStartLine + normalLineNum + removeLineNum;
                removeLineNum++;
            } else if (line.startsWith("@@")) {
                classStr = "class=\"change\";";
                removeStartLine = getRemoveStartLine(line);
                addStartLine = getAddStartLine(line);
                addLineNum = 0;
                removeLineNum = 0;
                normalLineNum = 0;
            } else if (!line.startsWith("\\")) {
                curAddNumber = addStartLine + normalLineNum + addLineNum;
                curRemoveNumber = removeStartLine + normalLineNum + removeLineNum;
                normalLineNum++;
            }
            lineNumberStr = getDiffLineNumber(curRemoveNumber == -1 ? "" : String.valueOf(curRemoveNumber),
                    curAddNumber == -1 ? "" : String.valueOf(curAddNumber));

            source.append("\n")
                    .append("<div ").append(classStr).append(">")
                    .append(wrap ? "" : lineNumberStr + getBlank(1))
                    .append(line)
                    .append("</div>");
        }
        return source.toString();
    }

    private static int getRemoveStartLine(String line){
        try {
            return Integer.parseInt(line.substring(line.indexOf("-") + 1, line.indexOf(",")));
        } catch (Exception e){
            return 1;
        }
    }

    private static int getAddStartLine(String line){
        try {
            return Integer.parseInt(line.substring(line.indexOf("+") + 1,
                    line.indexOf(",", line.indexOf("+"))));
        } catch (Exception e){
            return 1;
        }
    }

    private static String getDiffLineNumber(String removeNumber, String addNumber){
        int minLength = 4;
        return new StringBuilder().append(getBlank(minLength - removeNumber.length()))
                .append(removeNumber)
                .append(getBlank(1))
                .append(getBlank(minLength - addNumber.length()))
                .append(addNumber)
                .toString();
    }

    private static String getBlank(int num){
        StringBuilder builder = new StringBuilder("");
        for(int i = 0; i < num; i++){
            builder.append(" ");
        }
        return builder.toString();
    }

}
