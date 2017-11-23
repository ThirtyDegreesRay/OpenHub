package com.thirtydegreesray.openhub.mvp.model;

import com.thirtydegreesray.openhub.dao.Trace;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ThirtyDegreesRay on 2017/11/23 10:32:30
 */

public class TraceExt extends Trace {

    private User user;
    private Repository repository;

    public static TraceExt generate(Trace trace){
        TraceExt ext = new TraceExt();
        ext.setId(trace.getId());
        ext.setRepoId(trace.getRepoId());
        ext.setUserId(trace.getUserId());
        ext.setLatestTime(trace.getLatestTime());
        ext.setStartTime(trace.getStartTime());
        ext.setTraceNum(trace.getTraceNum());
        ext.setType(trace.getType());
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

    public Date getLatestDate(){
        if(getLatestTime() == null){
            return null;
        } else {
            return StringUtils.getDateByTime(getLatestTime());
        }
    }

}
