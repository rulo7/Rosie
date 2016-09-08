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

package com.racobos.rosie.sample.main.view.activity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import com.karumi.marvelapiclient.MarvelApiException;
import com.racobos.rosie.repository.PaginatedCollection;
import com.racobos.rosie.repository.datasource.paginated.Page;
import com.racobos.rosie.repository.policy.ReadPolicy;
import com.racobos.rosie.sample.InjectedInstrumentationTest;
import com.racobos.rosie.sample.R;
import com.racobos.rosie.sample.characters.domain.model.Character;
import com.racobos.rosie.sample.characters.repository.CharactersRepository;
import com.racobos.rosie.sample.characters.view.activity.CharacterDetailsActivity;
import com.racobos.rosie.sample.characters.view.fragment.CharactersFragment;
import com.racobos.rosie.sample.comics.domain.model.Comic;
import com.racobos.rosie.sample.comics.domain.model.ComicSeries;
import com.racobos.rosie.sample.comics.repository.ComicSeriesRepository;
import com.racobos.rosie.sample.comics.view.activity.ComicSeriesDetailsActivity;
import com.racobos.rosie.sample.comics.view.fragment.ComicSeriesFragment;
import com.racobos.rosie.sample.idlingresources.ViewPagerIdlingResource;
import com.racobos.rosie.sample.main.domain.usecase.GetMarvelSettings;
import com.racobos.rosie.sample.recyclerview.RecyclerViewInteraction;
import dagger.Module1;
import dagger.Provides1;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton1;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.racobos.rosie.sample.AncestorMatcher.withAncestor;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest extends InjectedInstrumentationTest {

  private static final int ANY_NUMBER_OF_CHARACTERS = 10;
  private static final int ANY_NUMBER_OF_COMIC_SERIES = 10;
  private static final String ANY_EXCEPTION = "AnyException";
  @Rule
  public IntentsTestRule<MainActivity> activityRule = new IntentsTestRule<>(MainActivity.class, true, false);
  @Inject
  CharactersRepository charactersRepository;
  @Inject
  ComicSeriesRepository comicSeriesRepository;
  @Inject
  GetMarvelSettings getMarvelSettings;

  @Test
  public void shouldShowFakeDataBarWhenMarvelKeysHasNotBeenProvided() throws Exception {
    givenFakeDataIsEnable();
    givenEmptyCharacters();
    givenEmptyComicSeries();
    startActivity();
    onView(withId(R.id.tv_disclaimer)).check(matches(isDisplayed()));
  }

  @Test
  public void shouldShowErrorIfSomethingWrongHappens() throws Exception {
    givenExceptionObtainingCharacters();
    givenEmptyComicSeries();
    startActivity();
    onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("¯\\_(ツ)_/¯"))).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
  }

  @Test
  public void shouldShowConnectionErrorIfHaveConnectionTroubles() throws Exception {
    givenConnectionExceptionObtainingCharacters();
    givenEmptyComicSeries();
    startActivity();
    onView(allOf(withId(android.support.design.R.id.snackbar_text),
            withText("Connection troubles. Ask to Ironman!"))).check(
            matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
  }

  @Test
  public void shouldHideLoadingWhenDataIsLoaded() throws Exception {
    givenEmptyCharacters();
    givenEmptyComicSeries();
    startActivity();
    onView(allOf(withId(R.id.loading), withAncestor(withId(R.id.fragment_characters)))).check(
            matches(not(isDisplayed())));
  }

  @Test
  public void shouldShowCharacterNameIfThereAreCharacters() throws Exception {
    List<Character> superHeroes = givenThereAreSomeCharacters(ANY_NUMBER_OF_CHARACTERS);
    startActivity();
    RecyclerViewInteraction.<Character>onRecyclerView(withId(R.id.rv_characters)).withItems(superHeroes)
            .check(new RecyclerViewInteraction.ItemViewAssertion<Character>() {
              @Override
              public void check(Character character, View view, NoMatchingViewException e) {
                matches(hasDescendant(withText(character.getName()))).check(view, e);
              }
            });
  }

  @Test
  public void shouldOpenCharacterDetailActivityOnRecyclerViewItemClicked() throws Exception {
    List<Character> characters = givenThereAreSomeCharacters(ANY_NUMBER_OF_CHARACTERS);
    int characterIndex = 0;
    startActivity();
    onView(withId(R.id.rv_characters)).
            perform(RecyclerViewActions.actionOnItemAtPosition(characterIndex, click()));
    Character characterSelected = characters.get(characterIndex);
    intended(hasComponent(CharacterDetailsActivity.class.getCanonicalName()));
    intended(hasExtra("CharacterDetailsActivity.CharacterKey", characterSelected.getKey()));
  }

  @Test
  public void shouldShowComicSeriesIfTheAreComicSeriesAndTabIsShown() throws Exception {
    givenThereAreSomeCharacters(ANY_NUMBER_OF_CHARACTERS);
    List<ComicSeries> comicSeries = givenThereAreSomeComicSeries(ANY_NUMBER_OF_COMIC_SERIES);
    startActivity();
    onView(withId(R.id.vp_main)).perform(swipeLeft());
    RecyclerViewInteraction.<ComicSeries>onRecyclerView(withId(R.id.rv_comics)).withItems(comicSeries)
            .check(new RecyclerViewInteraction.ItemViewAssertion<ComicSeries>() {
              @Override
              public void check(ComicSeries comic, View view, NoMatchingViewException e) {
                String textMatch = String.format("%1$s (%2$s)", comic.getName(), comic.getReleaseYear());
                matches(hasDescendant(withText(textMatch))).check(view, e);
              }
            });
  }

  @Test
  public void shouldOpenComicSeriesDetailActivityOnRecyclerViewItemClicked() throws Exception {
    givenThereAreSomeCharacters(ANY_NUMBER_OF_CHARACTERS);
    List<ComicSeries> comicSeries = givenThereAreSomeComicSeries(ANY_NUMBER_OF_COMIC_SERIES);
    givenAnyComicSeriesDetail();
    int comicSeriesIndex = 0;
    Activity activity = startActivity();
    registerIdlingResources(new ViewPagerIdlingResource((ViewPager) activity.findViewById(R.id.vp_main)));
    onView(withId(R.id.vp_main)).perform(swipeLeft());
    onView(withId(R.id.rv_comics)).
            perform(RecyclerViewActions.actionOnItemAtPosition(comicSeriesIndex, click()));
    ComicSeries comicSeriesSelected = comicSeries.get(comicSeriesIndex);
    intended(hasComponent(ComicSeriesDetailsActivity.class.getCanonicalName()));
    intended(hasExtra("ComicSeriesDetailsActivity.ComicSeriesKey", comicSeriesSelected.getKey().intValue()));
  }

  private void givenAnyComicSeriesDetail() throws Exception {
    ComicSeries comicSeries = new ComicSeries();
    comicSeries.setKey(0);
    comicSeries.setDescription("desc - " + 0);
    comicSeries.setName("name - " + 0);
    comicSeries.setComplete(true);
    comicSeries.setComics(new ArrayList<Comic>());
    comicSeries.setCoverUrl("https://i.annihil.us/u/prod/marvel/i/mg/c/60/55b6a28ef24fa.jpg");
    when(comicSeriesRepository.getComicSeriesDetail(any(Integer.class))).thenReturn(comicSeries);
  }

  private void givenFakeDataIsEnable() {
    when(getMarvelSettings.haveKeys()).thenReturn(false);
  }

  private void givenEmptyCharacters() throws Exception {
    when(charactersRepository.getAll(ReadPolicy.CACHE_ONLY)).thenReturn(new ArrayList<Character>());
    PaginatedCollection<Character> emptyPage = new PaginatedCollection<>();
    emptyPage.setPage(Page.withOffsetAndLimit(0, 0));
    emptyPage.setHasMore(false);
    when(charactersRepository.getPage(any(Page.class))).thenReturn(emptyPage);
  }

  private void givenExceptionObtainingCharacters() throws Exception {
    when(charactersRepository.getAll(ReadPolicy.CACHE_ONLY)).thenReturn(new ArrayList<Character>());
    when(charactersRepository.getPage(any(Page.class))).thenThrow(new MarvelApiException(ANY_EXCEPTION, null));
  }

  private void givenConnectionExceptionObtainingCharacters() throws Exception {
    when(charactersRepository.getAll(ReadPolicy.CACHE_ONLY)).thenReturn(new ArrayList<Character>());
    when(charactersRepository.getPage(any(Page.class))).thenThrow(
            new MarvelApiException(ANY_EXCEPTION, new UnknownHostException()));
  }

  private void givenEmptyComicSeries() throws Exception {
    when(comicSeriesRepository.getAll(ReadPolicy.CACHE_ONLY)).thenReturn(new ArrayList<ComicSeries>());
    PaginatedCollection<ComicSeries> emptyPage = new PaginatedCollection<>();
    emptyPage.setPage(Page.withOffsetAndLimit(0, 0));
    emptyPage.setHasMore(false);
    when(comicSeriesRepository.getPage(any(Page.class))).thenReturn(emptyPage);
  }

  private List<Character> givenThereAreSomeCharacters(int numberOfCharacters) throws Exception {
    List<Character> characters = new LinkedList<>();
    for (int i = 0; i < numberOfCharacters; i++) {
      Character character = getCharacter(i);
      characters.add(character);
      when(charactersRepository.getByKey(String.valueOf(i))).thenReturn(character);
    }
    PaginatedCollection<Character> paginatedCollection = new PaginatedCollection<>(characters);
    paginatedCollection.setPage(Page.withOffsetAndLimit(0, numberOfCharacters));
    paginatedCollection.setHasMore(false);
    when(charactersRepository.getPage(any(Page.class))).thenReturn(paginatedCollection);
    return characters;
  }

  private List<ComicSeries> givenThereAreSomeComicSeries(int numberOfComicSeries) throws Exception {
    List<ComicSeries> comics = new LinkedList<>();
    for (int i = 0; i < numberOfComicSeries; i++) {
      ComicSeries comic = getComicSeries(i);
      comics.add(comic);
    }
    PaginatedCollection<ComicSeries> paginatedCollection = new PaginatedCollection<>(comics);
    paginatedCollection.setPage(Page.withOffsetAndLimit(0, numberOfComicSeries));
    paginatedCollection.setHasMore(false);
    when(comicSeriesRepository.getPage(any(Page.class))).thenReturn(paginatedCollection);
    return comics;
  }

  @NonNull
  private Character getCharacter(int id) {
    Character character = new Character();
    character.setKey("" + id);
    character.setName("SuperHero - " + id);
    character.setDescription("Description Super Hero - " + id);
    character.setThumbnailUrl("https://id.annihil.us/u/prod/marvel/id/mg/c/60/55b6a28ef24fa.jpg");
    return character;
  }

  @NonNull
  private ComicSeries getComicSeries(int id) {
    ComicSeries comicSeries = new ComicSeries();
    comicSeries.setKey(id);
    comicSeries.setName("ComicSeries - " + id);
    comicSeries.setDescription("Description Comic Serie - " + id);
    return comicSeries;
  }

  private MainActivity startActivity() {
    return activityRule.launchActivity(null);
  }

  @Override
  public List<Object> getTestModules() {
    return Arrays.asList((Object) new TestModule());
  }

  @Module1(overrides = true, library = true, complete = false,
          injects = {
                  MainActivity.class, CharactersFragment.class, ComicSeriesFragment.class, MainActivityTest.class,
                  CharacterDetailsActivity.class
          })
  class TestModule {

    @Provides1
    @Singleton1
    public CharactersRepository provideCharactersRepository() {
      return mock(CharactersRepository.class);
    }

    @Provides1
    @Singleton1
    public ComicSeriesRepository provideComicSeriesRepository() {
      return mock(ComicSeriesRepository.class);
    }

    @Provides1
    @Singleton1
    public GetMarvelSettings provideGetMarvelSettings() {
      return mock(GetMarvelSettings.class);
    }
  }
}