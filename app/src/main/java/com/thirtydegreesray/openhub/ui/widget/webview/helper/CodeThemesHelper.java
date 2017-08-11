/*
 *    Copyright 2017 ThirtyDegressRay
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

package com.thirtydegreesray.openhub.ui.widget.webview.helper;

import android.support.annotation.NonNull;

/**
 * Created by Kosh on 21 Jun 2017, 1:44 PM
 */

public class CodeThemesHelper {

//    @NonNull public static List<String> listThemes() {
//        try {
//            List<String> list = Stream.of(App.getInstance().getAssets().list("highlight/styles/themes"))
//                    .map(s -> "themes/" + s)
//                    .toList();
//            list.add(0, "prettify.css");
//            list.add(1, "prettify_dark.css");
//            return list;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return Collections.emptyList();
//    }

    @NonNull public static String getTheme(boolean isDark) {
        return isDark ? "prettify_dark.css" : "prettify.css";
    }

//    private static boolean exists(@NonNull String theme) {
//        return listThemes().contains(theme);
//    }


    public static final String CODE_EXAMPLE =
            "class ThemeCodeActivity : BaseActivity<ThemeCodeMvp.View, ThemeCodePresenter>(), ThemeCodeMvp.View {\n" +
                    "\n" +
                    "    val spinner: AppCompatSpinner by bindView(R.id.themesList)\n" +
                    "    val webView: PrettifyWebView by bindView(R.id.webView)\n" +
                    "    val progress: ProgressBar? by bindView(R.id.readmeLoader)\n" +
                    "\n" +
                    "    override fun layout(): Int = R.layout.theme_code_layout\n" +
                    "\n" +
                    "    override fun isTransparent(): Boolean = false\n" +
                    "\n" +
                    "    override fun canBack(): Boolean = true\n" +
                    "\n" +
                    "    override fun isSecured(): Boolean = false\n" +
                    "\n" +
                    "    override fun providePresenter(): ThemeCodePresenter = ThemeCodePresenter()\n" +
                    "\n" +
                    "    @OnClick(R.id.done) fun onSaveTheme() {\n" +
                    "        val theme = spinner.selectedItem as String\n" +
                    "        PrefGetter.setCodeTheme(theme)\n" +
                    "        setResult(Activity.RESULT_OK)\n" +
                    "        finish()\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun onInitAdapter(list: List<String>) {\n" +
                    "        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list)\n" +
                    "    }\n" +
                    "\n" +
                    "    @OnItemSelected(R.id.themesList) fun onItemSelect() {\n" +
                    "        val theme = spinner.selectedItem as String\n" +
                    "        webView.setThemeSource(CodeThemesHelper.CODE_EXAMPLE, theme)\n" +
                    "    }\n" +
                    "\n" +
                    "\n" +
                    "    override fun onCreate(savedInstanceState: Bundle?) {\n" +
                    "        super.onCreate(savedInstanceState)\n" +
                    "        progress?.visibility = View.VISIBLE\n" +
                    "        webView.setOnContentChangedListener(this)\n" +
                    "        title = \"\"\n" +
                    "        presenter.onLoadThemes()\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun onContentChanged(p: Int) {\n" +
                    "        progress?.let {\n" +
                    "            it.progress = p\n" +
                    "            if (p == 100) it.visibility = View.GONE\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    override fun onScrollChanged(reachedTop: Boolean, scroll: Int) {}\n" +
                    "}";
}
