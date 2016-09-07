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

package com.karumi.rosie.sample.comics.view.viewmodel.mapper;

import com.karumi.rosie.mapper.Mapper;
import com.karumi.rosie.sample.comics.domain.model.Comic;
import com.karumi.rosie.sample.comics.domain.model.ComicSeries;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesDetailsViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicSeriesHeaderDetailViewModel;
import com.karumi.rosie.sample.comics.view.viewmodel.ComicViewModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject1;

public class ComicSeriesToComicSeriesDetailsViewModelMapper
    extends Mapper<ComicSeries, ComicSeriesDetailsViewModel> {

  @Inject1
  public ComicSeriesToComicSeriesDetailsViewModelMapper() {
  }

  @Override public ComicSeriesDetailsViewModel map(ComicSeries comicSeries) {
    ComicSeriesDetailsViewModel comicSeriesDetailsViewModel = new ComicSeriesDetailsViewModel();

    comicSeriesDetailsViewModel.setTitle(comicSeries.getName());
    List<ComicSeriesDetailViewModel> comicSeriesDetailViewModels = new LinkedList<>();
    comicSeriesDetailViewModels.add(mapComicSeriesToComicSeriesHeaderDetailViewModel(comicSeries));
    comicSeriesDetailViewModels.addAll(mapComicsToComicViewModels(comicSeries.getComics()));
    comicSeriesDetailsViewModel.setComicSeriesDetailViewModels(comicSeriesDetailViewModels);

    return comicSeriesDetailsViewModel;
  }

  @Override public ComicSeries reverseMap(ComicSeriesDetailsViewModel value) {
    throw new UnsupportedOperationException();
  }

  private ComicSeriesHeaderDetailViewModel mapComicSeriesToComicSeriesHeaderDetailViewModel(
      ComicSeries comicSeries) {
    ComicSeriesHeaderDetailViewModel comicSeriesHeaderDetailViewModel =
        new ComicSeriesHeaderDetailViewModel();

    comicSeriesHeaderDetailViewModel.setTitle(
        comicSeries.getName() + " (" + comicSeries.getReleaseYear() + ")");
    comicSeriesHeaderDetailViewModel.setCoverUrl(comicSeries.getCoverUrl());
    comicSeriesHeaderDetailViewModel.setDescription(comicSeries.getDescription());
    comicSeriesHeaderDetailViewModel.setRating(comicSeries.getRating());

    return comicSeriesHeaderDetailViewModel;
  }

  private List<ComicViewModel> mapComicsToComicViewModels(List<Comic> comics) {
    List<ComicViewModel> comicViewModels = new ArrayList<>();

    for (Comic comic : comics) {
      ComicViewModel comicViewModel = new ComicViewModel();
      comicViewModel.setKey(comic.getKey());
      comicViewModel.setTitle(comic.getName());
      comicViewModel.setThumbnailUrl(comic.getThumbnailUrl());
      comicViewModels.add(comicViewModel);
    }

    return comicViewModels;
  }
}
