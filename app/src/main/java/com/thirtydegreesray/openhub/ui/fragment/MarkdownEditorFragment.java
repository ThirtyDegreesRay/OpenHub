package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.ui.activity.MarkdownEditorActivity;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.StringUtils;

import butterknife.BindView;
import butterknife.OnTextChanged;

/**
 * Created by ThirtyDegreesRay on 2017/9/29 11:52:17
 */

public class MarkdownEditorFragment extends BaseFragment
        implements MarkdownEditorActivity.MarkdownEditor{

    public static MarkdownEditorFragment create(@Nullable String text){
        MarkdownEditorFragment fragment = new MarkdownEditorFragment();
        fragment.setArguments(BundleBuilder.builder().put("text", text).build());
        return fragment;
    }

    @BindView(R.id.markdown_edit) EditText markdownEdit;
    @AutoAccess boolean isTextChanged = false;
    @AutoAccess String text;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_markdown_editor;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {

    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        if(!StringUtils.isBlank(text)){
            isTextChanged = true;
            markdownEdit.setText(text);
        }
    }

    @OnTextChanged(R.id.markdown_edit)
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isTextChanged = true;
    }

    @Override
    public String getText() {
        return markdownEdit == null ? null : markdownEdit.getText().toString();
    }

    @Override
    public boolean isTextChanged() {
        isTextChanged = !isTextChanged;
        return !isTextChanged;
    }
}
