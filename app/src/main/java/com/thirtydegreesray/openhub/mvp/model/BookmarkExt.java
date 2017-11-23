package com.thirtydegreesray.openhub.mvp.model;

import com.thirtydegreesray.openhub.dao.Bookmark;

/**
 * Created by ThirtyDegreesRay on 2017/11/22 16:13:51
 */

public class BookmarkExt extends Bookmark {

    private User user;
    private Repository repository;

    public static BookmarkExt generate(Bookmark bookmark){
        BookmarkExt ext = new BookmarkExt();
        ext.setId(bookmark.getId());
        ext.setMarkTime(bookmark.getMarkTime());
        ext.setRepoId(bookmark.getRepoId());
        ext.setUserId(bookmark.getUserId());
        ext.setType(bookmark.getType());
        return ext;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

}
