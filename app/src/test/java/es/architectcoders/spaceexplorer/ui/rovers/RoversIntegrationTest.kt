package es.architectcoders.spaceexplorer.ui.rovers

import app.cash.turbine.test
import es.architectcoders.data.repository.RoversRepository
import es.architectcoders.domain.Photo
import es.architectcoders.spaceexplorer.testRules.CoroutinesTestRules
import es.architectcoders.spaceexplorer.apptestshared.FakeRoversLocalDataSource
import es.architectcoders.spaceexplorer.apptestshared.FakeRoversRemoteDataSource
import es.architectcoders.spaceexplorer.apptestshared.samplePhoto
import es.architectcoders.usecases.GetRoversUseCase
import es.architectcoders.usecases.RequestRoversUseCase
import es.architectcoders.usecases.SaveRoversFavoriteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RoversIntegrationTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRules()

    @Test
    fun `Data is loaded from server when local source is empty`() = runTest {
        val remoteData = listOf(samplePhoto.copy(id = "1"), samplePhoto.copy(id = "2"))
        val vm = buildViewModelWith(
            localData = emptyList(),
            remoteData = remoteData
        )

        vm.state.test {
            assertEquals(RoversViewModel.UiState(), awaitItem())
            assertEquals(RoversViewModel.UiState(loading = true), awaitItem())
            assertEquals(RoversViewModel.UiState(loading = false, error = null), awaitItem())
            assertEquals(RoversViewModel.UiState(photoList = remoteData), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Data is loaded from local source when remote source is empty`() = runTest {
        val localData = listOf(samplePhoto.copy(id = "10"), samplePhoto.copy(id = "11"))
        val remoteData = listOf(samplePhoto.copy(id = "1"), samplePhoto.copy(id = "2"))
        val vm = buildViewModelWith(
            localData = localData,
            remoteData = remoteData
        )

        vm.state.test {
            assertEquals(RoversViewModel.UiState(), awaitItem())
            assertEquals(RoversViewModel.UiState(loading = true), awaitItem())
            assertEquals(RoversViewModel.UiState(loading = false, error = null), awaitItem())
            assertEquals(RoversViewModel.UiState(photoList = localData), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
    private fun buildViewModelWith(
        localData: List<Photo> = emptyList(),
        remoteData: List<Photo> = emptyList()
    ): RoversViewModel {
        val localDataSource = FakeRoversLocalDataSource().apply { inMemoryPhotos.value = localData }
        val remoteDataSource = FakeRoversRemoteDataSource().apply { photos = remoteData }
        val roversRepository = RoversRepository(localDataSource, remoteDataSource)
        val getRoversUseCase = GetRoversUseCase(roversRepository)
        val requestRoversUseCase = RequestRoversUseCase(roversRepository)
        val saveRoversFavoriteUseCase = SaveRoversFavoriteUseCase(roversRepository)
        return RoversViewModel(requestRoversUseCase, getRoversUseCase, saveRoversFavoriteUseCase)
    }
}