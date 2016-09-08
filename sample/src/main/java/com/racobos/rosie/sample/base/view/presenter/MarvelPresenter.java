/*
 *  Copyright (C) 2015 Karumi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.racobos.rosie.sample.base.view.presenter;

import com.racobos.rosie.domain.usecase.UseCaseHandler;
import com.racobos.rosie.domain.usecase.error.OnErrorCallback;
import com.racobos.rosie.sample.base.view.error.ConnectionError;
import com.racobos.rosie.view.loading.RosiePresenterWithLoading;

public class MarvelPresenter<T extends MarvelPresenter.View> extends RosiePresenterWithLoading<T> {
  private final OnErrorCallback onErrorCallback = new OnErrorCallback() {
    @Override public boolean onError(Error error) {
      if (error instanceof ConnectionError) {
        getView().showConnectionError();
      } else {
        getView().showGenericError();
      }
      return true;
    }
  };

  public MarvelPresenter(UseCaseHandler useCaseHandler) {
    super(useCaseHandler);
    registerOnErrorCallback(onErrorCallback);
  }

  public interface View extends RosiePresenterWithLoading.View {
    void showGenericError();

    void showConnectionError();
  }
}
