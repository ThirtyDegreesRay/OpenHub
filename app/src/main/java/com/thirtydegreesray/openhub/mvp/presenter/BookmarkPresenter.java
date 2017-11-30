package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.openhub.dao.Bookmark;
import com.thirtydegreesray.openhub.dao.BookmarkDao;
import com.thirtydegreesray.openhub.dao.DaoSession;
import com.thirtydegreesray.openhub.dao.LocalRepo;
import com.thirtydegreesray.openhub.dao.LocalUser;
import com.thirtydegreesray.openhub.mvp.contract.IBookmarkContract;
import com.thirtydegreesray.openhub.mvp.model.BookmarkExt;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.mvp.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by ThirtyDegreesRay on 2017/11/22 15:59:45
 */

public class BookmarkPresenter extends BasePresenter<IBookmarkContract.View>
        implements IBookmarkContract.Presenter {

    private ArrayList<BookmarkExt> bookmarks;
    private BookmarkExt removedBookmark;
    private int removedPosition;

    @Inject
    public BookmarkPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        loadBookmarks(1);
    }

    @Override
    public void loadBookmarks(int page) {
        mView.showLoading();
        final ArrayList<BookmarkExt> tempBookmarks = new ArrayList<>();
        daoSession.rxTx()
                .run(() -> {
                    List<Bookmark> bookmarkList = daoSession.getBookmarkDao().queryBuilder()
                            .orderDesc(BookmarkDao.Properties.MarkTime)
                            .offset((page - 1) * 30)
                            .limit(page * 30)
                            .list();
                    for(Bookmark bookmark : bookmarkList){
                        BookmarkExt ext = BookmarkExt.generate(bookmark);
                        if("user".equals(bookmark.getType())){
                            LocalUser localUser = daoSession.getLocalUserDao().load(ext.getUserId());
                            ext.setUser(User.generateFromLocalUser(localUser));
                        }else{
                            LocalRepo localRepo = daoSession.getLocalRepoDao().load(ext.getRepoId());
                            ext.setRepository(Repository.generateFromLocalRepo(localRepo));
                        }
                        tempBookmarks.add(ext);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    if(bookmarks == null || page == 1){
                        bookmarks = tempBookmarks;
                    } else {
                        bookmarks.addAll(tempBookmarks);
                    }
                    mView.showBookmarks(bookmarks);
                    mView.hideLoading();
                });

    }

    @Override
    public void removeBookmark(int position) {
        removedBookmark = bookmarks.remove(position);
        removedPosition = position;
        rxDBExecute(() -> daoSession.getBookmarkDao().deleteByKey(removedBookmark.getId()));
    }

    @Override
    public void undoRemoveBookmark() {
        bookmarks.add(removedPosition, removedBookmark);
        mView.notifyItemAdded(removedPosition);
        rxDBExecute(() -> daoSession.getBookmarkDao().insert(removedBookmark));
    }

}
