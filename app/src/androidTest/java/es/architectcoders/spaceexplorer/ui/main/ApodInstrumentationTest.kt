package es.architectcoders.spaceexplorer.ui.main

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.AccessibilityChecks
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.architectcoders.data.datasource.ApodRemoteDataSource
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeApodEntity
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeApodEntity2
import es.architectcoders.spaceexplorer.data.server.MockWebServerRule
import es.architectcoders.spaceexplorer.framework.database.apodDb.ApodDao
import es.architectcoders.spaceexplorer.ui.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class ApodInstrumentationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)//con el this le pasamos la clase

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var apodDao: ApodDao

    @Inject
    lateinit var remoteDataSourceApod: ApodRemoteDataSource

    @Before
    fun setUp(){
        AccessibilityChecks.enable().setRunChecksFromRootView(true)
        hiltRule.inject()
    }

    @Test
    fun check_mock_server_is_working() = runTest {
        val apod = remoteDataSourceApod.getApod()
        apod.fold({ throw Exception(it.toString()) }) {
            val apod = it
            assertEquals("Stars and Dust in the Pacman Nebula", apod?.title)
        }
    }

    @Test
    fun check_insert_apod_in_db() = runTest {
        // Given
        val apod = defaultFakeApodEntity

        // When
        apodDao.insertApod(apod)
        val result = apodDao.apodsCount()

        // Then
        assertEquals(1, result)
    }
    @Test
    fun check_find_apod_by_id() = runTest {
        // Given
        val apod = defaultFakeApodEntity
        apodDao.insertApod(apod)

        // When
        val result = apodDao.findApodById(1)

        // Then
        assertEquals(apod, result)
    }
    @Test
    fun check_find_apod_by_date() = runTest {
        // Given
        val apod = defaultFakeApodEntity
        apodDao.insertApod(apod)

        // When
        val result = apodDao.findApodByDate("2023-01-01")

        // Then
        assertEquals(apod, result)
    }

    @Test
    fun check_apods_count_in_db() = runTest {
        // Given
        val apod1 = defaultFakeApodEntity
        val apod2 = defaultFakeApodEntity2
        apodDao.insertApod(apod1)
        apodDao.insertApod(apod2)

        // When
        val result = apodDao.apodsCount()

        // Then
        assertEquals(2, result)
    }

    @Test
    fun check_update_apod_in_db() = runTest {
        // Given
        val apod = defaultFakeApodEntity
        apodDao.insertApod(apod)

        // When
        val updatedApod = apod.copy(title = "Updated Title")
        apodDao.updateApod(updatedApod)
        val result = apodDao.findApodById(1)

        // Then
        assertEquals("Updated Title", result.title)
    }

    @Test
    fun check_get_all_apods() = runTest {
        // Given
        val apod1 = defaultFakeApodEntity
        val apod2 = defaultFakeApodEntity2
        apodDao.insertApod(apod1)
        apodDao.insertApod(apod2)

        // When
        val result = apodDao.getAllApods().first()

        // Then
        assertEquals(listOf(apod1, apod2), result)
    }
    inline fun <reified T : Any> ViewInteraction.getTag(): T? {
        var tag: T? = null
        perform(object : ViewAction {
            override fun getConstraints() = ViewMatchers.isAssignableFrom(View::class.java)

            override fun getDescription() = "Get tag from View"

            override fun perform(uiController: UiController, view: View) {
                when (val viewTag = view.tag) {
                    is T -> tag = viewTag
                    else -> error("The tag cannot be casted to the given type!")
                }
            }
        })
        return tag
    }

    @Test
    fun when_click_in_favorite_the_icon_change() {
        // Espera hasta que la UI termine de cargar
        onView(isRoot()).perform(waitFor(5000))

        onView(withId(R.id.cvApod))
            .check(matches(isDisplayed()))

        onView(withId(R.id.ivApodFav))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.ivApodFav))
            .check(matches(isSelected()))
    }

    @Test
    fun testBottomNavigation() {
        // Verifica que la BottomNavigationView esté visible
        onView(withChild(withId(R.id.bottom_navigation) ))
            .check(matches(isDisplayed()))

        // Verifica que el primer item esté seleccionado al iniciar
        onView(withId(R.id.homeFragment))
            .check(matches(isDisplayed()))

        // Navega al segundo item ("Rovers")
        onView(withId(R.id.roversFragment))
            .perform(click())

        // Verifica que el segundo item esté seleccionado
        onView(withId(R.id.roversFragment))
            .check(matches(isDisplayed()))

        // Navega al tercer item ("Notifications")
        onView(withId(R.id.marsFragment))
            .perform(click())

        // Verifica que el tercer item esté seleccionado
        onView(withId(R.id.marsFragment))
            .check(matches(isDisplayed()))
    }
}

@RunWith(AndroidJUnit4::class)
@LargeTest
class MyWelcomeWorkflowIntegrationTest {
    init {
        AccessibilityChecks.enable()
    }
}