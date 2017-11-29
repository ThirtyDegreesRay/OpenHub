

package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.dao.MyTrendingLanguage;
import com.thirtydegreesray.openhub.dao.MyTrendingLanguageDao;
import com.thirtydegreesray.openhub.mvp.contract.ITrendingContract;
import com.thirtydegreesray.openhub.mvp.model.TrendingLanguage;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;
import com.thirtydegreesray.openhub.util.JSONUtils;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class TrendingPresenter extends BasePresenter<ITrendingContract.View>
        implements ITrendingContract.Presenter{

    private ArrayList<TrendingLanguage> languages ;

    @Inject
    public TrendingPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public ArrayList<TrendingLanguage> getLanguagesFromLocal() {
        List<MyTrendingLanguage> myLanguages = daoSession.getMyTrendingLanguageDao().queryBuilder()
                .orderAsc(MyTrendingLanguageDao.Properties.Order)
                .list();
        if(StringUtils.isBlankList(myLanguages)){
            languages = JSONUtils.jsonToArrayList(getString(R.string.trending_languages), TrendingLanguage.class);
            languages = sortLanguages(languages);
        } else {
            languages = TrendingLanguage.generateFromDB(myLanguages);
        }
        return languages;
    }

    private ArrayList<TrendingLanguage> sortLanguages(ArrayList<TrendingLanguage> languages){
        for(int i = 0; i < languages.size(); i++){
            languages.get(i).setOrder(i + 1);
        }
        return languages;
    }

    public ArrayList<TrendingLanguage> getLanguages() {
        return languages;
    }
}
