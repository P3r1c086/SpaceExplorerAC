package es.architectcoders.spaceexplorer.ui.main

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.architectcoders.spaceexplorer.R
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotifications
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotifications2
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotificationsDb
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotificationsDb2
import es.architectcoders.spaceexplorer.framework.database.notificationsDb.NotificationsDao
import es.architectcoders.spaceexplorer.framework.database.notificationsDb.fromDomain
import es.architectcoders.spaceexplorer.ui.HiltTestActivity
import es.architectcoders.spaceexplorer.ui.home.HomeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest//para que los test funcionen con hilt necesito esta anotacion
class NotificationsInstrumentationTest {

    @get:Rule(order = 0)//primero que se configure hilt
    val hiltRule = HiltAndroidRule(this)//con el this le pasamos la clase

    // TestCoroutineDispatcher para controlar el hilo principal en pruebas
    private val testDispatcher = TestCoroutineDispatcher()

    @Inject
    lateinit var notificationsDao: NotificationsDao

    @Before
    fun setUp(){

        // Configura el Dispatcher principal para que use TestCoroutineDispatcher
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
    fun check_navigation_to_notifications(){

        // Crear el NavController de prueba
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
            setGraph(R.navigation.main_nav_graph)
        }

        // Lanza el Fragment usando la función launchFragmentInHiltContainer
        launchFragmentInHiltContainer<HomeFragment>(themeResId = com.google.android.apps.common.testing.accessibility.framework.R.style.Theme_MaterialComponents) {
            // Configurar NavController dentro de este bloque
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                Navigation.setViewNavController(requireView(), navController)
            }
        }

        // Haz clic en el elemento del BottomNavigationView
        onView(withId(R.id.marsFragment)).perform(click())

        // Verifica que el destino actual es el esperado
        assertEquals(R.id.marsFragment, navController.currentDestination?.id)
    }

    // Función para lanzar Fragmentos en contenedores habilitados para Hilt
    private inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null,
        themeResId: Int = androidx.fragment.testing.R.style.FragmentScenarioEmptyFragmentActivityTheme,
        crossinline action: T.() -> Unit = {}
    ) {
        val mainActivityIntent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                HiltTestActivity::class.java
            )
        ).putExtra("androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY", themeResId)

        ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { activity ->
            val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                T::class.java.classLoader!!,
                T::class.java.name
            )
            fragment.arguments = fragmentArgs
            activity.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()

            (fragment as T).action()  // Asegúrate de que action se ejecute sin parámetros
        }
    }
}