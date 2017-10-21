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

package com.thirtydegreesray.openhub.ui.activity.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.util.Logger;

/**
 * Created by ThirtyDegreesRay on 2017/10/21 10:03:15
 */

public abstract class SingleFragmentActivity<P extends IBaseContract.Presenter, F extends Fragment>
        extends BaseActivity<P> implements IBaseContract.View{

    private F mFragment;
    private final String FRAGMENT_TAG = "mFragment";

    @Override
    protected int getContentView() {
        return R.layout.activity_single_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        Fragment temp = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if(temp == null){
            mFragment = createFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mFragment, FRAGMENT_TAG)
                    .commit();
            Logger.d("temp fragment is null");
        } else {
            mFragment = (F) temp;
            Logger.d("temp fragment not null");
        }
    }

    protected abstract F createFragment();

    protected F getFragment(){
        return mFragment;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Logger.d("onAttachFragment");
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }
}
