package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.ui.activity.MarkdownEditorActivity;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/9/29 11:52:42
 */

public class MarkdownPreviewFragment extends BaseFragment{

    public static MarkdownPreviewFragment create(@NonNull MarkdownEditorActivity.MarkdownEditor markdownEditor){
        MarkdownPreviewFragment fragment = new MarkdownPreviewFragment();
        fragment.setMarkdownEditor(markdownEditor);
        return fragment;
    }

    private MarkdownEditorActivity.MarkdownEditor markdownEditor;
    @BindView(R.id.preview_text) TextView previewText;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_markdown_preview;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {

    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {

    }

    public void setMarkdownEditor(MarkdownEditorActivity.MarkdownEditor markdownEditor) {
        this.markdownEditor = markdownEditor;
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(markdownEditor.isTextChanged()){
            RichText.fromMarkdown(markdownEditor.getText()).into(previewText);
        }
    }

}
