

package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.IEditIssueContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.Label;
import com.thirtydegreesray.openhub.mvp.presenter.EditIssuePresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.ui.widget.ChooseLabelsDialog;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.thirtydegreesray.openhub.util.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/10/13 17:37:35
 */

public class EditIssueActivity extends BaseActivity<EditIssuePresenter>
        implements IEditIssueContract.View, ChooseLabelsDialog.ChooseLabelsListener {

    @BindView(R.id.title_edit) TextInputEditText titleEdit;
    @BindView(R.id.title_layout) TextInputLayout titleLayout;
    @BindView(R.id.comment_edit) TextInputEditText commentEdit;
    @BindView(R.id.comment_layout) TextInputLayout commentLayout;
    @BindView(R.id.labels_text) TextView labelsText;
    @BindView(R.id.edit_labels) LinearLayout editLabelsLay;

    public static void showForAdd(@NonNull Activity activity, @NonNull String userId,
                                  @NonNull String repoName, int requestCode) {
        Intent intent = new Intent(activity, EditIssueActivity.class);
        intent.putExtras(BundleHelper.builder().put("addMode", true).put("userId", userId)
                .put("repoName", repoName).build());
        activity.startActivityForResult(intent, requestCode);
    }

    public static void showForEdit(@NonNull Activity activity, @NonNull Issue issue, int requestCode) {
        Intent intent = new Intent(activity, EditIssueActivity.class);
        intent.putExtras(BundleHelper.builder().put("addMode", false).put("issue", issue).build());
        activity.startActivityForResult(intent, requestCode);
    }

    private final int MARKDOWN_EDITOR_REQUEST_CODE= 100;
    @AutoAccess String titleEditStr;
    @AutoAccess String commentEditStr;


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_edit_issue;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_commit){
            if(checkForCommit())
                mPresenter.commitIssue(titleEdit.getText().toString(), commentEdit.getText().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        setToolbarTitle(mPresenter.isAddMode() ? getString(R.string.create_issue) : getString(R.string.edit_issue));
        titleEdit.setText(mPresenter.getIssueTitle());
        commentEdit.setText(mPresenter.getIssueComment());
        titleEdit.setSelection(titleEdit.length());
        commentEdit.setSelection(commentEdit.length());

        boolean isOwner = AppData.INSTANCE.getLoggedUser().getLogin().equals(mPresenter.getRepoOwner());
        if(isOwner){
            editLabelsLay.setVisibility(View.VISIBLE);
            setLabelsText();
        } else {
            editLabelsLay.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNewIssue(@NonNull Issue issue) {
        showSuccessToast(mPresenter.isAddMode() ? getString(R.string.create_issue_success) :
                getString(R.string.edit_issue_success));
        Intent data = new Intent();
        data.putExtra("issue", issue);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onLoadLabelsComplete(ArrayList<Label> labels) {
        new ChooseLabelsDialog(getActivity(), labels, this).show();
    }

    @OnClick({R.id.markdown_editor_bn, R.id.edit_labels})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.markdown_editor_bn:
                MarkdownEditorActivity.show(getActivity(), R.string.edit,
                        MARKDOWN_EDITOR_REQUEST_CODE, commentEdit.getText().toString());
                break;
            case R.id.edit_labels:
                mPresenter.loadLabels();
                break;

        }
    }

    private boolean checkForCommit(){
        if(StringUtils.isBlank(titleEdit.getText().toString())){
            titleLayout.setError(getString(R.string.issue_title_null_tip));
            return false;
        }else {
            titleLayout.setErrorEnabled(false);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        if(requestCode == MARKDOWN_EDITOR_REQUEST_CODE){
            String text = data.getStringExtra("text");
            commentEdit.setText(text);
            commentEdit.requestFocus();
            commentEdit.setSelection(text.length());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        titleEditStr = titleEdit.getText().toString();
        commentEditStr = commentEdit.getText().toString();
    }

    private void setLabelsText(){
        labelsText.setText(ViewUtils.getLabelsSpan(getActivity(), mPresenter.getLabels()));
    }

    @Override
    public void onChooseLabelsComplete(@NonNull ArrayList<Label> labels) {
        mPresenter.getIssue().setLabels(labels);
        setLabelsText();
    }

    @Override
    public void onShowManageLabels() {
        LabelManageActivity.show(getActivity(), mPresenter.getRepoOwner(), mPresenter.getRepoName());
        mPresenter.clearAllLabelsData();
    }
}
