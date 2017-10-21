package com.thirtydegreesray.openhub.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.ui.activity.MarkdownEditorCallback;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.BundleBuilder;
import com.thirtydegreesray.openhub.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by ThirtyDegreesRay on 2017/9/29 11:52:17
 */

public class MarkdownEditorFragment extends BaseFragment
        implements MarkdownEditorCallback {

    public static MarkdownEditorFragment create(@Nullable String text) {
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
        if (!StringUtils.isBlank(text)) {
            isTextChanged = true;
            markdownEdit.setText(text);
            markdownEdit.setSelection(text.length());
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

    @OnClick({R.id.add_large_head, R.id.add_medium_head, R.id.add_small_head, R.id.add_bold,
            R.id.add_italic, R.id.add_quote, R.id.insert_code, R.id.add_link, R.id.add_bulleted_list,
            R.id.add_mention})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_large_head:
                addKeyWord("#");
                break;
            case R.id.add_medium_head:
                addKeyWord("##");
                break;
            case R.id.add_small_head:
                addKeyWord("###");
                break;
            case R.id.add_bold:
                addKeyWord("****", 2);
                break;
            case R.id.add_italic:
                addKeyWord("__", 1);
                break;
            case R.id.add_quote:
                addKeyWord(">");
                break;
            case R.id.insert_code:
                addKeyWord("``", 1);
                break;
            case R.id.add_link:
                addKeyWord("[](url)", 1);
                break;
            case R.id.add_bulleted_list:
                addKeyWord("-");
                break;
            case R.id.add_mention:
                addKeyWord("@", 1);
                break;
        }
    }

    private void addKeyWord(String keyWord){
        addKeyWord(keyWord, -1);
    }

    private void addKeyWord(String keyWord, int cursorPosition){
        int cursorIndex = markdownEdit.getSelectionStart();
        String curContent = markdownEdit.getText().toString();
        String preStr = curContent.substring(0, cursorIndex);
        String sufStr = curContent.substring(cursorIndex);
        boolean needPreBlank = !StringUtils.isBlank(preStr)
                && preStr.charAt(preStr.length() - 1) != ' '
                && preStr.charAt(preStr.length() - 1) != '\n';
        String newStr = preStr;
        if(needPreBlank) {
            newStr = newStr.concat(" ");
        }
        newStr = newStr.concat(keyWord).concat(" ").concat(sufStr);
        int newCursorIndex = cursorIndex + (needPreBlank ? 1 : 0)
                + (cursorPosition == -1 ? keyWord.length() + 1 : cursorPosition);
        markdownEdit.setText(newStr);
        markdownEdit.setSelection(newCursorIndex);
    }

}
