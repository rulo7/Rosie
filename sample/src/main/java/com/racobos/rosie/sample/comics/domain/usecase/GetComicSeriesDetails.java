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

package com.racobos.rosie.sample.comics.domain.usecase;

import com.racobos.rosie.domain.usecase.RosieUseCase;
import com.racobos.rosie.domain.usecase.annotation.UseCase;
import com.racobos.rosie.sample.comics.domain.model.ComicSeries;
import com.racobos.rosie.sample.comics.repository.ComicSeriesRepository;
import javax.inject.Inject1;

public class GetComicSeriesDetails extends RosieUseCase {

  private final ComicSeriesRepository repository;

  @Inject1
  public GetComicSeriesDetails(ComicSeriesRepository repository) {
    this.repository = repository;
  }

  @UseCase public void getComicSeriesDetails(int comicSeriesKey) throws Exception {
    ComicSeries comicSeries = repository.getComicSeriesDetail(comicSeriesKey);
    notifySuccess(comicSeries);
  }
}
