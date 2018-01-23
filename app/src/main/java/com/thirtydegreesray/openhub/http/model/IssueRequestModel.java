package com.thirtydegreesray.openhub.http.model;

import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.Label;
import com.thirtydegreesray.openhub.mvp.model.User;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2018/1/23 11:28:18
 */

public class IssueRequestModel {

    private String title;
    private Issue.IssueState state;
    private String body;
    private ArrayList<Label> labels;
    private ArrayList<User> assignees;

    public static IssueRequestModel generateFromIssue(Issue issue){
        IssueRequestModel model = new IssueRequestModel();
        model.setTitle(issue.getTitle());
        model.setState(issue.getState());
        model.setBody(issue.getBody());
        model.setLabels(issue.getLabels());
        model.setAssignees(issue.getAssignees());
        return model;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Issue.IssueState getState() {
        return state;
    }

    public void setState(Issue.IssueState state) {
        this.state = state;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ArrayList<Label> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Label> labels) {
        this.labels = labels;
    }

    public ArrayList<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(ArrayList<User> assignees) {
        this.assignees = assignees;
    }

}
