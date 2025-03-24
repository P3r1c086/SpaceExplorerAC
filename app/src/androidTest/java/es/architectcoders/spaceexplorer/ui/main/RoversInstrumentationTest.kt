package es.architectcoders.spaceexplorer.ui.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.architectcoders.data.datasource.RoversRemoteDataSource
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.apptestshared.defaultFakePhotos
import es.architectcoders.spaceexplorer.apptestshared.defaultFakePhotos2
import es.architectcoders.spaceexplorer.data.server.MockWebServerRule
import es.architectcoders.spaceexplorer.framework.database.roverDb.RoversDao
import es.architectcoders.spaceexplorer.framework.database.roverDb.fromDomain
import es.architectcoders.spaceexplorer.ui.MainActivity
import es.architectcoders.spaceexplorer.ui.rovers.RoversAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class RoversInstrumentationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)//con el this le pasamos la clase

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    //Esta rule funciona al correr la app en un dispositivo con API 29 o inferior
    @get:Rule(order = 3)
    val storagePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )

    @Inject
    lateinit var roversDao: RoversDao

    @Inject
    lateinit var remoteDataSourceRovers: RoversRemoteDataSource

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun check_4_items_db() = runTest{
        //Given
        roversDao.insertRovers(defaultFakePhotos.fromDomain())
        //When
        val result = roversDao.roversCount()
        //Then
        assertEquals(4,result)
    }
    @Test
    fun check_6_items_db() = runTest{
        //Given
        roversDao.insertRovers(defaultFakePhotos2.fromDomain())
        //When
        val result = roversDao.roversCount()
        //Then
        assertEquals(6,result)
    }
    @Test
    fun check_replace_items_db() = runTest{
        //Given
        roversDao.insertRovers(defaultFakePhotos.fromDomain())
        roversDao.insertRovers(defaultFakePhotos2.fromDomain())
        //When
        val result =roversDao.roversCount()
        //Then
        assertEquals(6,result)
    }

    @Test
    fun check_mock_server_is_working() = runTest {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date = dateFormat.parse("2023-10-31")
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }
        val photos = remoteDataSourceRovers.getRovers(calendar)
        photos.fold({ throw Exception(it.toString()) }) {
            assertEquals("1204630", it[0].id)
        }
    }

    @Test
    fun when_click_in_expand_button_the_text_is_showed() {
        // Click en el fragmento de rovers
        Espresso.onView(ViewMatchers.withId(R.id.roversFragment))
            .perform(ViewActions.click())

        // Esperar para que los datos carguen (usando una acción personalizada)
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(5000))

        // Verificar que RecyclerView está visible
        Espresso.onView(ViewMatchers.withId(R.id.rvRovers))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Click en el primer elemento del RecyclerView
        Espresso.onView(ViewMatchers.withId(R.id.rvRovers))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RoversAdapter.ViewHolder>(0, ViewActions.click())
            )

        // Click en el botón de expandir dentro del primer elemento del RecyclerView
        Espresso.onView(ViewMatchers.withId(R.id.rvRovers))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RoversAdapter.ViewHolder>(
                    0,
                    clickChildViewWithId(R.id.ibExpand)
                )
            )

        // Hacer scroll hasta el primer elemento para asegurarse de que es visible
        Espresso.onView(ViewMatchers.withId(R.id.rvRovers))
            .perform(RecyclerViewActions.scrollToPosition<RoversAdapter.ViewHolder>(0))

        // Verificar que el contenido expandido (llData) es visible solo dentro del primer elemento
        Espresso.onView(
            CoreMatchers.allOf(
                ViewMatchers.withId(R.id.llData),
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
                ViewMatchers.isDescendantOfA(RecyclerViewMatchers.atPosition(0, R.id.rvRovers))
            )
        ).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}