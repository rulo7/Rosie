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

import com.racobos.rosie.sample.characters.view.fragment.CharactersFragment;
import com.racobos.rosie.sample.comics.view.fragment.ComicSeriesFragment;
import com.racobos.rosie.sample.main.view.activity.MainActivity;
import dagger.Module1;

@Module1(library = true,
    complete = false,
    injects = {
        MainActivity.class, CharactersFragment.class, ComicSeriesFragment.class
    }) public class MainModule {
}
