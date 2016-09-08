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

package com.racobos.rosie.module;

import android.app.Application;
import android.content.Context;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.racobos.rosie.application.RosieApplication;
import com.racobos.rosie.daggerutils.ForApplication;
import com.racobos.rosie.domain.usecase.TaskScheduler;
import com.racobos.rosie.domain.usecase.UseCaseHandler;
import com.racobos.rosie.domain.usecase.error.ErrorHandler;
import com.racobos.rosie.domain.usecase.jobqueue.TaskSchedulerJobQueue;
import dagger.Module1;
import dagger.Provides1;
import javax.inject.Singleton1;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to be created.
 */
@Module1(library = true, injects = RosieApplication.class)
public class RosieAndroidModule {

  private static final int MIN_CONSUMER_COUNT = 1;
  private static final int MAX_CONSUMER_COUNT = 5;
  private static final int LOAD_FACTOR = 1;

  private final Context context;

  public RosieAndroidModule(Application application) {
    validateApplication(application);
    this.context = application;
  }

  /**
   * Allow the application context to be injected but requires it to be annotated with
   * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
   */
  @Provides1
  @ForApplication
  Context provideApplicationContext() {
    return context;
  }

  @Provides1
  @Singleton1
  public UseCaseHandler provideUseCaseHandler(TaskScheduler taskScheduler,
      ErrorHandler errorHandler) {
    return new UseCaseHandler(taskScheduler, errorHandler);
  }

  @Provides1
  @Singleton1
  public TaskScheduler provideTaskScheduler(JobManager jobManager) {
    return new TaskSchedulerJobQueue(jobManager);
  }

  @Provides1
  @Singleton1
  public JobManager provideJobManager(@ForApplication Context context) {
    Configuration config = new Configuration.Builder(context).minConsumerCount(MIN_CONSUMER_COUNT)
        .maxConsumerCount(MAX_CONSUMER_COUNT)
        .loadFactor(LOAD_FACTOR)
        .build();
    return new JobManager(context, config);
  }

  private void validateApplication(Application application) {
    if (application == null) {
      throw new IllegalArgumentException("The Application passed in construction can't be null.");
    }
  }
}
