package es.architectcoders.spaceexplorer.ui.rovers

import app.cash.turbine.test
import es.architectcoders.domain.Photo
import es.architectcoders.spaceexplorer.testRules.CoroutinesTestRules
import es.architectcoders.usecases.GetRoversUseCase
import es.architectcoders.usecases.RequestRoversUseCase
import es.architectcoders.usecases.SaveRoversFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
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

    private val photos = listOf(samplePhoto.copy(id = 1))

    private lateinit var vm: RoversViewModel


    @Test
    fun `State is updated with current cached content immediately`() = runTest {
        vm = buildViewModel()

        vm.state.test {
            assertEquals(RoversViewModel.UiState(), awaitItem())
            assertEquals(RoversViewModel.UiState(photoList = photos), awaitItem())
            cancel()
        }
    }

    @Test
    fun `Progress is shown when screen starts and hidden when it finishes requesting photos`() =
        runTest {
            vm = buildViewModel()
            vm.onUiReady()

            vm.state.test {
                assertEquals(RoversViewModel.UiState(), awaitItem())
                assertEquals(RoversViewModel.UiState(photoList = photos), awaitItem())
                assertEquals(RoversViewModel.UiState(photoList = photos, loading = true), awaitItem())
                assertEquals(RoversViewModel.UiState(photoList = photos, loading = false), awaitItem())
                cancel()
            }
        }

    @Test
    fun `Photos are requested when UI screen starts`() = runTest {
        vm = buildViewModel()
        vm.onUiReady()
        runCurrent()

        verify(requestRoversUseCase).invoke()
    }

    private fun buildViewModel() : RoversViewModel {
        whenever(getRoversUseCase()).thenReturn(flowOf(photos))
        return RoversViewModel(requestRoversUseCase, getRoversUseCase, saveRoverFavoriteUseCase)
    }
}

private val samplePhoto = Photo(
    "2023-01-01",
    0,
    "https://example.com",
    "1",
    false
)
