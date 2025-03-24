package es.architectcoders.spaceexplorer.ui.rovers

import app.cash.turbine.test
import es.architectcoders.domain.Error.*
import es.architectcoders.domain.Photo
import es.architectcoders.spaceexplorer.framework.toError
import es.architectcoders.spaceexplorer.testRules.CoroutinesTestRules
import es.architectcoders.usecases.GetRoversUseCase
import es.architectcoders.usecases.RequestRoversUseCase
import es.architectcoders.usecases.SaveRoversFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RoversViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRules()

    @Mock
    private lateinit var requestRoversUseCase: RequestRoversUseCase

    @Mock
    private lateinit var getRoversUseCase: GetRoversUseCase

    @Mock
    private lateinit var saveRoverFavoriteUseCase: SaveRoversFavoriteUseCase

    private val photos = listOf(samplePhoto.copy(id = "1"))

    private lateinit var vm: RoversViewModel


    @Test
    fun `State is updated with current cached content immediately`() = runTest {
        vm = buildViewModel()

        vm.state.test {
            assertEquals(RoversViewModel.UiState(), awaitItem())
            assertEquals(RoversViewModel.UiState(loading = true), awaitItem())
            assertEquals(RoversViewModel.UiState(loading = false, error = null), awaitItem())
            assertEquals(RoversViewModel.UiState(photoList = photos), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Progress is shown when screen starts and hidden when it finishes requesting photos`() =
        runTest {
            // Given(condiciones)
            whenever(getRoversUseCase()).thenReturn(flowOf(photos))
            whenever(requestRoversUseCase()).thenReturn(null)
            vm = buildViewModel()

            // When(lo que estas testeando)

            // Then(resultados esperados)
            vm.state.test {
                assertEquals(RoversViewModel.UiState(), awaitItem())
                assertEquals(RoversViewModel.UiState(loading = true), awaitItem())
                assertEquals(RoversViewModel.UiState(loading = false), awaitItem())
                assertEquals(RoversViewModel.UiState(photoList = photos, loading = false, error = null), awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `Photos are requested when UI screen starts`() = runTest {
        vm = buildViewModel()
        runCurrent()

        verify(requestRoversUseCase).invoke()
    }

    @Test
    fun `Error is shown when photos cannot be requested`() = runTest {
        // Given
        val error = Unknown("Error unknown")
        whenever(requestRoversUseCase()).thenReturn(error)
        vm = buildViewModel()
        // When

        // Then
        vm.state.test {
            assertEquals(RoversViewModel.UiState(), awaitItem())
            assertEquals(RoversViewModel.UiState(loading = true), awaitItem())
            assertEquals(RoversViewModel.UiState(photoList = null, loading = false, error = error), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
    @Test
    fun `Error is shown when photos cannot be obtained from room`() = runTest {
        // Given
        val error = Throwable("Error unknown")
        vm = buildViewModelWithRoomError()
        // When

        // Then
        vm.state.test {
            assertEquals(RoversViewModel.UiState(), awaitItem())
            assertEquals(RoversViewModel.UiState(loading = true), awaitItem())
            assertEquals(RoversViewModel.UiState(photoList = null, loading = false, error = null), awaitItem())
            assertEquals(RoversViewModel.UiState(photoList = null, loading = false, error = error.toError()), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Photo is saved as favorite`() = runTest {
        // Given
        val photo = samplePhoto.copy(id = "1")
        vm = buildViewModelWithRoomError()
        // When
        vm.saveRoversAsFavourite(photo)
        // Then
        vm.state.test {
            assertNull(RoversViewModel.UiState(error = null).error)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Photo isn't saved as favorite`() = runTest {
        // Given
        val photo = samplePhoto.copy(id = "1")
        vm = buildViewModelWithRoomError()

        // When
        vm.saveRoversAsFavourite(photo)

        // Then
        vm.state.test {
            assertEquals(RoversViewModel.UiState(error = null), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
    @Test
    fun `When press retry launches to request`() = runTest {
        // Given
        vm = buildViewModelWithRoomError()

        // When
        vm.retry()
        runCurrent()
        // Then
        verify(requestRoversUseCase, times(2)).invoke()
    }

    private fun buildViewModel() : RoversViewModel {
        whenever(getRoversUseCase()).thenReturn(flowOf(photos))
        return RoversViewModel(requestRoversUseCase, getRoversUseCase, saveRoverFavoriteUseCase)
    }
    private fun buildViewModelWithRoomError() : RoversViewModel {
        val error = Throwable("Error unknown")
        whenever(getRoversUseCase()).thenReturn(flow { throw error })
        return RoversViewModel(requestRoversUseCase, getRoversUseCase, saveRoverFavoriteUseCase)
    }
}

private val samplePhoto = Photo(
    date = "2023-01-01",
    title = "Sample Photo",
    explanation = "A sample photo explanation.",
    hdurl = "https://example.com/hd",
    url = "https://example.com",
    mediaType = "image",
    serviceVersion = "v1",
    type = "photo",
    favorite = false,
    sol = "1",
    imgSrc = "https://example.com/image.jpg",
    id = "1",
    earthDate = "2023-01-01"
)
