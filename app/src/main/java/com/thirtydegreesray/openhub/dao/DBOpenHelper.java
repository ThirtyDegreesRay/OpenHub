package com.thirtydegreesray.openhub.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ThirtyDegreesRay on 2017/11/13 10:40:22
 */

public class DBOpenHelper extends DaoMaster.DevOpenHelper {

    public DBOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if(oldVersion == 2 && newVersion == 3){
            //create new table, keep ori
            TraceUserDao.createTable(db, false);
            TraceRepoDao.createTable(db, false);
            BookMarkUserDao.createTable(db, false);
            BookMarkRepoDao.createTable(db, false);
        } else if(oldVersion == 3 && newVersion == 4){
            //create new table
            LocalUserDao.createTable(db, false);
            LocalRepoDao.createTable(db, false);
            TraceDao.createTable(db, false);
            BookmarkDao.createTable(db, false);

            //transfer data from ori
            transferBookmarksAndTraceData(db);

            //drop old tables
            TraceUserDao.dropTable(db, true);
            TraceRepoDao.dropTable(db, true);
            BookMarkUserDao.dropTable(db, true);
            BookMarkRepoDao.dropTable(db, true);
        } else if(oldVersion == 4 && newVersion == 5){
            MyTrendingLanguageDao.createTable(db, true);
        } else {
            super.onUpgrade(db, oldVersion, newVersion);
        }
    }

    private void transferBookmarksAndTraceData(Database db){
        DaoSession daoSession = new DaoMaster(db).newSession();
        List<TraceRepo> traceRepoList = daoSession.getTraceRepoDao().loadAll();
        List<TraceUser> traceUserList = daoSession.getTraceUserDao().loadAll();
        List<BookMarkRepo> bookmarkRepoList = daoSession.getBookMarkRepoDao().loadAll();
        List<BookMarkUser> bookMarkUserList = daoSession.getBookMarkUserDao().loadAll();

        List<LocalRepo> localRepoList = getLocalRepoList(traceRepoList, bookmarkRepoList);
        List<LocalUser> localUserList = getLocalUserList(traceUserList, bookMarkUserList);
        List<Trace> traceList = getTraceList(traceRepoList, traceUserList);
        List<Bookmark> bookmarkList = getBookmarkList(bookmarkRepoList, bookMarkUserList);

        daoSession.getLocalRepoDao().insertInTx(localRepoList);
        daoSession.getLocalUserDao().insertInTx(localUserList);
        daoSession.getTraceDao().insertInTx(traceList);
        daoSession.getBookmarkDao().insertInTx(bookmarkList);

        daoSession.clear();
    }

    private List<LocalRepo> getLocalRepoList(List<TraceRepo> traceRepoList, List<BookMarkRepo> bookmarkRepoList){
        List<LocalRepo> localRepoList = new ArrayList<>();
        List<Long> repoIdList = new ArrayList<>();
        for(TraceRepo repo : traceRepoList){
            if(!repoIdList.contains(repo.getId())){
                LocalRepo localRepo = new LocalRepo();
                localRepo.setId(repo.getId());
                localRepo.setDescription(repo.getDescription());
                localRepo.setFork(repo.getFork());
                localRepo.setForksCount(repo.getForksCount());
                localRepo.setLanguage(repo.getLanguage());
                localRepo.setName(repo.getName());
                localRepo.setOwnerAvatarUrl(repo.getOwnerAvatarUrl());
                localRepo.setOwnerLogin(repo.getOwnerLogin());
                localRepo.setStargazersCount(repo.getStargazersCount());
                localRepo.setWatchersCount(repo.getWatchersCount());
                localRepoList.add(localRepo);
                repoIdList.add(repo.getId());
            }
        }
        for(BookMarkRepo repo : bookmarkRepoList){
            if(!repoIdList.contains(repo.getId())){
                LocalRepo localRepo = new LocalRepo();
                localRepo.setId(repo.getId());
                localRepo.setDescription(repo.getDescription());
                localRepo.setFork(repo.getFork());
                localRepo.setForksCount(repo.getForksCount());
                localRepo.setLanguage(repo.getLanguage());
                localRepo.setName(repo.getName());
                localRepo.setOwnerAvatarUrl(repo.getOwnerAvatarUrl());
                localRepo.setOwnerLogin(repo.getOwnerLogin());
                localRepo.setStargazersCount(repo.getStargazersCount());
                localRepo.setWatchersCount(repo.getWatchersCount());
                localRepoList.add(localRepo);
                repoIdList.add(repo.getId());
            }
        }
        return localRepoList;
    }

    private List<LocalUser> getLocalUserList(List<TraceUser> traceUserList, List<BookMarkUser> bookMarkUserList){
        List<LocalUser> localUserList = new ArrayList<>();
        List<String> userIdList = new ArrayList<>();
        for(TraceUser user : traceUserList){
            if(!userIdList.contains(user.getLogin())){
                LocalUser localUser = new LocalUser();
                localUser.setLogin(user.getLogin());
                localUser.setAvatarUrl(user.getAvatarUrl());
                localUser.setFollowers(user.getFollowers());
                localUser.setFollowing(user.getFollowing());
                localUser.setName(user.getName());
                localUserList.add(localUser);
                userIdList.add(user.getLogin());
            }
        }
        for(BookMarkUser user : bookMarkUserList){
            if(!userIdList.contains(user.getLogin())){
                LocalUser localUser = new LocalUser();
                localUser.setLogin(user.getLogin());
                localUser.setAvatarUrl(user.getAvatarUrl());
                localUser.setFollowers(user.getFollowers());
                localUser.setFollowing(user.getFollowing());
                localUser.setName(user.getName());
                localUserList.add(localUser);
                userIdList.add(user.getLogin());
            }
        }
        return localUserList;
    }

    private List<Trace> getTraceList(List<TraceRepo> traceRepoList, List<TraceUser> traceUserList){
        List<Trace> traceList = new ArrayList<>();
        for(TraceRepo oriTrace : traceRepoList){
            Trace trace = new Trace(UUID.randomUUID().toString());
            trace.setType("repo");
            trace.setRepoId(oriTrace.getId());
            trace.setStartTime(oriTrace.getStartTime());
            trace.setLatestTime(oriTrace.getLatestTime());
            trace.setTraceNum(oriTrace.getTraceNum());
            traceList.add(trace);
        }
        for(TraceUser oriTrace : traceUserList){
            Trace trace = new Trace(UUID.randomUUID().toString());
            trace.setType("user");
            trace.setUserId(oriTrace.getLogin());
            trace.setStartTime(oriTrace.getStartTime());
            trace.setLatestTime(oriTrace.getLatestTime());
            trace.setTraceNum(oriTrace.getTraceNum());
            traceList.add(trace);
        }
        return traceList;
    }

    private List<Bookmark> getBookmarkList(List<BookMarkRepo> bookmarkRepoList, List<BookMarkUser> bookMarkUserList){
        List<Bookmark> bookmarkList = new ArrayList<>();
        for(BookMarkRepo oriBookmark : bookmarkRepoList){
            Bookmark bookmark = new Bookmark(UUID.randomUUID().toString());
            bookmark.setType("repo");
            bookmark.setRepoId(oriBookmark.getId());
            bookmark.setMarkTime(oriBookmark.getMarkTime());
            bookmarkList.add(bookmark);
        }
        for(BookMarkUser oriBookmark : bookMarkUserList){
            Bookmark bookmark = new Bookmark(UUID.randomUUID().toString());
            bookmark.setType("user");
            bookmark.setUserId(oriBookmark.getLogin());
            bookmark.setMarkTime(oriBookmark.getMarkTime());
            bookmarkList.add(bookmark);
        }
        return bookmarkList;
    }

}
