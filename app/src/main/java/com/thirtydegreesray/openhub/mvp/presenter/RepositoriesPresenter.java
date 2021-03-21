

package com.thirtydegreesray.openhub.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.common.Event;
import com.thirtydegreesray.openhub.dao.BookMarkRepo;
import com.thirtydegreesray.openhub.dao.BookMarkRepoDao;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.dao.TraceRepo;
import com.thirtydegreesray.openhub.dao.TraceRepoDao;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.error.HttpPageNoFoundError;
import com.thirtydegreesray.openhub.mvp.contract.IRepositoriesContract;
import com.thirtydegreesray.openhub.mvp.model.Collection;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.model.SearchModel;
import com.thirtydegreesray.openhub.mvp.model.SearchResult;
import com.thirtydegreesray.openhub.mvp.model.Topic;
import com.thirtydegreesray.openhub.mvp.model.TrendingLanguage;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.model.filter.RepositoriesFilter;
import com.thirtydegreesray.openhub.mvp.model.filter.TrendingSince;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePagerPresenter;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;
import com.thirtydegreesray.openhub.util.StringUtils;

import org.greenrobot.eventbus.Subscribe;
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
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class RepositoriesPresenter extends BasePagerPresenter<IRepositoriesContract.View>
        implements IRepositoriesContract.Presenter {

    private ArrayList<Repository> repos;

    @AutoAccess RepositoriesFragment.RepositoriesType type;
    @AutoAccess String user;
    @AutoAccess String repo;

    @AutoAccess SearchModel searchModel;
    @AutoAccess TrendingSince since;

    @AutoAccess RepositoriesFilter filter;

    @AutoAccess TrendingLanguage language;

    @AutoAccess Collection collection;
    @AutoAccess Topic topic;

    @Inject
    public RepositoriesPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (type.equals(RepositoriesFragment.RepositoriesType.SEARCH)) {
            setEventSubscriber(true);
        }
    }

    @Override
    protected void loadData() {
        if (RepositoriesFragment.RepositoriesType.SEARCH.equals(type)) {
            if (searchModel != null) searchRepos(1);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.TRACE.equals(type)) {
            loadTrace(1);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.BOOKMARK.equals(type)) {
            loadBookmarks(1);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.COLLECTION.equals(type)) {
            loadCollection(false);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.TOPIC.equals(type)) {
            initSearchModelForTopic();
            searchRepos(1);
            return;
        }
        if(RepositoriesFragment.RepositoriesType.TRENDING.equals(type)){
            loadTrending(false);
            return;
        }
        loadRepositories(false, 1);
    }

    @Override
    public void loadRepositories(final boolean isReLoad, final int page) {
        filter = getFilter();
        if (type.equals(RepositoriesFragment.RepositoriesType.SEARCH)) {
            searchRepos(page);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.TRACE.equals(type)) {
            loadTrace(page);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.BOOKMARK.equals(type)) {
            loadBookmarks(page);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.COLLECTION.equals(type)) {
            loadCollection(isReLoad);
            return;
        }
        if (RepositoriesFragment.RepositoriesType.TOPIC.equals(type)) {
            initSearchModelForTopic();
            searchRepos(page);
            return;
        }
        if(RepositoriesFragment.RepositoriesType.TRENDING.equals(type)){
            loadTrending(isReLoad);
            return;
        }
        mView.showLoading();
        final boolean readCacheFirst = !isReLoad && page == 1;

        HttpObserver<ArrayList<Repository>> httpObserver = new HttpObserver<ArrayList<Repository>>() {
            @Override
            public void onError(@NonNull Throwable error) {
                mView.hideLoading();
                handleError(error);
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<ArrayList<Repository>> response) {
                mView.hideLoading();
                if (isReLoad || readCacheFirst || repos == null || page == 1) {
                    repos = response.body();
                } else {
                    repos.addAll(response.body());
                }
                if (response.body().size() == 0 && repos.size() != 0) {
                    mView.setCanLoadMore(false);
                } else {
                    mView.showRepositories(repos);
                }
            }
        };

        generalRxHttpExecute(new IObservableCreator<ArrayList<Repository>>() {
            @Nullable
            @Override
            public Observable<Response<ArrayList<Repository>>> createObservable(boolean forceNetWork) {
                return getObservable(forceNetWork, page);
            }
        }, httpObserver, readCacheFirst);

    }

    @Override
    public void loadRepositories(RepositoriesFilter filter) {
        this.filter = filter;
        loadRepositories(false, 1);
    }

    private Observable<Response<ArrayList<Repository>>> getObservable(boolean forceNetWork, int page) {
        switch (type) {
            case OWNED:
                return getRepoService().getUserRepos(forceNetWork, page, filter.getType(),
                        filter.getSort(), filter.getSortDirection());
            case PUBLIC:
                return getRepoService().getUserPublicRepos(forceNetWork, user, page,
                        filter.getType(), filter.getSort(), filter.getSortDirection());
            case STARRED:
                return getRepoService().getStarredRepos(forceNetWork, user, page,
                        filter.getSort(), filter.getSortDirection());
            case FORKS:
                return getRepoService().getForks(forceNetWork, user, repo, page);
            default:
                return null;
        }
    }

    private void searchRepos(final int page) {
        mView.showLoading();

        HttpObserver<SearchResult<Repository>> httpObserver =
                new HttpObserver<SearchResult<Repository>>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        mView.hideLoading();
                        handleError(error);
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<SearchResult<Repository>> response) {
                        mView.hideLoading();
                        if (repos == null || page == 1) {
                            repos = response.body().getItems();
                        } else {
                            repos.addAll(response.body().getItems());
                        }
                        if (response.body().getItems().size() == 0 && repos.size() != 0) {
                            mView.setCanLoadMore(false);
                        } else {
                            mView.showRepositories(repos);
                        }
                    }
                };
        generalRxHttpExecute(new IObservableCreator<SearchResult<Repository>>() {
            @Nullable
            @Override
            public Observable<Response<SearchResult<Repository>>> createObservable(boolean forceNetWork) {
                return getSearchService().searchRepos(searchModel.getQuery(), searchModel.getSort(),
                        searchModel.getOrder(), page);
            }
        }, httpObserver);
    }

    @Subscribe
    public void onSearchEvent(@NonNull Event.SearchEvent searchEvent) {
        if (!searchEvent.searchModel.getType().equals(SearchModel.SearchType.Repository)) return;
        setLoaded(false);
        this.searchModel = searchEvent.searchModel;
        prepareLoadData();
    }

    private void handleError(Throwable error) {
        if (!StringUtils.isBlankList(repos)) {
            mView.showErrorToast(getErrorTip(error));
        } else if (error instanceof HttpPageNoFoundError) {
            mView.showRepositories(new ArrayList<Repository>());
        } else {
            mView.showLoadError(getErrorTip(error));
        }
    }

    public String getUser() {
        return user;
    }

    public RepositoriesFragment.RepositoriesType getType() {
        return type;
    }

    public RepositoriesFilter getFilter() {
        if (filter == null) {
            filter = RepositoriesFragment.RepositoriesType.STARRED.equals(type) ?
                    RepositoriesFilter.DEFAULT_STARRED_REPO : RepositoriesFilter.DEFAULT;
        }
        return filter;
    }

    private void loadTrace(int page) {
        long start = System.currentTimeMillis();

        List<TraceRepo> traceRepos = daoSession.getTraceRepoDao().queryBuilder()
                .orderDesc(TraceRepoDao.Properties.LatestTime)
                .offset((page - 1) * 30)
                .limit(page * 30)
                .list();

        ArrayList<Repository> queryRepos = new ArrayList<>();
        for (TraceRepo traceRepo : traceRepos) {
            queryRepos.add(Repository.generateFromTrace(traceRepo));
        }
        Logger.t("loadTrace").d(System.currentTimeMillis() - start);
        showQueryRepos(queryRepos, page);
    }

    private void loadBookmarks(int page) {
        List<BookMarkRepo> bookMarkRepos = daoSession.getBookMarkRepoDao().queryBuilder()
                .orderDesc(BookMarkRepoDao.Properties.MarkTime)
                .offset((page - 1) * 30)
                .limit(page * 30)
                .list();

        ArrayList<Repository> queryRepos = new ArrayList<>();
        for (BookMarkRepo bookMarkRepo : bookMarkRepos) {
            queryRepos.add(Repository.generateFromBookmark(bookMarkRepo));
        }
        showQueryRepos(queryRepos, page);
    }

    private void showQueryRepos(ArrayList<Repository> queryRepos, int page){
        if(repos == null || page == 1){
            repos = queryRepos;
        } else {
            repos.addAll(queryRepos);
        }

        mView.showRepositories(repos);
        mView.hideLoading();
    }

    public void setLanguage(TrendingLanguage language) {
        this.language = language;
    }

    private void loadCollection(boolean isReload){
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
                    parseCollectionsPageData(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        generalRxHttpExecute(forceNetWork -> getGitHubWebPageService()
                        .getCollectionInfo(forceNetWork, collection.getId()),
                httpObserver, !isReload);

    }

    private void parseCollectionsPageData(String page){
        Observable.just(page)
                .map(s -> {
                    ArrayList<Repository> repos = new ArrayList<>();
                    try {
                        Document doc = Jsoup.parse(s, AppConfig.GITHUB_BASE_URL);
                        Elements elements = doc.getElementsByTag("article");
                        for (Element element : elements) {
                            //maybe a user or an org, so add catch
                            try{
                                repos.add(parseCollectionsRepositoryData(element));
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    return repos;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    if(mView == null) return;
                    if(results.size() != 0){
                        repos = results;
                        mView.hideLoading();
                        mView.showRepositories(repos);
                    } else {
                        String errorTip = String.format(getString(R.string.github_page_parse_error),
                                getString(R.string.repo_collections));
                        mView.showLoadError(errorTip);
                        mView.hideLoading();
                    }
                });
    }

    private Repository parseCollectionsRepositoryData(Element element) throws Exception{
        String fullName = element.select("div > h1 > a").attr("href");
        fullName = fullName.substring(1);
        String owner = fullName.substring(0, fullName.lastIndexOf("/"));
        String repoName = fullName.substring(fullName.lastIndexOf("/") + 1);
//        String ownerAvatar = element.select("div > div > a > img").attr("src");
        String ownerAvatar = "";

        Elements articleElements = element.getElementsByTag("div");
        Element descElement = articleElements.get(articleElements.size() - 2);
        StringBuilder desc = new StringBuilder("");
        for(TextNode textNode : descElement.textNodes()){
            desc.append(textNode.getWholeText());
        }

        Element numElement = articleElements.last();
        String starNumStr =  numElement.select("a").get(0).textNodes().get(1).toString();
        String forkNumStr =  numElement.select("a").get(1).textNodes().get(1).toString();
        String language = "";
        Elements languageElements = numElement.select("span > span > span");
        if(languageElements.size() > 0){
            language = numElement.select("span > span > span").get(1).textNodes().get(0).toString();
        }

        Repository repo = new Repository();
        repo.setFullName(fullName);
        repo.setName(repoName);
        User user = new User();
        user.setLogin(owner);
        user.setAvatarUrl(ownerAvatar);
        repo.setOwner(user);

        repo.setDescription(desc.toString());
        repo.setStargazersCount(Integer.parseInt(starNumStr.replaceAll(" ", "")));
        repo.setForksCount(Integer.parseInt(forkNumStr.replaceAll(" ", "")));
        repo.setLanguage(language);

        return repo;
    }

    private void initSearchModelForTopic(){
        if(searchModel == null){
            searchModel = new SearchModel(SearchModel.SearchType.Repository);
            searchModel.setQuery("topic:" + topic.getId());
        }
    }

    private void loadTrending(boolean isReload){
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
                    parseTrendingPageData(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        generalRxHttpExecute(forceNetWork -> getGitHubWebPageService()
                        .getTrendingRepos(forceNetWork, language.getSlug(), since.name()),
                httpObserver, !isReload);
    }

    private void parseTrendingPageData(String page){
        Observable.just(page)
                .map(s -> {
                    ArrayList<Repository> repos = new ArrayList<>();
                    try {
                        Document doc = Jsoup.parse(s, AppConfig.GITHUB_BASE_URL);
                        Elements elements = doc.getElementsByClass("Box-row");
                        if(elements.size() != 0){
                            for (Element element : elements) {
                                try{
                                    repos.add(parseTrendingRepositoryData(element));
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        repos = null;
                    }

                    return repos;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    if(mView == null) return;
                    if(results != null){
                        repos = results;
                        mView.hideLoading();
                        mView.showRepositories(repos);
                    } else {
                        String errorTip = String.format(getString(R.string.github_page_parse_error),
                                getString(R.string.trending));
                        mView.showLoadError(errorTip);
                        mView.hideLoading();
                    }
                });
    }

    private Repository parseTrendingRepositoryData(Element element) throws Exception{
        String fullName = element.select("h1 > a").attr("href");
        fullName = fullName.substring(1);
        String owner = fullName.substring(0, fullName.lastIndexOf("/"));
        String repoName = fullName.substring(fullName.lastIndexOf("/") + 1);

        Element descElement = element.getElementsByClass("col-9 color-text-secondary my-1 pr-4").first();
        Element numElement = element.getElementsByClass("f6 color-text-secondary mt-2").first();
        StringBuilder desc = new StringBuilder("");
        String language = "unknown";
        String starNumStr = "0";
        String forkNumStr = "0";
        String periodNumStr = "0";

        try{
            if(null != descElement){
                for(TextNode textNode : descElement.textNodes()){
                    desc.append(textNode.getWholeText());
                }
            }

            if(null != numElement){
                Elements languageElements = numElement.select("span > span");
                if(null != languageElements && languageElements.size() > 0){
                    language = numElement.select("span > span").get(1).textNodes().get(0).toString().trim();
                }
                starNumStr =  numElement.select("a").get(0).textNodes().get(1).toString()
                        .replaceAll(" ", "").replaceAll(",", "");
                forkNumStr =  numElement.select("a").get(1).textNodes().get(1).toString()
                        .replaceAll(" ", "").replaceAll(",", "");
                Element periodElement =  numElement.getElementsByClass("d-inline-block float-sm-right").first();
                if(periodElement != null){
                    periodNumStr = periodElement.childNodes().get(2).toString().trim();
                    periodNumStr = periodNumStr.substring(0, periodNumStr.indexOf(" "))
                            .replaceAll(",", "");
                }
            }
        }catch (Exception e){
            desc = new StringBuilder("desc parse error.");
            Logger.e("Trending repo desc or num info parse error.", e);
        }

        Repository repo = new Repository();
        repo.setFullName(fullName);
        repo.setName(repoName);
        User user = new User();
        user.setLogin(owner);
        repo.setOwner(user);

        repo.setDescription(desc.toString().trim()
                .replaceAll("\n", ""));
        repo.setStargazersCount(Integer.parseInt(starNumStr));
        repo.setForksCount(Integer.parseInt(forkNumStr));
        repo.setSinceStargazersCount(Integer.parseInt(periodNumStr));
        repo.setLanguage(language);
        repo.setSince(since);

        return repo;
    }

    public TrendingLanguage getLanguage() {
        return language;
    }
}
