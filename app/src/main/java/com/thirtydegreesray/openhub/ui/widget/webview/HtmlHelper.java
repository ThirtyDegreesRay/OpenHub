/*
 *    Copyright 2017 ThirtyDegreesRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub.ui.widget.webview;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thirtydegreesray.openhub.mvp.model.NameParser;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ThirtyDegreesRay on 2017/8/20 12:58:43
 */

class HtmlHelper {

    private final static List<String> SUPPORTED_CODE_FILE_EXTENSIONS = Arrays.asList(
            "bsh", "c", "cc", "cpp", "cs", "csh", "cyc", "cv", "htm", "html", "java",
            "js", "m", "mxml", "perl", "pl", "pm", "py", "rb", "sh", "xhtml", "xml",
            "xsl"
    );

    private static Pattern LINK_TAG_MATCHER = Pattern.compile("href=\"(.*?)\"");
    private static Pattern IMAGE_TAG_MATCHER = Pattern.compile("src=\"(.*?)\"");

    static String generateImageHtml(@NonNull String imageUrl, @NonNull String backgroundColor){
        return "<html>" +
                "<head>" +
                    "<style>" +
                        "img{display: inline; height: auto; max-width: 100%;}" +
                        "body{background: " + backgroundColor + ";}" +
                    "</style>" +
                "</head>" +
                "<body><img src=\"" + imageUrl + "\"/></body>" +
                "</html>";
    }

    static String generateCodeHtml(@NonNull String codeSource, @Nullable String extension,
                                   boolean isDark, @NonNull String backgroundColor, boolean wrap){
        String skin = isDark ? "sons-of-obsidian" : "prettify";
        return generateCodeHtml(codeSource, extension, skin, backgroundColor, wrap);
    }

    private static String generateCodeHtml(@NonNull String codeSource, @Nullable String extension,
                                           @Nullable String skin, @NonNull String backgroundColor, boolean wrap ){
        return "<html>\n" +
                "<head>\n" +
                    "<meta charset=\"utf-8\" />\n" +
                    "<title>Code WebView</title>\n" +
                    "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0;\"/>" +
                    "<script src=\"./core/run_prettify.js?autoload=true&amp;" +
                    "skin=" + skin + "&amp;" +
                    "lang=" + getExtension(extension) + "&amp;\" defer></script>\n" +
                    "<style>" +
                        "body {background: " + backgroundColor + ";}" +
                        ".prettyprint {background: " + backgroundColor + ";}" +
                        "pre.prettyprint { white-space: " + (wrap ? "pre-wrap" : "no-wrap") + "; }" +
                    "</style>" +
                "</head>\n" +
                "<body>\n" +
                    "<?prettify lang=" + getExtension(extension) + " linenums=false?>\n" +
                    "<pre class=\"prettyprint\">\n" +
                        formatCode(codeSource) +
                    "</pre>\n" +
                "</body>\n" +
                "</html>";
    }

    static String generateMdHtml(@NonNull String mdSource, @Nullable String baseUrl,
                                 boolean isDark, @NonNull String backgroundColor,
                                 @NonNull String accentColor){
        String skin = isDark ? "github_dark.css" : "github.css";
        if(StringUtils.isBlank(baseUrl)){
            return generateMdHtml(mdSource, skin, backgroundColor, accentColor);
        }else{
            return generateMdHtml(validateImageBaseUrl(mdSource, baseUrl), skin,
                    backgroundColor, accentColor);
        }
    }

    private static String generateMdHtml(@NonNull String mdSource, String skin,
                                         @NonNull String backgroundColor, @NonNull String accentColor){
        return "<html>\n" +
                "<head>\n" +
                    "<meta charset=\"utf-8\" />\n" +
                    "<title>Code WebView</title>\n" +
                    "<meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\"/>" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"./" + skin + "\">\n" +
                    "<style>" +
                        "body{background: " + backgroundColor + ";}" +
                        "a {color:" + accentColor + " !important;}" +
                    "</style>" +
                "</head>\n" +
                "<body>\n" +
                    mdSource +
                "</body>\n" +
                "</html>";
    }

    private static String getExtension(@Nullable String extension){
        return SUPPORTED_CODE_FILE_EXTENSIONS.contains(extension) ? extension : "";
    }

    private static String formatCode(@NonNull String codeSource){
        if(StringUtils.isBlank(codeSource)) return  codeSource;
        return codeSource.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    @NonNull private static String validateImageBaseUrl(@NonNull String source, @NonNull String baseUrl) {
        NameParser nameParser = new NameParser(baseUrl);
        String owner = nameParser.getUsername();
        String repoName = nameParser.getName();
        Uri uri = Uri.parse(baseUrl);
        ArrayList<String> paths = new ArrayList<>(uri.getPathSegments());
        StringBuilder builder = new StringBuilder();
        builder.append(owner).append("/").append(repoName).append("/");
        boolean containsMaster = paths.size() > 3;
        if (!containsMaster) {
            builder.append("master/");
        } else {
            paths.remove("blob");
        }
        paths.remove(owner);
        paths.remove(repoName);
        for (String path : paths) {
            if (!path.equalsIgnoreCase(uri.getLastPathSegment())) {
                builder.append(path).append("/");
            }
        }
        Matcher matcher = IMAGE_TAG_MATCHER.matcher(source);
        while (matcher.find()) {
            String src = matcher.group(1).trim();
            if (src.startsWith("http://") || src.startsWith("https://")) {
                continue;
            }
            String finalSrc;
            if (src.startsWith("/" + owner + "/" + repoName)) {
                finalSrc = "https://raw.githubusercontent.com/" + src;
            } else {
                finalSrc = "https://raw.githubusercontent.com/" + builder.toString() + src;
            }
            source = source.replace("src=\"" + src + "\"", "src=\"" + finalSrc
                    .replace("raw/", "master/").replaceAll("//", "/") + "\"");
        }
        return validateLinks(source, baseUrl);
    }

    @NonNull private static String validateLinks(@NonNull String source, @NonNull String baseUrl) {
        NameParser nameParser = new NameParser(baseUrl);
        String owner = nameParser.getUsername();
        String repoName = nameParser.getName();
        Matcher matcher = LINK_TAG_MATCHER.matcher(source);
        Uri uri = Uri.parse(baseUrl);
        ArrayList<String> paths = new ArrayList<>(uri.getPathSegments());
        StringBuilder builder = new StringBuilder();
        builder.append("https://").append(uri.getAuthority()).append("/").append(owner).append("/").append(repoName).append("/");
        boolean containsMaster = paths.size() > 3 && paths.get(2).equalsIgnoreCase("blob");
        if (!containsMaster) {
            builder.append("blob/master/");
        }
        paths.remove(owner);
        paths.remove(repoName);
        for (String path : paths) {
            if (!path.equalsIgnoreCase(uri.getLastPathSegment())) {
                builder.append(path).append("/");
            }
        }
        while (matcher.find()) {
            String href = matcher.group(1).trim();
            if (href.startsWith("#") || href.startsWith("http://") || href.startsWith("https://") || href.startsWith("mailto:")) {
                continue;
            }
            String link = builder.toString() + "" + href;
            source = source.replace("href=\"" + href + "\"", "href=\"" + link + "\"");
        }
        return source;
    }


}
