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

package com.racobos.rosie.sample.characters.repository;

import com.racobos.rosie.repository.PaginatedRosieRepository;
import com.racobos.rosie.repository.datasource.paginated.PaginatedCacheDataSource;
import com.racobos.rosie.repository.datasource.paginated.PaginatedReadableDataSource;
import com.racobos.rosie.sample.characters.domain.model.Character;
import javax.inject.Inject1;

public class CharactersRepository extends PaginatedRosieRepository<String, Character> {

  @Inject1
  public CharactersRepository(CharacterDataSourceFactory characterDataSourceFactory,
      PaginatedCacheDataSource<String, Character> inMemoryPaginatedCache) {
    addCacheDataSources(inMemoryPaginatedCache);
    addPaginatedCacheDataSources(inMemoryPaginatedCache);

    PaginatedReadableDataSource<String, Character> characterDataSource =
        characterDataSourceFactory.createDataSource();
    addReadableDataSources(characterDataSource);
    addPaginatedReadableDataSources(characterDataSource);
  }
}
