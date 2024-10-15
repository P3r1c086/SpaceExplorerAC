package es.architectcoders.spaceexplorer.ui.main

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.architectcoders.spaceexplorer.ui.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@HiltAndroidTest//para que los test funcionen con hilt necesito esta anotacion
class RoversInstrumentationTest {

    @get:Rule(order = 0)//primero que se configure hilt
    val hiltRule = HiltAndroidRule(this)//con el this le pasamos la clase

    //todo: esta rule funciona al correr la app en un dispositivo con API 29 o inferior
    @get:Rule(order = 1)//segundo que se configure el permiso
    val storagePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )

    @get:Rule(order = 2)//tercero que se configure la activity rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_it_works(){

    }
}