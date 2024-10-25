package es.architectcoders.spaceexplorer.ui.main

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotifications
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotifications2
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotificationsDb
import es.architectcoders.spaceexplorer.apptestshared.defaultFakeNotificationsDb2
import es.architectcoders.spaceexplorer.apptestshared.defaultFakePhotos
import es.architectcoders.spaceexplorer.apptestshared.defaultFakePhotos2
import es.architectcoders.spaceexplorer.framework.database.notificationsDb.NotificationsDao
import es.architectcoders.spaceexplorer.framework.database.notificationsDb.fromDomain
import es.architectcoders.spaceexplorer.framework.database.roverDb.fromDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest//para que los test funcionen con hilt necesito esta anotacion
class NotificationsInstrumentationTest {

    @get:Rule(order = 0)//primero que se configure hilt
    val hiltRule = HiltAndroidRule(this)//con el this le pasamos la clase

    @Inject
    lateinit var notificationsDao: NotificationsDao

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun check_4_items_db() = runTest{
        //Given
        notificationsDao.insertNotifications(defaultFakeNotifications.fromDomain())
        //When
        val result = notificationsDao.notificationsCount()
        //Then
        Assert.assertEquals(4,result)
    }

    @Test
    fun check_6_items_db() = runTest{
        //Given
        notificationsDao.insertNotifications(defaultFakeNotifications2.fromDomain())
        //When
        val result = notificationsDao.notificationsCount()
        //Then
        Assert.assertEquals(6, result)
    }

    @Test
    fun check_replace_items_db() = runTest{
        //Given
        notificationsDao.insertNotifications(defaultFakeNotificationsDb)
        notificationsDao.insertNotifications(defaultFakeNotificationsDb2)
        //When
        val result =notificationsDao.notificationsCount()
        //Then
        Assert.assertEquals(6, result)
    }
}