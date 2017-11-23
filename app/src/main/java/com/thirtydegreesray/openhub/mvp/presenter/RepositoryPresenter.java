

package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.Bookmark;
import com.thirtydegreesray.openhub.dao.BookmarkDao;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.dao.LocalRepo;
import com.thirtydegreesray.openhub.dao.Trace;
import com.thirtydegreesray.openhub.dao.TraceDao;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpProgressSubscriber;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.mvp.contract.IRepositoryContract;
import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.util.StarWishesHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:42:47
 */

public class RepositoryPresenter extends BasePresenter<IRepositoryContract.View>
        implements IRepositoryContract.Presenter {

    @AutoAccess(dataName = "repository") Repository repository;

    @AutoAccess String owner;
    @AutoAccess String repoName;

    private ArrayList<Branch> branches;
    @AutoAccess Branch curBranch;
    private boolean starred;
    private boolean watched;

    private boolean isStatusChecked = false;
    @AutoAccess boolean isTraceSaved = false;

    private boolean isBookmarkQueried = false;
    private boolean bookmarked = false;

    @Inject
    public RepositoryPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (repository != null) {
            owner = repository.getOwner().getLogin();
            repoName = repository.getName();
            initCurBranch();
            mView.showRepo(repository);
            getRepoInfo(false);
            checkStatus();
        } else {
            getRepoInfo(true);
        }
    }

    @Override
    public void loadBranchesAndTags() {
        if (branches != null) {
            mView.showBranchesAndTags(branches, curBranch);
            return;
        }
        HttpProgressSubscriber<ArrayList<Branch>> httpProgressSubscriber =
                new HttpProgressSubscriber<>(
                        mView.getProgressDialog(getLoadTip()),
                        new HttpObserver<ArrayList<Branch>>() {
                            @Override
                            public void onError(Throwable error) {
                                mView.showErrorToast(getErrorTip(error));
                            }

                            @Override
                            public void onSuccess(HttpResponse<ArrayList<Branch>> response) {
                                setTags(response.body());
                                branches.addAll(response.body());
                                mView.showBranchesAndTags(branches, curBranch);
                            }
                        }
                );
        Observable<Response<ArrayList<Branch>>> observable = getRepoService().getBranches(owner, repoName)
                .flatMap(new Func1<Response<ArrayList<Branch>>, Observable<Response<ArrayList<Branch>>>>() {
                    @Override
                    public Observable<Response<ArrayList<Branch>>> call(
                            Response<ArrayList<Branch>> arrayListResponse) {
                        branches = arrayListResponse.body();
                        return getRepoService().getTags(owner, repoName);
                    }
                });
        generalRxHttpExecute(observable, httpProgressSubscriber);
    }

    @Override
    public void starRepo(boolean star) {
        starred = star;
        Observable<Response<ResponseBody>> observable = starred ?
                getRepoService().starRepo(owner, repoName) :
                getRepoService().unstarRepo(owner, repoName);
        executeSimpleRequest(observable);
    }

    @Override
    public void watchRepo(boolean watch) {
        watched = watch;
        Observable<Response<ResponseBody>> observable = watched ?
                getRepoService().watchRepo(owner, repoName) :
                getRepoService().unwatchRepo(owner, repoName);
        executeSimpleRequest(observable);
    }

    @Override
    public void createFork() {
        mView.getProgressDialog(getLoadTip()).show();
        HttpObserver<Repository> httpObserver = new HttpObserver<Repository>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
                mView.getProgressDialog(getLoadTip()).dismiss();
            }

            @Override
            public void onSuccess(HttpResponse<Repository> response) {
                if(response.body() != null) {
                    mView.showSuccessToast(getString(R.string.forked));
                    RepositoryActivity.show(getContext(), response.body());
                } else {
                    mView.showErrorToast(getString(R.string.fork_failed));
                }
                mView.getProgressDialog(getLoadTip()).dismiss();
            }
        };
        generalRxHttpExecute(new IObservableCreator<Repository>() {
            @Override
            public Observable<Response<Repository>> createObservable(boolean forceNetWork) {
                return getRepoService().createFork(owner, repoName);
            }
        }, httpObserver);
    }

    @Override
    public boolean isForkEnable() {
        if(repository != null && !repository.isFork() &&
                !repository.getOwner().getLogin().equals(AppData.INSTANCE.getLoggedUser().getLogin())){
            return true;
        }
        return false;
    }

    private void setTags(ArrayList<Branch> list) {
        for (Branch branch : list) {
            branch.setBranch(false);
        }
    }

    private void getRepoInfo(final boolean isShowLoading) {
        if (isShowLoading) mView.showLoading();
        HttpObserver<Repository> httpObserver =
                new HttpObserver<Repository>() {
                    @Override
                    public void onError(Throwable error) {
                        if (isShowLoading) mView.hideLoading();
                        mView.showErrorToast(getErrorTip(error));
                    }

                    @Override
                    public void onSuccess(HttpResponse<Repository> response) {
                        if (isShowLoading) mView.hideLoading();
                        repository = response.body();
                        initCurBranch();
                        mView.showRepo(repository);
                        checkStatus();
                        saveTrace();
                    }
                };

        generalRxHttpExecute(new IObservableCreator<Repository>() {
            @Override
            public Observable<Response<Repository>> createObservable(boolean forceNetWork) {
                return getRepoService().getRepoInfo(forceNetWork, owner, repoName);
            }
        }, httpObserver, true);
    }

    private void checkStatus(){
        if(isStatusChecked) return;
        isStatusChecked = true;
        checkStarred();
        checkWatched();
    }


    private void checkStarred() {
        checkStatus(
                getRepoService().checkRepoStarred(owner, repoName),
                new CheckStatusCallback() {
                    @Override
                    public void onChecked(boolean status) {
                        starred = status;
                        mView.invalidateOptionsMenu();
                        starWishes();
                    }
                }
        );
    }

    private void checkWatched() {
        checkStatus(
                getRepoService().checkRepoWatched(owner, repoName),
                new CheckStatusCallback() {
                    @Override
                    public void onChecked(boolean status) {
                        watched = status;
                        mView.invalidateOptionsMenu();
                    }
                }
        );
    }

    private void initCurBranch(){
        curBranch = new Branch(repository.getDefaultBranch());
        curBranch.setZipballUrl("https://github.com/".concat(owner).concat("/")
                .concat(repoName).concat("/archive/")
                .concat(curBranch.getName()).concat(".zip"));
        curBranch.setTarballUrl("https://github.com/".concat(owner).concat("/")
                .concat(repoName).concat("/archive/")
                .concat(curBranch.getName()).concat(".tar.gz"));
    }

    public Repository getRepository() {
        return repository;
    }

    public boolean isFork() {
        return repository != null && repository.isFork();
    }

    public boolean isStarred() {
        return starred;
    }

    public boolean isWatched() {
        return watched;
    }

    public String getZipSourceUrl(){
        return curBranch.getZipballUrl();
    }

    public String getZipSourceName(){
        return repoName.concat("-").concat(curBranch.getName()).concat(".zip");
    }

    public String getTarSourceUrl(){
        return curBranch.getTarballUrl();
    }

    public String getTarSourceName(){
        return repoName.concat("-").concat(curBranch.getName()).concat(".tar.gz");
    }

    public void setCurBranch(Branch curBranch) {
        this.curBranch = curBranch;
    }

    public String getRepoName() {
        return repository == null ? repoName : repository.getName();
    }

    private void starWishes(){
        if(!starred && getString(R.string.author_login_id).equals(owner)
                && getString(R.string.app_name).equals(repoName)
                && StarWishesHelper.isStarWishesTipable()){
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!starred && mView != null){
                        mView.showStarWishes();
                    }
                }
            }, 3000);
        }
    }

    @Override
    public boolean isBookmarked() {
        if(!isBookmarkQueried && repository != null){
            bookmarked = daoSession.getBookmarkDao().queryBuilder()
                    .where(BookmarkDao.Properties.RepoId.eq(repository.getId()))
                    .unique() != null;
            isBookmarkQueried = true;
        }
        return bookmarked;
    }

    @Override
    public void bookmark(boolean bookmark) {
        if(repository == null) return;
        bookmarked = bookmark;
        Bookmark bookmarkModel = daoSession.getBookmarkDao().queryBuilder()
                .where(BookmarkDao.Properties.RepoId.eq(repository.getId()))
                .unique();
        if(bookmark && bookmarkModel == null){
            bookmarkModel = new Bookmark(UUID.randomUUID().toString());
            bookmarkModel.setType("repo");
            bookmarkModel.setRepoId((long) repository.getId());
            bookmarkModel.setMarkTime(new Date());
            daoSession.getBookmarkDao().insert(bookmarkModel);
        } else if(!bookmark && bookmarkModel != null){
            daoSession.getBookmarkDao().delete(bookmarkModel);
        }
    }

    private void saveTrace(){
        daoSession.runInTx(() ->{
            if(!isTraceSaved){
                Trace trace = daoSession.getTraceDao().queryBuilder()
                        .where(TraceDao.Properties.RepoId.eq(repository.getId()))
                        .unique();

                if(trace == null){
                    trace = new Trace(UUID.randomUUID().toString());
                    trace.setType("repo");
                    trace.setRepoId((long) repository.getId());
                    Date curDate = new Date();
                    trace.setStartTime(curDate);
                    trace.setLatestTime(curDate);
                    trace.setTraceNum(1);
                    daoSession.getTraceDao().insert(trace);
                } else {
                    trace.setTraceNum(trace.getTraceNum() + 1);
                    trace.setLatestTime(new Date());
                    daoSession.getTraceDao().update(trace);
                }
            }

            LocalRepo localRepo = daoSession.getLocalRepoDao().load((long) repository.getId());
            LocalRepo updateRepo = repository.toLocalRepo();
            if(localRepo == null){
                daoSession.getLocalRepoDao().insert(updateRepo);
            } else {
                daoSession.getLocalRepoDao().update(updateRepo);
            }
        });
        isTraceSaved = true;
    }

}
