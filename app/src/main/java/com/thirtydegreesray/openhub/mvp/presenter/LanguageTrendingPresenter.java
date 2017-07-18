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

package com.thirtydegreesray.openhub.mvp.presenter;

import com.thirtydegreesray.openhub.db.DaoSession;
import com.thirtydegreesray.openhub.mvp.contract.ILanguageTrendingContract;
import com.thirtydegreesray.openhub.mvp.model.Repository;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class LanguageTrendingPresenter extends ILanguageTrendingContract.Presenter {

    @Inject
    public LanguageTrendingPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void loadRepositories(String language) {
        ArrayList<Repository> list = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            Repository repository = new Repository();
            repository.setName(language + "-" + i);
            list.add(repository);
        }
        mView.showRepositories(list);
    }


}
