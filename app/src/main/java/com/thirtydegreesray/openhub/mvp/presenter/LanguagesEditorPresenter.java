package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2017/11/28 17:23:17
 */

public class LanguagesEditorPresenter extends BasePresenter<ILanguagesEditorContract.View>
        implements ILanguagesEditorContract.Presenter {

    @AutoAccess LanguagesEditorActivity.LanguageEditorMode mode;
    @AutoAccess ArrayList<TrendingLanguage> selectedLanguages;
    private ArrayList<TrendingLanguage> languages;
    private TrendingLanguage removedLanguage ;
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
        if(LanguagesEditorActivity.LanguageEditorMode.Sort.equals(mode)){
            loadSelectedLanguages();
        } else {
            loadAllLanguages();
        }
    }

    @Override
    public void saveSelectedLanguages() {
        ArrayList<MyTrendingLanguage> myLanguages = new ArrayList<>();
        ArrayList<TrendingLanguage> saveLanguages = new ArrayList<>();
        if(mode.equals(LanguagesEditorActivity.LanguageEditorMode.Sort)){
            saveLanguages = languages;
        } else {
            ArrayList<TrendingLanguage> dbSelectedLanguages = selectedLanguages;
            for(TrendingLanguage trendingLanguage : languages){
                if(!trendingLanguage.isSelected() && dbSelectedLanguages.contains(trendingLanguage)){
                    dbSelectedLanguages.remove(trendingLanguage);
                } else if(trendingLanguage.isSelected() && !dbSelectedLanguages.contains(trendingLanguage)){
                    saveLanguages.add(trendingLanguage);
                }
            }
            saveLanguages.addAll(0 ,dbSelectedLanguages);
        }

        int order = 0;
        for(TrendingLanguage trendingLanguage : saveLanguages){
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
        if(LanguagesEditorActivity.LanguageEditorMode.Sort.equals(mode)){
            count = languages.size();
        } else {
            for(TrendingLanguage language : languages){
                if(language.isSelected()) count++;
            }
        }
        return count;
    }

    private void loadSelectedLanguages(){
        languages = getSelectedLanguages();
        mView.showLanguages(languages);
    }

    private void loadAllLanguages(){
        mView.showLoading();
        HttpObserver<ArrayList<TrendingLanguage>> httpObserver =
                new HttpObserver<ArrayList<TrendingLanguage>>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                mView.showLoadError(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<ArrayList<TrendingLanguage>> response) {
                mView.hideLoading();
                languages = response.body();
                for(TrendingLanguage trendingLanguage : languages){
                    trendingLanguage.setSelected(selectedLanguages.contains(trendingLanguage));
                }
                mView.showLanguages(languages);
            }
        };
        generalRxHttpExecute(forceNetWork -> getOpenHubService().getTrendingLanguages(),
                httpObserver, false);
    }

    private ArrayList<TrendingLanguage> getSelectedLanguages(){
        ArrayList<TrendingLanguage> selectedLanguages;
        List<MyTrendingLanguage> myLanguages = daoSession.getMyTrendingLanguageDao().queryBuilder()
                .orderAsc(MyTrendingLanguageDao.Properties.Order)
                .list();
        if(StringUtils.isBlankList(myLanguages)){
            selectedLanguages = JSONUtils.jsonToArrayList(getString(R.string.trending_languages), TrendingLanguage.class);
        } else {
            selectedLanguages = TrendingLanguage.generateFromDB(myLanguages);
        }
        return selectedLanguages;
    }

    public LanguagesEditorActivity.LanguageEditorMode getMode() {
        return mode;
    }

}
