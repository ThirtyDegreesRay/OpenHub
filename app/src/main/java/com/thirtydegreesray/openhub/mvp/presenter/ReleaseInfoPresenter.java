package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.mvp.contract.IReleaseInfoContract;
import com.thirtydegreesray.openhub.mvp.model.DownloadSource;
import com.thirtydegreesray.openhub.mvp.model.Release;
import com.thirtydegreesray.openhub.mvp.model.ReleaseAsset;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 13:11:46
 */

public class ReleaseInfoPresenter extends BasePresenter<IReleaseInfoContract.View>
        implements IReleaseInfoContract.Presenter{

    @AutoAccess String repoName;
    @AutoAccess Release release;

    @Inject
    public ReleaseInfoPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if(release != null){
            mView.showReleaseInfo(release);
        } else {
            loadReleaseInfo();
        }
    }

    private void loadReleaseInfo(){

    }

    public String getTagName(){
        return  release.getTagName();
    }

    public String getRepoName() {
        return repoName;
    }

    public Release getRelease(){
        return release;
    }

    public ArrayList<DownloadSource> getDownloadSources(){
        ArrayList<DownloadSource> sources = new ArrayList<>();
        for(ReleaseAsset asset : release.getAssets()){
            sources.add(new DownloadSource(asset.getDownloadUrl(), false, asset.getName(), asset.getSize()));
        }
        sources.add(new DownloadSource(release.getZipballUrl(), true, getString(R.string.source_code_zip)));
        sources.add(new DownloadSource(release.getTarballUrl(), true, getString(R.string.source_code_tar)));
        return sources;
    }

}
