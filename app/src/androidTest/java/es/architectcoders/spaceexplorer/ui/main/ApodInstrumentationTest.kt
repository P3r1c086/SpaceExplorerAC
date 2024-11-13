package es.architectcoders.spaceexplorer.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeApodEntity
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeApodEntity2
import es.architectcoders.spaceexplorer.framework.database.apodDb.ApodDao
import es.architectcoders.spaceexplorer.ui.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest//para que los test funcionen con hilt necesito esta anotacion
class ApodInstrumentationTest {

    @get:Rule(order = 0)//se configura hilt
    val hiltRule = HiltAndroidRule(this)//con el this le pasamos la clase

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var apodDao: ApodDao

    @Before
    fun setUp(){
        hiltRule.inject()
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
        val result = apodDao.getAllApods().first() //recolecto el flujo, usando `first()` para obtener el valor actual

        // Then
        assertEquals(listOf(apod1, apod2), result)
    }

    @Test
    fun when_click_in_favorite_the_icon_change(){
        // Verifica que el botón comienza en el estado inicial (no favorito)
        onView(withId(R.id.ivApodFav))
            .check(matches(withTagValue(`is`("not_favorite"))))  // Este "tag" es un ejemplo. Usar un recurso o atributo identificable.

        // Simula el click en el botón de favorito
        onView(withId(R.id.ivApodFav)).perform(click())

        // Verifica que el botón ha cambiado al estado de favorito
        onView(withId(R.id.ivApodFav))
            .check(matches(withTagValue(`is`("favorite"))))  // Verifica que el estado cambió
    }
}