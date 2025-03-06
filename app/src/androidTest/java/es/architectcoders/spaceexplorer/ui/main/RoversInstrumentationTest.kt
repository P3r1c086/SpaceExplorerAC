package es.architectcoders.spaceexplorer.ui.main

import androidx.test.rule.GrantPermissionRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.architectcoders.data.datasource.RoversRemoteDataSource
import es.architectcoders.spaceexplorer.apptestshared.defaultFakePhotos
import es.architectcoders.spaceexplorer.apptestshared.defaultFakePhotos2
import es.architectcoders.spaceexplorer.data.server.MockWebServerRule
import es.architectcoders.spaceexplorer.data.server.fromJson
import es.architectcoders.spaceexplorer.framework.database.roverDb.RoversDao
import es.architectcoders.spaceexplorer.framework.database.roverDb.fromDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
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

    //Esta rule funciona al correr la app en un dispositivo con API 29 o inferior
    @get:Rule(order = 2)//segundo que se configure el permiso
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
}