package tests;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.com.mobileassignment.views.MainActivity;
import pageObjects.MainActivityPage;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4ClassRunner.class)
public class MainActivityTest {

    MainActivityPage mainActivityPage = new MainActivityPage();

    public static final String HEADER = "Mobile Assignment";
    private static final String[] VALID_SEARCH_INPUT = { "Colorado Springs, US", "coruna", "A Coruna, ES", "665", "'", "hol"};

    @Rule
    public ActivityTestRule<MainActivity> mainRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void loadApp() {
        mainActivityPage.loadAppPage(mainRule);
    }

    @Test
    public void openApp() {
        mainActivityPage.verifyAppHeader(HEADER);
        mainActivityPage.verifySearchInputField();
        mainActivityPage.verifyListOfCities();
    }

    @Test
    public void verifySearchResultForInvalidCity() {
        String invalidCity = "invalid city";
        mainActivityPage.inputSearch(invalidCity).waitForSearchResults(mainRule);
        assertTrue(String.format("No result available for %s", invalidCity), mainActivityPage.getCities(mainRule).isEmpty());
    }

    @Test
    public void verifySearchResultForExactMatch() {
        mainActivityPage.inputSearch(VALID_SEARCH_INPUT[0]).waitForSearchResults(mainRule);
        assertEquals(mainActivityPage.getCities(mainRule), VALID_SEARCH_INPUT[0]);
    }

    @Test
    public void verifySearchResultForSimilarMatch() {
        mainActivityPage.inputSearch(VALID_SEARCH_INPUT[1]).waitForSearchResults(mainRule);
        assertFalse(String.format("Search results does not contain a similar match like %s", VALID_SEARCH_INPUT[2]), mainActivityPage.getCities(mainRule).contains(VALID_SEARCH_INPUT[2]));
    }

    @Test
    public void verifySearchResultForSpace() {
        String inputString = " " ;
        mainActivityPage.inputSearch(inputString).waitForSearchResults(mainRule);
        assertTrue(String.format("No result available for a space"), mainActivityPage.getCities(mainRule).isEmpty());
    }

    @Test
    public void verifySearchResultForValidNumericAndSpecialCharacters() {
        mainActivityPage.inputSearch(VALID_SEARCH_INPUT[3]).waitForSearchResults(mainRule);
        assertFalse(String.format("Search results are available for valid numeric character(s)"), mainActivityPage.getCities(mainRule).isEmpty());

        mainActivityPage.emptySearchField();

        mainActivityPage.inputSearch(VALID_SEARCH_INPUT[4]).waitForSearchResults(mainRule);
        assertFalse(String.format("Search results are available for valid special character(s)"), mainActivityPage.getCities(mainRule).isEmpty());
    }

    @Test
    public void verifySearchResultForInvalidNumericAndSpecialCharacters() {
        String inputString = "7745" ;
        mainActivityPage.inputSearch(inputString).waitForSearchResults(mainRule);
        assertTrue(String.format("No result available for invalid numeric character(s)"), mainActivityPage.getCities(mainRule).isEmpty());

        mainActivityPage.emptySearchField();

        inputString = "$$";
        mainActivityPage.inputSearch(inputString).waitForSearchResults(mainRule);
        assertTrue(String.format("No result available for invalid special character(s)"), mainActivityPage.getCities(mainRule).isEmpty());
    }

    @Test
    public void VerifySearchResultIsScrollable() {
        assertTrue(String.format("Scroll down action is performed"), mainActivityPage.scrollDown());
        assertTrue(String.format("Scroll up action is performed"), mainActivityPage.scrollUp());

        mainActivityPage.inputSearch(VALID_SEARCH_INPUT[5]).waitForSearchResults(mainRule);
        assertTrue(String.format("Scroll down action is performed on the results for input %s", VALID_SEARCH_INPUT[5]), mainActivityPage.scrollDown());
        assertTrue(String.format("Scroll up action is performed on the results for input %s", VALID_SEARCH_INPUT[5]), mainActivityPage.scrollUp());
    }
}