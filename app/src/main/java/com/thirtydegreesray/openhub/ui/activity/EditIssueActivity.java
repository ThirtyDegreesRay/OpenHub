/*
 *    Copyright 2017 ThirtyDegreesRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.mvp.contract.IEditIssueContract;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.presenter.EditIssuePresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/10/13 17:37:35
 */

public class EditIssueActivity extends BaseActivity<EditIssuePresenter>
        implements IEditIssueContract.View {

    @BindView(R.id.title_edit) TextInputEditText titleEdit;
    @BindView(R.id.title_layout) TextInputLayout titleLayout;
    @BindView(R.id.comment_edit) TextInputEditText commentEdit;
    @BindView(R.id.comment_layout) TextInputLayout commentLayout;

    public static void showForAdd(@NonNull Activity activity, @NonNull String userId,
                                  @NonNull String repoName, int requestCode) {
        Intent intent = new Intent(activity, EditIssueActivity.class);
        intent.putExtras(BundleBuilder.builder().put("addMode", true).put("userId", userId)
                .put("repoName", repoName).build());
        activity.startActivityForResult(intent, requestCode);
    }

    public static void showForEdit(@NonNull Activity activity, @NonNull Issue issue, int requestCode) {
        Intent intent = new Intent(activity, EditIssueActivity.class);
        intent.putExtras(BundleBuilder.builder().put("addMode", false).put("issue", issue).build());
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
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        setToolbarTitle(mPresenter.isAddMode() ? getString(R.string.create_issue) : getString(R.string.edit_issue));
        titleEdit.setText(mPresenter.getIssueTitle());
        commentEdit.setText(mPresenter.getIssueComment());
        titleEdit.setSelection(titleEdit.length());
        commentEdit.setSelection(commentEdit.length());
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

    @OnClick({R.id.markdown_editor_bn, R.id.add_issue_bn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.markdown_editor_bn:
                MarkdownEditorActivity.show(getActivity(), R.string.edit,
                        MARKDOWN_EDITOR_REQUEST_CODE, commentEdit.getText().toString());
                break;
            case R.id.add_issue_bn:
                if(checkForCommit())
                    mPresenter.commitIssue(titleEdit.getText().toString(), commentEdit.getText().toString());
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
}
