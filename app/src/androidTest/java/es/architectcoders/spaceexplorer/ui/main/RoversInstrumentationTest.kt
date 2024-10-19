package es.architectcoders.spaceexplorer.ui.main

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.architectcoders.spaceexplorer.apptestshared.FakeRoversLocalDataSource
import es.architectcoders.spaceexplorer.apptestshared.defaultFakePhotos
import es.architectcoders.spaceexplorer.apptestshared.defaultFakePhotos2
import es.architectcoders.spaceexplorer.apptestshared.samplePhoto
import es.architectcoders.spaceexplorer.framework.database.roverDb.RoversDao
import es.architectcoders.spaceexplorer.framework.database.roverDb.fromDomain
import es.architectcoders.spaceexplorer.ui.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
@HiltAndroidTest//para que los test funcionen con hilt necesito esta anotacion
class RoversInstrumentationTest {

    @get:Rule(order = 0)//primero que se configure hilt
    val hiltRule = HiltAndroidRule(this)//con el this le pasamos la clase

    //Esta rule funciona al correr la app en un dispositivo con API 29 o inferior
    @get:Rule(order = 1)//segundo que se configure el permiso
    val storagePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )

    @Inject
    lateinit var roversDao: RoversDao

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun check_4_items_db() = runTest{
        roversDao.insertRovers(defaultFakePhotos.fromDomain())
        assertEquals(4,roversDao.roversCount())
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

        roversDao.insertRovers(defaultFakePhotos.fromDomain())
        roversDao.insertRovers(defaultFakePhotos2.fromDomain())
        assertEquals(6,roversDao.roversCount())
    }

    //todo: hacer test apod y notifications
}