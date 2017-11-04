package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.ui.activity.MarkdownEditorCallback;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.StringUtils;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/9/29 11:52:42
 */
//FIXME click link cause exception
public class MarkdownPreviewFragment extends BaseFragment{

    public static MarkdownPreviewFragment create(){
        MarkdownPreviewFragment fragment = new MarkdownPreviewFragment();
        return fragment;
    }

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
        previewText.setText(R.string.nothing_to_preview);
    }

    private MarkdownEditorCallback getMarkdownEditorCallback(){
        return (MarkdownEditorCallback) getActivity();
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(getMarkdownEditorCallback().isTextChanged()){
            if(StringUtils.isBlank(getMarkdownEditorCallback().getText())){
                previewText.setText(R.string.nothing_to_preview);
            }else{
                RichText.fromMarkdown(getMarkdownEditorCallback().getText()).into(previewText);
            }
        }
    }

}
