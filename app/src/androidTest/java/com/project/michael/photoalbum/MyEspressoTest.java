package com.project.michael.photoalbum;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MyEspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ensureMakeAlbumWorks() {
        onView(withId(R.id.new_album_button)).perform(click());
        onView(withId(R.id.new_album_text)).perform(typeText("TestAlbum"), closeSoftKeyboard());
        onView(withId(R.id.new_album_okay)).perform(click());
    }

    @Test
    public void ensureDeleteAlbumWorks() {
        onView(withId(R.id.delete_album_button)).perform(click());
        onView(withId(R.id.del_album_text)).perform(typeText("TestAlbum"), closeSoftKeyboard());
        onView(withId(R.id.del_album_okay)).perform(click());
    }

    @Test
    public void ensureSearchWorks() {
        onView(withId(R.id.search_button)).perform(click());
        onView(withId(R.id.timeframe_from_text)).perform(typeText("01/01/2019"), closeSoftKeyboard());
        // Type text into time until text box
        onView(withId(R.id.timeframe_to_text)).perform(typeText("28/02/2019"), closeSoftKeyboard());

        onView(withId(R.id.location_from_text)).perform(typeText("50.0,-150.0"), closeSoftKeyboard());

        onView(withId(R.id.location_to_text)).perform(typeText("40.0,150.0"), closeSoftKeyboard());

        // Click on Search button to start search
        onView(withId(R.id.search_button)).perform(click());
    }
}
