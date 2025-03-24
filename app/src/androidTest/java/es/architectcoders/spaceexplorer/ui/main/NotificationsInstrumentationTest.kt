package es.architectcoders.spaceexplorer.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotifications
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotifications2
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotificationsDb
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotificationsDb2
import es.architectcoders.spaceexplorer.framework.database.notificationsDb.NotificationsDao
import es.architectcoders.spaceexplorer.framework.database.notificationsDb.fromDomain
import es.architectcoders.spaceexplorer.ui.MainActivity
import es.architectcoders.spaceexplorer.ui.notifications.NotificationsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class NotificationsInstrumentationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)//con el this le pasamos la clase

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    // TestCoroutineDispatcher para controlar el hilo principal en pruebas
    private val testDispatcher = TestCoroutineDispatcher()

    @Inject
    lateinit var notificationsDao: NotificationsDao

    @Before
    fun setUp(){
        // Configuro el Dispatcher principal para que use TestCoroutineDispatcher
        Dispatchers.setMain(testDispatcher)
        hiltRule.inject()
    }

    @Test
    fun check_4_items_db() = runTest{
        //Given
        notificationsDao.insertNotifications(defaultFakeNotifications.fromDomain())
        //When
        val result = notificationsDao.notificationsCount()
        //Then
        assertEquals(4,result)
    }

    @Test
    fun check_6_items_db() = runTest{
        //Given
        notificationsDao.insertNotifications(defaultFakeNotifications2.fromDomain())
        //When
        val result = notificationsDao.notificationsCount()
        //Then
        assertEquals(6, result)
    }

    @Test
    fun check_replace_items_db() = runTest{
        //Given
        notificationsDao.insertNotifications(defaultFakeNotificationsDb)
        notificationsDao.insertNotifications(defaultFakeNotificationsDb2)
        //When
        val result =notificationsDao.notificationsCount()
        //Then
        assertEquals(6, result)
    }

    @Test
    fun when_click_in_expand_button_the_text_is_showed(){
        // Navega a NotificationsFragment a través del BottomNavigationView
        onView(withId(R.id.marsFragment))
            .perform(click())

        onView(isRoot()).perform(waitFor(5000))

        // Espera hasta que `rvNotifications` sea visible
        onView(withId(R.id.rvNotifications))
            .check(matches(isDisplayed()))

        // Realiza scroll hasta el primer elemento y haz click en `ibExpand`
        onView(withId(R.id.rvNotifications))
            .perform(RecyclerViewActions.actionOnItemAtPosition<NotificationsAdapter.ViewHolder>(0, click()))

        // Verifica que `llData` del primer item se muestra después del click
        onView(withId(R.id.rvNotifications))
            .perform(RecyclerViewActions.actionOnItemAtPosition<NotificationsAdapter.ViewHolder>(0, clickChildViewWithId(R.id.ibExpand)))

        // Verifica que el contenido expandido es visible
        onView(withId(R.id.rvNotifications))
            .perform(RecyclerViewActions.scrollToPosition<NotificationsAdapter.ViewHolder>(0))

        onView(withId(R.id.llData))
            .check(matches(isDisplayed()))
    }
}