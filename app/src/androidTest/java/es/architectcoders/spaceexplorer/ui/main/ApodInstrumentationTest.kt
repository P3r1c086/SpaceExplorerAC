package es.architectcoders.spaceexplorer.ui.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.architectcoders.data.datasource.ApodRemoteDataSource
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeApodEntity
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeApodEntity2
import es.architectcoders.spaceexplorer.data.server.MockWebServerRule
import es.architectcoders.spaceexplorer.data.server.fromJson
import es.architectcoders.spaceexplorer.framework.database.apodDb.ApodDao
import es.architectcoders.spaceexplorer.ui.MainActivity
import es.architectcoders.spaceexplorer.ui.common.Constants
import es.architectcoders.spaceexplorer.ui.home.HomeFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest//para que los test funcionen con hilt necesito esta anotacion
class ApodInstrumentationTest {

    @get:Rule(order = 0)//se configura hilt
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
        mockWebServerRule.server.enqueue(
            MockResponse().fromJson("item_apod.json")
        )

        hiltRule.inject()
    }

    @Test
    fun check_mock_server_is_working() = runTest {
        val apod = remoteDataSourceApod.getApod()
        apod.fold({ throw Exception(it.toString()) }) {
            assertEquals("2024-11-18", it?.date)
        }
    }

//    @Test
//    fun check_insert_apod_in_db() = runTest {
//        // Given
//        val apod = defaultFakeApodEntity
//
//        // When
//        apodDao.insertApod(apod)
//        val result = apodDao.apodsCount()
//
//        // Then
//        assertEquals(1, result)
//    }
//    @Test
//    fun check_find_apod_by_id() = runTest {
//        // Given
//        val apod = defaultFakeApodEntity
//        apodDao.insertApod(apod)
//
//        // When
//        val result = apodDao.findApodById(1)
//
//        // Then
//        assertEquals(apod, result)
//    }
//    @Test
//    fun check_find_apod_by_date() = runTest {
//        // Given
//        val apod = defaultFakeApodEntity
//        apodDao.insertApod(apod)
//
//        // When
//        val result = apodDao.findApodByDate("2023-01-01")
//
//        // Then
//        assertEquals(apod, result)
//    }
//
//    @Test
//    fun check_apods_count_in_db() = runTest {
//        // Given
//        val apod1 = defaultFakeApodEntity
//        val apod2 = defaultFakeApodEntity2
//        apodDao.insertApod(apod1)
//        apodDao.insertApod(apod2)
//
//        // When
//        val result = apodDao.apodsCount()
//
//        // Then
//        assertEquals(2, result)
//    }
//
//    @Test
//    fun check_update_apod_in_db() = runTest {
//        // Given
//        val apod = defaultFakeApodEntity
//        apodDao.insertApod(apod)
//
//        // When
//        val updatedApod = apod.copy(title = "Updated Title")
//        apodDao.updateApod(updatedApod)
//        val result = apodDao.findApodById(1)
//
//        // Then
//        assertEquals("Updated Title", result.title)
//    }
//
//    @Test
//    fun check_get_all_apods() = runTest {
//        // Given
//        val apod1 = defaultFakeApodEntity
//        val apod2 = defaultFakeApodEntity2
//        apodDao.insertApod(apod1)
//        apodDao.insertApod(apod2)
//
//        // When
//        val result = apodDao.getAllApods().first() //recolecto el flujo, usando `first()` para obtener el valor actual
//
//        // Then
//        assertEquals(listOf(apod1, apod2), result)
//    }

//    @Test
//    fun when_click_in_favorite_the_icon_change(){
//        // Verifica que el botón comienza en el estado inicial (no favorito)
//        onView(withId(R.id.ivApodFav))
//            .check(matches(withTagValue(`is`(Constants.NOT_FAVORITE_TAG))))  // Este "tag" es un ejemplo. Usar un recurso o atributo identificable.
//
//        // Simula el click en el botón de favorito
//        onView(withId(R.id.ivApodFav)).perform(click())
//
//        // Verifica que el botón ha cambiado al estado de favorito
//        onView(withId(R.id.ivApodFav))
//            .check(matches(withTagValue(`is`(Constants.FAVORITE_TAG))))  // Verifica que el estado cambió
//    }

//    @Test
//    fun testBottomNavigation() {
//        // Verifica que la BottomNavigationView esté visible
//        onView(withChild(withId(R.id.bottom_navigation) ))
//            .check(matches(ViewMatchers.isDisplayed()))
//        // Verifica que el primer item esté seleccionado al iniciar
////        onView(allOf(withId(R.id.homeFragment),withParent(withId(R.id.bottom_navigation) ))  )
////            .check(matches(ViewMatchers.isDisplayed()))
//        onView(withId(R.id.homeFragment))
//            .check(matches(ViewMatchers.isDisplayed()))
//
//        // Navega al segundo item (por ejemplo, "Dashboard")
//        onView(withId(R.id.roversFragment))
//            .perform(click())
//
//        // Verifica que el segundo item esté seleccionado
//        onView(withId(R.id.roversFragment))
//            .check(matches(ViewMatchers.isDisplayed()))
//
//        // Navega al tercer item (por ejemplo, "Notifications")
//        onView(withId(R.id.marsFragment))
//            .perform(click())
//
//        // Verifica que el tercer item esté seleccionado
//        onView(withId(R.id.marsFragment))
//            .check(matches(ViewMatchers.isDisplayed()))
//    }
}
