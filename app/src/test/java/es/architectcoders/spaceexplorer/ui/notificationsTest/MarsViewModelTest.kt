package es.architectcoders.spaceexplorer.ui.notificationsTest

import app.cash.turbine.test
import es.architectcoders.domain.Error
import es.architectcoders.domain.NotificationsItem
import es.architectcoders.spaceexplorer.framework.toError
import es.architectcoders.spaceexplorer.testRules.CoroutinesTestRules
import es.architectcoders.spaceexplorer.ui.mars.MarsViewModel
import es.architectcoders.spaceexplorer.ui.rovers.RoversViewModel
import es.architectcoders.usecases.GetNotificationsUseCase
import es.architectcoders.usecases.RequestNotificationsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@RunWith(MockitoJUnitRunner::class)
class MarsViewModelTest{

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRules()

    @Mock
    private lateinit var getNotificationsUseCase: GetNotificationsUseCase

    @Mock
    private lateinit var requestNotificationsUseCase: RequestNotificationsUseCase

    private val notifications = listOf(sampleNotificationItem)

    private lateinit var vm: MarsViewModel


    @Test
    fun `State is updated with current cached content immediately`() = runTest {
        vm = buildViewModel()

        vm.state.test {
            assertEquals(MarsViewModel.UiState(), awaitItem())
            assertEquals(MarsViewModel.UiState(loading = true), awaitItem())
            assertEquals(MarsViewModel.UiState(loading = false, error = null), awaitItem())
            assertEquals(MarsViewModel.UiState(notificationsList = notifications), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Progress is shown when screen starts and hidden when it finishes requesting notifications`() = runTest {
        // Given(condiciones)
        whenever(getNotificationsUseCase()).thenReturn(flowOf(notifications))
        whenever(requestNotificationsUseCase()).thenReturn(null)
        vm = buildViewModel()

        // When(lo que estas testeando)

        // Then(resultados esperados)
        vm.state.test {
            assertEquals(MarsViewModel.UiState(), awaitItem())
            assertEquals(MarsViewModel.UiState(loading = true), awaitItem())
            assertEquals(MarsViewModel.UiState(loading = false), awaitItem())
            assertEquals(MarsViewModel.UiState(notificationsList = notifications, loading = false, error = null), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Notifications are requested when screen starts`() = runTest {
        // Given(condiciones)
        vm = buildViewModel()
        // When(lo que estas testeando)

        // Then(resultados esperados)
        runCurrent()// wait for coroutines to finish
        verify(requestNotificationsUseCase).invoke()
    }

    @Test
    fun `Error is shown when notifications cannot be requested`() = runTest {
        // Given(condiciones)
        val error = Error.Unknown("Error unknown")
        whenever(requestNotificationsUseCase()).thenReturn(error)
        vm = buildViewModel()
        // When

        // Then
        vm.state.test {
            assertEquals(MarsViewModel.UiState(), awaitItem())
            assertEquals(MarsViewModel.UiState(loading = true), awaitItem())
            assertEquals(MarsViewModel.UiState(notificationsList = null, loading = false, error = error), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Error is shown when notifications can't be obtained from room`() = runTest {
        // Given
        val error = Throwable("Error unknown")
        vm = buildViewModelWithRoomError()
        // When

        // Then
        vm.state.test {
            assertEquals(MarsViewModel.UiState(), awaitItem())
            assertEquals(MarsViewModel.UiState(loading = true), awaitItem())
            assertEquals(MarsViewModel.UiState(notificationsList = null, loading = false, error = null), awaitItem())
            assertEquals(MarsViewModel.UiState(notificationsList = null, loading = false, error = error.toError()), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When press retry launches to request`() = runTest {
        // Given
        vm = buildViewModelWithRoomError()

        // When
        vm.retry()
        runCurrent()// wait for coroutines to finish
        // Then
        verify(requestNotificationsUseCase, times(2)).invoke()
    }

    private fun buildViewModel(): MarsViewModel {
        whenever(getNotificationsUseCase()).thenReturn(flowOf(notifications))
        return MarsViewModel(requestNotificationsUseCase, getNotificationsUseCase)
    }

    private fun buildViewModelWithRoomError() : MarsViewModel {
        val error = Throwable("Error unknown")
        whenever(getNotificationsUseCase()).thenReturn(flow { throw error })
        return MarsViewModel(requestNotificationsUseCase, getNotificationsUseCase)
    }
}

private val sampleNotificationItem = NotificationsItem(
    "2014-05-08T12:43Z",
    "023_AB_123",
    "## NASA Goddard Space Flight Center, Space Weather Research Center ( SWRC )## Message Type: Space Weather Notification - M5.2 Flare#### Message Issue Date: 2014-05-08T12:43:04Z## Message ID: 20140508-AL-002## Disclaimer: NOAA's Space Weather Prediction Center (http://swpc.noaa.gov) is the United States Government official source for space weather forecasts. This Experimental Research Information",
    "https://kauai.ccmc.gsfc.nasa.gov/DONKI/view/Alert/5429/1",
    "FLR"
)