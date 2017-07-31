/*
 *    Copyright 2017 ThirtyDegressRay
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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.inject.component.DaggerActivityComponent;
import com.thirtydegreesray.openhub.mvp.contract.ILoginContract;
import com.thirtydegreesray.openhub.mvp.presenter.LoginPresenter;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.util.HttpUtil;
import com.thirtydegreesray.openhub.util.StringUtil;
import com.unstoppable.submitbuttonview.SubmitButton;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 2017/7/12.
 *
 * @author ThirtyDegreesRay
 */

public class LoginActivity extends BaseActivity<LoginPresenter>
        implements ILoginContract.View {

    private final String TAG = "LoginActivity";
    @BindView(R.id.user_name_et) TextInputEditText userNameEt;
    @BindView(R.id.user_name_layout) TextInputLayout userNameLayout;
    @BindView(R.id.password_et) TextInputEditText passwordEt;
    @BindView(R.id.password_layout) TextInputLayout passwordLayout;
    @BindView(R.id.login_bn) SubmitButton loginBn;

    private String userName;
    private String password;

    private Handler handler;

    @Override
    public void onGetTokenSuccess(String token, String scope, int expireIn) {
        Intent intent = new Intent();
        intent.putExtra("accessToken", token);
        intent.putExtra("scope", scope);
        intent.putExtra("expireIn", expireIn);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 依赖注入的入口
     *
     * @param appComponent appComponent
     */
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    /**
     * 获取ContentView id
     *
     * @return
     */
    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    /**
     * 初始化activity
     */
    @Override
    protected void initActivity() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String url = (String) msg.obj;
                Map<String, String> params = HttpUtil.getParams(url);
                String code = params.get("code");
                String state = params.get("state");
                mPresenter.getToken(code, state);
            }
        };
    }

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {

        loginBn.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
            @Override
            public void onResultEnd() {

            }
        });

//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(@NonNull WebView view, @NonNull String url) {
//                if (url.startsWith(AppConfig.OAUTH2_CALLBACK_URL)) {
//                    Message message = new Message();
//                    message.obj = url;
//                    handler.sendMessage(message);
//                } else {
//                    view.loadUrl(url);
//                }
//                Log.i(TAG, "shouldOverrideUrlLoading:" + url);
//                return true;
//            }
//        });
//        webView.loadUrl(mPresenter.getOAuth2Url());
    }

    @OnClick({R.id.login_bn, R.id.oauth_login_bn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_bn:
                if(loginCheck()){
                    loginBn.reset();
                }else{
                    loginBn.reset();
                }
                break;
            case R.id.oauth_login_bn:
                break;
        }
    }

    private boolean loginCheck(){
        boolean valid = true;
        userName = userNameEt.getText().toString();
        password = passwordEt.getText().toString();
        if(StringUtil.isBlank(userName)){
            valid = false;
            userNameLayout.setError(getString(R.string.user_name_warning));
        }else{
            userNameLayout.setErrorEnabled(false);
        }
        if(StringUtil.isBlank(password)){
            valid = false;
            passwordLayout.setError(getString(R.string.password_warning));
        }else{
            passwordLayout.setErrorEnabled(false);
        }
        return valid;
    }
}
