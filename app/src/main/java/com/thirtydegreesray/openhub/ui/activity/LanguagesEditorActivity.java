package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.View;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.model.TrendingLanguage;
import com.thirtydegreesray.openhub.ui.activity.base.SingleFragmentActivity;
import com.thirtydegreesray.openhub.ui.fragment.LanguagesEditorFragment;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.ui.widget.ZoomAbleFloatingActionButton;
import com.thirtydegreesray.openhub.util.BundleHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ThirtyDegreesRay on 2017/11/28 18:55:27
 */

public class LanguagesEditorActivity extends
        SingleFragmentActivity<IBaseContract.Presenter, LanguagesEditorFragment>
        implements ListFragment.ListScrollListener{

    public static void showForChoose(@NonNull Activity activity, @NonNull LanguageEditorMode mode,
                                     @NonNull ArrayList<TrendingLanguage> selectedLanguages, int requestCode) {
        Intent intent = new Intent(activity, LanguagesEditorActivity.class);
        intent.putExtras(BundleHelper.builder().put("mode", mode)
                .put("selectedLanguages", selectedLanguages).build());
        activity.startActivityForResult(intent, requestCode);
    }

    public static void show(@NonNull Activity activity, @NonNull LanguageEditorMode mode,
                            int requestCode) {
        Intent intent = new Intent(activity, LanguagesEditorActivity.class);
        intent.putExtras(BundleHelper.builder().put("mode", mode).build());
        activity.startActivityForResult(intent, requestCode);
    }

    @BindView(R.id.float_action_bn) ZoomAbleFloatingActionButton floatingActionButton;
    private final int ADD_LANGUAGE_REQUEST_CODE = 100;

    @Override
    public void onScrollUp() {
        floatingActionButton.zoomIn();
    }

    @Override
    public void onScrollDown() {
        floatingActionButton.zoomOut();
    }

    public enum LanguageEditorMode {
        Sort, Choose
    }

    @AutoAccess LanguageEditorMode mode;
    @AutoAccess ArrayList<TrendingLanguage> selectedLanguages;

    @Override
    protected LanguagesEditorFragment createFragment() {
        if (LanguageEditorMode.Sort.equals(mode)) {
            LanguagesEditorFragment fragment = LanguagesEditorFragment.create(mode);
            fragment.setListScrollListener(this);
            return fragment;
        } else {
            return LanguagesEditorFragment.createForChoose(mode, selectedLanguages);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (LanguageEditorMode.Sort.equals(mode)) {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setImageResource(R.drawable.ic_add);
            setToolbarTitle(getString(R.string.my_languages));
        } else {
            setToolbarTitle(getString(R.string.choose_languages));
        }
        setToolbarScrollAble(true);
    }

    @OnClick(R.id.float_action_bn)
    public void onAddClick() {
        LanguagesEditorActivity.showForChoose(getActivity(), LanguageEditorMode.Choose,
                getFragment().getSelectedLanguages(), ADD_LANGUAGE_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ADD_LANGUAGE_REQUEST_CODE) {
            getFragment().updateLanguages();
        }

    }
}
