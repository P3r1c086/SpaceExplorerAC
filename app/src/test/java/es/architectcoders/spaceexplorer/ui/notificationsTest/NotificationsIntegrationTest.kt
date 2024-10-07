package es.architectcoders.spaceexplorer.ui.notificationsTest

import app.cash.turbine.test
import es.architectcoders.data.repository.NotificationsRepository
import es.architectcoders.domain.NotificationsItem
import es.architectcoders.spaceexplorer.testRules.CoroutinesTestRules
import es.architectcoders.spaceexplorer.apptestshared.FakeNotificationsLocalDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeNotificationsRemoteDataSource
import es.architectcoders.spaceexplorer.apptestshared.sampleNotificationItem
import es.architectcoders.spaceexplorer.ui.notifications.NotificationsViewModel
import es.architectcoders.usecases.GetNotificationsUseCase
import es.architectcoders.usecases.RequestNotificationsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class NotificationsIntegrationTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRules()

    @Test
    fun `Data is loaded from server when local source is empty`() = runTest {
        val remoteData = listOf(sampleNotificationItem.copy(messageID = "1"), sampleNotificationItem.copy(messageID = "2"))
        val vm = buildViewModelWith(
            localData = emptyList(),
            remoteData = remoteData
        )

        vm.state.test {
            Assert.assertEquals(NotificationsViewModel.UiState(), awaitItem())
            Assert.assertEquals(NotificationsViewModel.UiState(loading = true), awaitItem())
            Assert.assertEquals(NotificationsViewModel.UiState(loading = false, error = null), awaitItem())
            Assert.assertEquals(NotificationsViewModel.UiState(notificationsList = remoteData), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Data is loaded from local source when remote source is empty`() = runTest {
        val localData = listOf(sampleNotificationItem.copy(messageID = "10"), sampleNotificationItem.copy(messageID = "11"))
        val remoteData = listOf(sampleNotificationItem.copy(messageID = "1"), sampleNotificationItem.copy(messageID = "2"))
        val vm = buildViewModelWith(
            localData = localData,
            remoteData = remoteData
        )

        vm.state.test {
            Assert.assertEquals(NotificationsViewModel.UiState(), awaitItem())
            Assert.assertEquals(NotificationsViewModel.UiState(loading = true), awaitItem())
            Assert.assertEquals(NotificationsViewModel.UiState(loading = false, error = null), awaitItem())
            Assert.assertEquals(NotificationsViewModel.UiState(notificationsList = localData), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
    private fun buildViewModelWith(
        localData: List<NotificationsItem> = emptyList(),
        remoteData: List<NotificationsItem> = emptyList()
    ): NotificationsViewModel {
        val localDataSource = FakeNotificationsLocalDataSource().apply { inMemoryNotifications.value = localData }
        val remoteDataSource = FakeNotificationsRemoteDataSource().apply { notifications = remoteData }
        val notificationsRepository = NotificationsRepository(localDataSource, remoteDataSource)
        val getNotificationsUseCase = GetNotificationsUseCase(notificationsRepository)
        val requestNotificationsUseCase = RequestNotificationsUseCase(notificationsRepository)
        return NotificationsViewModel(requestNotificationsUseCase, getNotificationsUseCase)
    }
}