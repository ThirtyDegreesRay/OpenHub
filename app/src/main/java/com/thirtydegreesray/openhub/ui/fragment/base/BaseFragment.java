

package com.thirtydegreesray.openhub.ui.fragment.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.thirtydegreesray.dataautoaccess.DataAutoAccess;
import com.thirtydegreesray.openhub.AppApplication;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.ui.activity.LoginActivity;
import com.thirtydegreesray.openhub.util.AppUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public abstract class BaseFragment<P extends IBaseContract.Presenter>
        extends Fragment implements IBaseContract.View {

    private final String TAG = BaseFragment.class.getSimpleName();

    @Inject
    protected P mPresenter;
    private ProgressDialog mProgressDialog;
    Unbinder unbinder;

    /**
     * fragment for viewpager
     */
    private boolean isPagerFragment = false;
    private boolean isFragmentShowed = false;

    public BaseFragment() {

    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void setupFragmentComponent(AppComponent appComponent);

    protected abstract void initFragment(Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //some page contain WebView will make default language changed
        AppUtils.updateAppLanguage(getActivity());
        View fragmentView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        initFragment(savedInstanceState);
        if (mPresenter != null) mPresenter.onViewInitialized();
        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFragmentComponent(getAppComponent());
        if (mPresenter != null) mPresenter.attachView(this);
        DataAutoAccess.getData(this, savedInstanceState);
        DataAutoAccess.getData(this, getArguments());
        if (mPresenter != null) mPresenter.onRestoreInstanceState(getArguments());
        if (mPresenter != null) mPresenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DataAutoAccess.saveData(this, outState);
        if (mPresenter != null) mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mPresenter != null) mPresenter.detachView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.with(this).onLowMemory();
    }

    public void onFragmentShowed() {
        isFragmentShowed = true;
    }

    public void onFragmentHided() {
        isFragmentShowed = false;
    }

    protected AppApplication getAppApplication() {
        return AppApplication.get();
    }

    protected AppComponent getAppComponent() {
        return getAppApplication().getAppComponent();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showProgressDialog(String content) {
        getProgressDialog(content);
        mProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        } else {
            throw new NullPointerException("dismissProgressDialog: can't dismiss a null dialog, must showForRepo dialog first!");
        }
    }

    @Override
    public ProgressDialog getProgressDialog(String content) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(content);
        return mProgressDialog;
    }

    @Override
    public void showToast(String message) {
        Toasty.normal(getActivity(), message).show();
    }

    @Override
    public void showInfoToast(String message) {
        Toasty.info(getActivity(), message).show();
    }

    @Override
    public void showSuccessToast(String message) {
        Toasty.success(getActivity(), message).show();
    }

    @Override
    public void showErrorToast(String message) {
        Toasty.error(getActivity(), message).show();
    }

    @Override
    public void showWarningToast(String message) {
        Toasty.warning(getActivity(), message).show();
    }

    @Override
    public void showTipDialog(String content) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle("提示")
                .setMessage(content)
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public void showConfirmDialog(String msn, String title, String confirmText
            , DialogInterface.OnClickListener confirmListener) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(title)
                .setMessage(msn)
                .setCancelable(true)
                .setPositiveButton(confirmText, confirmListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public boolean isPagerFragment() {
        return isPagerFragment;
    }

    public BaseFragment setPagerFragment(boolean flag) {
        isPagerFragment = flag;
        return this;
    }

    public boolean isFragmentShowed() {
        return isFragmentShowed;
    }

    @Override
    public void showLoginPage() {
        getActivity().finishAffinity();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void scrollToTop() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected final void showOperationTip(@StringRes int msgResId) {
        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView(), msgResId, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, v -> {

                    });
            snackbar.show();
        }
    }

}
