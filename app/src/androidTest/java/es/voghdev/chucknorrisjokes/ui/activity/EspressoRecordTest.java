package es.voghdev.chucknorrisjokes.ui.activity;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.voghdev.chucknorrisjokes.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EspressoRecordTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void espressoRecordTest() {
        ViewInteraction viewPager = onView(
                allOf(withId(R.id.viewPager),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        viewPager.perform(swipeLeft());

        ViewInteraction viewPager2 = onView(
                allOf(withId(R.id.viewPager),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        viewPager2.perform(swipeLeft());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spn_categories),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        withParent(withId(R.id.viewPager))),
                                0),
                        isDisplayed()));
        appCompatSpinner.perform(click());

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(12);
        appCompatTextView.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_search), withText("Search"),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        withParent(withId(R.id.viewPager))),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction viewPager3 = onView(
                allOf(withId(R.id.viewPager),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        viewPager3.perform(swipeRight());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.et_keyword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.til_keyword),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Bruce lee"), closeSoftKeyboard());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_search), withText("Search"),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        withParent(withId(R.id.viewPager))),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        pressBack();

        ViewInteraction viewPager4 = onView(
                allOf(withId(R.id.viewPager),
                        childAtPosition(
                                allOf(withId(R.id.rootView),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        viewPager4.perform(swipeRight());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
