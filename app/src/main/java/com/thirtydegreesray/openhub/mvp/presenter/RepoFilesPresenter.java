

package com.thirtydegreesray.openhub.mvp.presenter;

import android.support.annotation.NonNull;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.common.SizedMap;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.error.HttpPageNoFoundError;
import com.thirtydegreesray.openhub.mvp.contract.IRepoFilesContract;
import com.thirtydegreesray.openhub.mvp.model.FileModel;
import com.thirtydegreesray.openhub.mvp.model.FilePath;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePagerPresenter;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/8/14 16:06:30
 */

public class RepoFilesPresenter extends BasePagerPresenter<IRepoFilesContract.View>
        implements IRepoFilesContract.Presenter{

    private Map<String, ArrayList<FileModel>> cacheMap;
    @AutoAccess Repository repo ;
    @AutoAccess String curPath = "";
    @AutoAccess String curBranch = "";
    private ArrayList<FilePath> filePath;
    private FilePath homePath ;

    @Inject
    public RepoFilesPresenter(DaoSession daoSession) {
        super(daoSession);
        cacheMap = new SizedMap<>();
        filePath = new ArrayList<>();
        homePath = new FilePath("", "");
        filePath.add(homePath);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
    }

    @Override
    protected void loadData() {
        loadFiles(curPath, false);
    }

    @Override
    public void loadFiles(boolean isReload) {
        loadFiles(curPath, isReload);
    }

    @Override
    public void loadFiles(@NonNull String path, boolean isReload) {
        curPath = path;
        curBranch = StringUtils.isBlank(curBranch) ? repo.getDefaultBranch() : curBranch;
        updateFilePath();
        ArrayList<FileModel> filesCache = cacheMap.get(getCacheKey());
        if(!isReload && filesCache != null){
            mView.showFiles(filesCache);
            return ;
        }

        mView.showLoading();
        HttpObserver<ArrayList<FileModel>> httpObserver =
                new HttpObserver<ArrayList<FileModel>>() {
                    @Override
                    public void onError(Throwable error) {
                        if(error instanceof HttpPageNoFoundError){
                            mView.showFiles(new ArrayList<FileModel>());
                        }else{
                            mView.showLoadError(getErrorTip(error));
                        }
                        mView.hideLoading();
                    }

                    @Override
                    public void onSuccess(HttpResponse<ArrayList<FileModel>> response) {
                        sort(response.body());
                        cacheMap.put(getCacheKey(), response.body());
                        mView.showFiles(response.body());
                        mView.hideLoading();
                    }
                };
        generalRxHttpExecute(new IObservableCreator<ArrayList<FileModel>>() {
            @Override
            public Observable<Response<ArrayList<FileModel>>> createObservable(boolean forceNetWork) {
                return getRepoService().getRepoFiles(repo.getOwner().getLogin(),
                        repo.getName(), curPath, curBranch);
            }
        }, httpObserver, false);
    }

    @Override
    public boolean goBack() {
        if(!StringUtils.isBlank(curPath)){
            curPath = curPath.contains("/") ?
                    curPath.substring(0, curPath.lastIndexOf("/")) : "";
            loadFiles(false);
            return true;
        }
        return false;
    }

    @Override
    public void goHome() {
        if(curPath.equals("")) return;
        curPath = "";
        loadFiles(false);
    }

    private void sort(ArrayList<FileModel> oriList){
        Collections.sort(oriList, new Comparator<FileModel>() {
            @Override
            public int compare(FileModel o1, FileModel o2) {
                if(!o1.getType().equals(o2.getType())){
                    return o1.isDir() ? -1 : 1;
                }
                return 0;
            }
        });
    }

    private String getCacheKey(){
        return curBranch + "-" + curPath;
    }

    public String getCurPath() {
        return curPath;
    }

    public void setCurPath(String curPath) {
        this.curPath = curPath;
    }

    public ArrayList<FilePath> getFilePath(){
        return filePath;
    }

    private void updateFilePath(){
        filePath.clear();
        filePath.add(homePath);
        if(!StringUtils.isBlank(curPath)){
            String[] pathArray = curPath.split("/");
            for(int i = 0; i < pathArray.length; i++){
                String name = pathArray[i];
                String fullPath = "";
                for(int j = 0; j <= i; j++){
                    fullPath = fullPath.concat(pathArray[j]).concat("/");
                }
                fullPath = fullPath.endsWith("/") ?
                        fullPath.substring(0, fullPath.length() - 1) : fullPath;
                FilePath path = new FilePath(name, fullPath);
                filePath.add(path);
            }
        }
        mView.showFilePath(filePath);
    }

    public String getRepoName(){
        return repo.getName();
    }

    public void setCurBranch(String curBranch) {
        this.curBranch = curBranch;
        curPath = "";
    }
}
