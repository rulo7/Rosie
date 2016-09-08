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

package com.racobos.rosie.sample.main;

import com.racobos.rosie.domain.usecase.error.ErrorHandler;
import com.racobos.rosie.repository.datasource.paginated.InMemoryPaginatedCacheDataSource;
import com.racobos.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.racobos.rosie.sample.base.view.error.MarvelErrorFactory;
import com.racobos.rosie.sample.characters.domain.model.Character;
import com.racobos.rosie.sample.comics.domain.model.ComicSeries;
import com.racobos.rosie.sample.main.domain.usecase.GetMarvelSettings;
import com.racobos.rosie.time.TimeProvider;
import dagger.Module1;
import dagger.Provides1;
import javax.inject.Singleton1;

import static java.util.concurrent.TimeUnit.MINUTES;

@Module1(library = true,
    complete = false,
    injects = {
        MainApplication.class
    }) public class ApplicationModule {

  private static final long CHARACTERS_IN_MEMORY_CACHE_TTL = MINUTES.toMillis(5);
  private static final long COMICS_IN_MEMORY_CACHE_TTL = MINUTES.toMillis(5);

  @Provides1
  @Singleton1
  public PaginatedCacheDataSource<String, Character> provideCharactersPageInMemoryCache() {
    return new InMemoryPaginatedCacheDataSource<>(new TimeProvider(),
        CHARACTERS_IN_MEMORY_CACHE_TTL);
  }

  @Provides1
  @Singleton1
  public PaginatedCacheDataSource<Integer, ComicSeries> provideComicsPageInMemoryCache() {
    return new InMemoryPaginatedCacheDataSource<>(new TimeProvider(), COMICS_IN_MEMORY_CACHE_TTL);
  }

  @Provides1
  public ErrorHandler providesErrorHandler(MarvelErrorFactory errorFactory) {
    return new ErrorHandler(errorFactory);
  }

  @Provides1
  @Singleton1
  public GetMarvelSettings provideGetMarvelSettings() {
    return new GetMarvelSettings();
  }

}
