package com.thirtydegreesray.openhub.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerFragmentComponent;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.mvp.contract.ILanguagesEditorContract;
import com.thirtydegreesray.openhub.mvp.model.TrendingLanguage;
import com.thirtydegreesray.openhub.mvp.presenter.LanguagesEditorPresenter;
import com.thirtydegreesray.openhub.ui.activity.LanguagesEditorActivity;
import com.thirtydegreesray.openhub.ui.adapter.LanguagesAdapter;
import com.thirtydegreesray.openhub.ui.adapter.base.ItemTouchHelperCallback;
import com.thirtydegreesray.openhub.ui.fragment.base.ListFragment;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.PrefUtils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/11/28 18:57:52
 */

public class LanguagesEditorFragment extends ListFragment<LanguagesEditorPresenter, LanguagesAdapter>
        implements ILanguagesEditorContract.View, ItemTouchHelperCallback.ItemGestureListener{

    public static LanguagesEditorFragment create(@NonNull LanguagesEditorActivity.LanguageEditorMode mode){
        LanguagesEditorFragment fragment = new LanguagesEditorFragment();
        fragment.setArguments(BundleHelper.builder().put("mode", mode).build());
        return fragment;
    }

    public static LanguagesEditorFragment createForChoose(@NonNull LanguagesEditorActivity.LanguageEditorMode mode,
            @NonNull ArrayList<TrendingLanguage> selectedLanguages){
        LanguagesEditorFragment fragment = new LanguagesEditorFragment();
        fragment.setArguments(BundleHelper.builder().put("mode", mode)
                .put("selectedLanguages", selectedLanguages).build());
        return fragment;
    }

    private ItemTouchHelper itemTouchHelper;
    @BindView(R.id.search_edit_text) EditText searchEditText;

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setHasOptionsMenu(true);
        setCanLoadMore(false);
        if(LanguagesEditorActivity.LanguageEditorMode.Sort.equals(mPresenter.getMode())){
            ItemTouchHelperCallback callback = new ItemTouchHelperCallback(
                    ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
            itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
            if(PrefUtils.isLanguagesEditorTipAble()){
                showOperationTip(R.string.languages_editor_tip);
                PrefUtils.set(PrefUtils.LANGUAGES_EDITOR_TIP_ABLE, false);
            }
        }
        addVerticalDivider();
        initSearchEditText();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_search;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadLanguages();
    }

    @Override
    protected String getEmptyTip() {
        return null;
    }

    @Override
    public void showLanguages(ArrayList<TrendingLanguage> languages) {
        adapter.setData(languages);
        postNotifyDataSetChanged();
    }

    @Override
    public void notifyItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        if(LanguagesEditorActivity.LanguageEditorMode.Choose.equals(mPresenter.getMode())){
            boolean selected = adapter.getData().get(position).isSelected();
            adapter.getData().get(position).setSelected(!selected);
            adapter.notifyItemChanged(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_commit){
            if(mPresenter.getListSelectedLanguageCount() < 1){
                showWarningToast(getString(R.string.no_choose_language_warning));
            } else {
                mPresenter.saveSelectedLanguages();
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateLanguages(){
        mPresenter.loadLanguages();
    }

    @Override
    public boolean onItemMoved(int fromPosition, int toPosition) {
        TrendingLanguage fromLanguage = adapter.getData().remove(fromPosition);
        adapter.getData().add(toPosition, fromLanguage);
        adapter.notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        TrendingLanguage removedLanguage = mPresenter.removeLanguage(position);
        if(adapter.getData().size() == 0){
            postNotifyDataSetChanged();
        } else {
            adapter.notifyItemRemoved(position);
        }
        String tip = String.format(getString(R.string.language_removed), removedLanguage.getName());
        Snackbar.make(recyclerView, tip, Snackbar.LENGTH_SHORT)
                .setAction(R.string.undo, v -> mPresenter.undoRemoveLanguage() )
                .show();
    }

    public ArrayList<TrendingLanguage> getSelectedLanguages(){
        return adapter.getData();
    }

    private void initSearchEditText(){
        if(LanguagesEditorActivity.LanguageEditorMode.Sort.equals(mPresenter.getMode())){
            searchEditText.setVisibility(View.GONE);
        } else {
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mPresenter.searchLanguages(searchEditText.getText().toString());
                }
            });
        }
    }

}
