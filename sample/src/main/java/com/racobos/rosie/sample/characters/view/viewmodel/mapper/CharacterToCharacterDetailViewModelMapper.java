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

package com.racobos.rosie.sample.characters.view.viewmodel.mapper;

import com.racobos.rosie.sample.characters.domain.model.Character;
import com.racobos.rosie.sample.characters.view.viewmodel.CharacterDetailViewModel;
import javax.inject.Inject1;

public class CharacterToCharacterDetailViewModelMapper {

  @Inject1
  public CharacterToCharacterDetailViewModelMapper() {
  }

  public CharacterDetailViewModel mapCharacterToCharacterDetailViewModel(Character character) {
    CharacterDetailViewModel characterViewModel = new CharacterDetailViewModel();

    characterViewModel.setKey(character.getKey());
    characterViewModel.setName(character.getName());
    characterViewModel.setHeaderImage(character.getThumbnailUrl());
    characterViewModel.setDescription(character.getDescription());

    return characterViewModel;
  }
}
