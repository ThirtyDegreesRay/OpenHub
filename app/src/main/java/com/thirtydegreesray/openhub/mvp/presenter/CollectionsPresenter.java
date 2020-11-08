package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.ICollectionsContract;
import com.thirtydegreesray.openhub.mvp.model.Collection;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
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
 * Created by ThirtyDegreesRay on 2017/12/25 15:16:34
 */

public class CollectionsPresenter extends BasePresenter<ICollectionsContract.View>
        implements ICollectionsContract.Presenter {

    private ArrayList<Collection> collections;

    @Inject
    public CollectionsPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        loadCollections(false);
    }

    @Override
    public void loadCollections(boolean isReload) {
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

        generalRxHttpExecute(forceNetWork -> getGitHubWebPageService().getCollections(forceNetWork),
                httpObserver, !isReload);

    }

    private void parsePageData(String page) {
        Observable.just(page)
                .map(s -> {
                    ArrayList<Collection> collections = new ArrayList<>();
                    try {
                        Document doc = Jsoup.parse(s, AppConfig.GITHUB_BASE_URL);
                        collections.addAll(getTopCollections(doc));
                        collections.addAll(getBellowCollections(doc));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return collections;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    if(mView == null) return;
                    if(results.size() != 0){
                        collections = results;
                        mView.hideLoading();
                        mView.showCollections(collections);
                    } else {
                        String errorTip = String.format(getString(R.string.github_page_parse_error),
                                getString(R.string.repo_collections));
                        mView.showLoadError(errorTip);
                        mView.hideLoading();
                    }
                });
    }

    private ArrayList<Collection> getTopCollections(Document doc){
        ArrayList<Collection> collections = new ArrayList<>();
        Elements elements = doc.getElementsByClass(
                "col-12 col-sm-6 col-md-4 mb-4");
        for (Element element : elements) {
            Element hrefElement = element.select("a").first();
            Element titleElement = element.select("a > p").first();
            Element descElement = element.select("a > p").last();
            String id = hrefElement.attr("href");
            id = id.substring(id.lastIndexOf("/") + 1);
            String title = titleElement.textNodes().get(0).toString();
            String desc = descElement.textNodes().get(0).toString();

//            List<TextNode> descTextNodes = descElement.textNodes();
//            int descIndex = descTextNodes.size() == 0 ? 0 : descTextNodes.size() - 1;
//            String desc = descTextNodes.get(descIndex).toString().trim();
            Collection collection = new Collection(id, title, desc);
            collections.add(collection);
        }
        return collections;
    }

    private ArrayList<Collection> getBellowCollections(Document doc){
        ArrayList<Collection> collections = new ArrayList<>();
        Elements elements = doc.getElementsByClass(
                "d-flex border-bottom border-gray-light pb-4 mb-5");
        for (Element element : elements) {
            Element titleElement = element.select("div > h2 > a").first();
            Element descElement = element.select("div").last();
            String id = titleElement.attr("href");
            id = id.substring(id.lastIndexOf("/") + 1);
            String title = titleElement.textNodes().get(0).toString();

            List<TextNode> descTextNodes = descElement.textNodes();
            int descIndex = descTextNodes.size() == 0 ? 0 : descTextNodes.size() - 1;
            String desc = descTextNodes.get(descIndex).toString().trim();
            Collection collection = new Collection(id, title, desc);
            collections.add(collection);
        }
        return collections;
    }

}
