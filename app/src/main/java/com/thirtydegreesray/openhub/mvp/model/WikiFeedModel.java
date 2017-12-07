package com.thirtydegreesray.openhub.mvp.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/12/7 10:01:39
 */
@Root(name = "feed")
public class WikiFeedModel {

    @Element(name = "id") private String id;
    @Element(name = "title") private String title;
    @Element(name = "updated") private String updated;
    @ElementList(inline = true) private ArrayList<WikiModel> wikiList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public ArrayList<WikiModel> getWikiList() {
        return wikiList;
    }

    public void setWikiList(ArrayList<WikiModel> wikiList) {
        this.wikiList = wikiList;
    }

}
