/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.racobos.rosie.domain.usecase.callback;

import android.os.Handler;
import android.os.Looper;

/**
 * CallbackScheduler implementation used to notify callbacks on the Android Main Thread.
 */
public class MainThreadCallbackScheduler implements CallbackScheduler {

  private final Handler uiHandler;

  public MainThreadCallbackScheduler() {
    this.uiHandler = new Handler(Looper.getMainLooper());
  }

  @Override public void post(Runnable runnable) {
    uiHandler.post(runnable);
  }
}
