package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.dao.MyTrendingLanguage;
import com.thirtydegreesray.openhub.dao.MyTrendingLanguageDao;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.ILanguagesEditorContract;
import com.thirtydegreesray.openhub.mvp.model.TrendingLanguage;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;
import com.thirtydegreesray.openhub.ui.activity.LanguagesEditorActivity;
import com.thirtydegreesray.openhub.util.JSONUtils;
import com.thirtydegreesray.openhub.util.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ThirtyDegreesRay on 2017/11/28 17:23:17
 */

public class LanguagesEditorPresenter extends BasePresenter<ILanguagesEditorContract.View>
        implements ILanguagesEditorContract.Presenter {

    @AutoAccess LanguagesEditorActivity.LanguageEditorMode mode;
    @AutoAccess ArrayList<TrendingLanguage> selectedLanguages;
    private ArrayList<TrendingLanguage> languages;
    private TrendingLanguage removedLanguage;
    private int removedPosition;

    @Inject
    public LanguagesEditorPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        loadLanguages();
    }

    @Override
    public void loadLanguages() {
        if (LanguagesEditorActivity.LanguageEditorMode.Sort.equals(mode)) {
            loadSelectedLanguages();
        } else {
            loadAllLanguages();
        }
    }

    @Override
    public void searchLanguages(String key) {
        if(languages == null){
            return;
        }
        if(StringUtils.isBlank(key)){
            mView.showLanguages(languages);
        } else {
            key = key.toLowerCase();
            ArrayList<TrendingLanguage> searchedLanguages = new ArrayList<>();
            for(TrendingLanguage language : languages){
                if(language.getName().toLowerCase().contains(key)){
                    searchedLanguages.add(language);
                }
            }
            mView.showLanguages(searchedLanguages);
        }
    }

    @Override
    public void saveSelectedLanguages() {
        ArrayList<MyTrendingLanguage> myLanguages = new ArrayList<>();
        ArrayList<TrendingLanguage> saveLanguages = new ArrayList<>();
        if (mode.equals(LanguagesEditorActivity.LanguageEditorMode.Sort)) {
            saveLanguages = languages;
        } else {
            ArrayList<TrendingLanguage> dbSelectedLanguages = selectedLanguages;
            for (TrendingLanguage trendingLanguage : languages) {
                if (!trendingLanguage.isSelected() && dbSelectedLanguages.contains(trendingLanguage)) {
                    dbSelectedLanguages.remove(trendingLanguage);
                } else if (trendingLanguage.isSelected() && !dbSelectedLanguages.contains(trendingLanguage)) {
                    saveLanguages.add(trendingLanguage);
                }
            }
            saveLanguages.addAll(0, dbSelectedLanguages);
        }

        int order = 0;
        for (TrendingLanguage trendingLanguage : saveLanguages) {
            myLanguages.add(TrendingLanguage.generateDB(trendingLanguage, ++order));
        }

        daoSession.getMyTrendingLanguageDao().deleteAll();
        daoSession.getMyTrendingLanguageDao().insertInTx(myLanguages);
    }

    @Override
    public TrendingLanguage removeLanguage(int position) {
        removedLanguage = languages.remove(position);
        removedPosition = position;
        return removedLanguage;
    }

    @Override
    public void undoRemoveLanguage() {
        languages.add(removedPosition, removedLanguage);
        mView.notifyItemInserted(removedPosition);
    }

    @Override
    public int getListSelectedLanguageCount() {
        int count = 0;
        if (LanguagesEditorActivity.LanguageEditorMode.Sort.equals(mode)) {
            count = languages.size();
        } else {
            for (TrendingLanguage language : languages) {
                if (language.isSelected()) count++;
            }
        }
        return count;
    }

    private void loadSelectedLanguages() {
        languages = getSelectedLanguages();
        mView.showLanguages(languages);
        mView.hideLoading();
    }

    private void loadAllLanguages() {
        mView.showLoading();
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                mView.showLoadError(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<ResponseBody> response) {
                try {
                    parsePageData(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        generalRxHttpExecute(forceNetWork -> getGitHubWebPageService().getTrendingLanguages(),
                httpObserver, false);
    }

    private ArrayList<TrendingLanguage> getSelectedLanguages() {
        ArrayList<TrendingLanguage> selectedLanguages;
        List<MyTrendingLanguage> myLanguages = daoSession.getMyTrendingLanguageDao().queryBuilder()
                .orderAsc(MyTrendingLanguageDao.Properties.Order)
                .list();
        if (StringUtils.isBlankList(myLanguages)) {
            selectedLanguages = JSONUtils.jsonToArrayList(getString(R.string.trending_languages), TrendingLanguage.class);
            selectedLanguages.addAll(0, getFixedLanguages());
        } else {
            selectedLanguages = TrendingLanguage.generateFromDB(myLanguages);
            fixFixedLanguagesName(selectedLanguages);
        }
        return selectedLanguages;
    }

    public LanguagesEditorActivity.LanguageEditorMode getMode() {
        return mode;
    }

    private void parsePageData(String page) {
        Observable.just(page)
                .map(s -> {
                    ArrayList<TrendingLanguage> languages = new ArrayList<>();
                    languages.addAll(getFixedLanguages());
                    try {
                        Document doc = Jsoup.parse(s, AppConfig.GITHUB_BASE_URL);
                        Element languageElement = doc.getElementById("select-menu-language");
                        Elements elements = languageElement.select(".select-menu-modal .select-menu-list a.select-menu-item");
                        for (Element element : elements) {
                            String slug = element.attr("href");
                            slug = slug.substring(slug.lastIndexOf("/") + 1);
                            if(slug.contains("?")){
                                slug = slug.substring(0, slug.indexOf("?"));
                            }
                            Element nameElement = element.select("span").first();
                            String name = nameElement.textNodes().get(0).toString().trim();
                            languages.add(new TrendingLanguage(name, slug));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return languages;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    if (results.size() > 2) {
                        mView.hideLoading();
                        languages = results;
                        for (TrendingLanguage trendingLanguage : languages) {
                            trendingLanguage.setSelected(selectedLanguages.contains(trendingLanguage));
                        }
                        mView.showLanguages(languages);
                    } else {
                        String errorTip = String.format(getString(R.string.github_page_parse_error),
                                getString(R.string.trending));
                        mView.showLoadError(errorTip);
                        mView.hideLoading();
                    }
                });
    }

    private ArrayList<TrendingLanguage> getFixedLanguages(){
        ArrayList<TrendingLanguage> fixedLanguages = new ArrayList<>();
        fixedLanguages.add(new TrendingLanguage(getString(R.string.all_languages), "all"));
        fixedLanguages.add(new TrendingLanguage(getString(R.string.unknown_languages), "unknown"));
        return fixedLanguages;
    }

    private void fixFixedLanguagesName(ArrayList<TrendingLanguage> languages){
        for(TrendingLanguage language : languages){
            if(language.getSlug().equals("all")){
                language.setName(getString(R.string.all_languages));
            } else  if(language.getSlug().equals("unknown")){
                language.setName(getString(R.string.unknown_languages));
            }
        }
    }

}
