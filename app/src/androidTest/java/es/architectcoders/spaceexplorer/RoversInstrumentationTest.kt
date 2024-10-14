package es.architectcoders.spaceexplorer

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import es.architectcoders.spaceexplorer.ui.MainActivity
import org.junit.Rule
import org.junit.Test

class RoversInstrumentationTest {

    //todo: esta rule funciona al correr la app en un dispositivo con API 29 o inferior
    @get:Rule
    val storagePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_it_works(){

    }
}