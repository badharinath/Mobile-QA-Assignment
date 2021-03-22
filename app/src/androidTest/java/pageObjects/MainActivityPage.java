package pageObjects;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import java.util.ArrayList;
import java.util.List;

import app.com.mobileassignment.R;
import app.com.mobileassignment.model.City;
import app.com.mobileassignment.views.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.azimolabs.conditionwatcher.Instruction;

public class MainActivityPage {
    private ViewInteraction searchInputField;

    public MainActivityPage() {
        searchInputField = onView(withId(R.id.search));
    }

    // Wait for the application to be loaded
    public MainActivityPage loadAppPage(ActivityTestRule<MainActivity> rule) {
        waitForView(rule.getActivity().findViewById(R.id.progress_bar), View.GONE);
        return this;
    }

    // Verify app header
    public ViewInteraction verifyAppHeader(String header){
        return onView(withText(header)).check(matches(isDisplayed()));
    }

    //Verify search input field
    public void verifySearchInputField() {
        onView(withId(R.id.search)).check(matches(isDisplayed()));
    }

    //Verify initial list of cities
    public void verifyListOfCities() {
        onView(withId(R.id.citiesList)).check(matches(isDisplayed()));
    }

    // Click on Search text field and enter user input
    public MainActivityPage inputSearch(String text) {
        for (int i = 0; i < text.length(); i++) {
            searchInputField.perform(typeText(String.valueOf(text.charAt(i))));
        }
        return this;
    }

    // Empty Search input text field
    public MainActivityPage emptySearchField() {
        searchInputField.perform(clearText());
        return this;
    }

    // Wait for search results to be visible
    public MainActivityPage waitForSearchResults(ActivityTestRule<MainActivity> rule) {
        waitForView(rule.getActivity().findViewById(R.id.results), View.VISIBLE);
        return this;
    }

    // Retrieve list of cities
    public List<City> getCities(ActivityTestRule<MainActivity> rule) {
        ListAdapter citiesListAdapter = ((ListView) rule.getActivity().findViewById(R.id.citiesList)).getAdapter();
        List<City> citiesList = new ArrayList<>();
        for (int i = 0; i < citiesListAdapter.getCount(); i++) {
            citiesList.add((City) citiesListAdapter.getItem(i));
        }
        return citiesList;
    }

    //Scroll the view down. Returns true if it is successfully scrolled down
    public boolean scrollDown() {
        onView(withId(R.id.results)).perform(swipeUp());
        return true;
    }

    //Scroll the view up. Returns true if it is successfully scrolled up
    public boolean scrollUp() {
        onView(withId(R.id.results)).perform(swipeDown());
        return true;
    }

    private static void waitForView(final View view, final int condition) {
        try {
            ConditionWatcher.waitForCondition(new Instruction() {
                @Override
                public String getDescription() {
                    return String.format("Wait for specific view with id %s , until %s", view.toString(), condition);
                }

                @Override
                public boolean checkCondition() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return view.getVisibility() == condition;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}