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

package com.racobos.rosie.sample.comics.repository.datasource;

import com.racobos.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.racobos.rosie.sample.comics.domain.model.Comic;
import com.racobos.rosie.sample.comics.domain.model.ComicSeries;
import java.util.List;

public interface ComicSeriesDataSource extends PaginatedReadableDataSource<Integer, ComicSeries> {
  List<Comic> getComicBySeries(int key) throws Exception;
}
