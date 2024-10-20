package es.architectcoders.usecases.favoritos

import es.architectcoders.data.repository.FavoriteRepository
import es.architectcoders.usecases.GetFavoritesUseCase
import es.architectcoders.usecases.sampleApod
import es.architectcoders.usecases.samplePhoto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class GetFavoritesUseCaseTest {

    @Mock
    private lateinit var favoriteRepository: FavoriteRepository

    private lateinit var getFavoritesUseCase: GetFavoritesUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getFavoritesUseCase = GetFavoritesUseCase(favoriteRepository)
    }

    @Test
    fun `Invoke returns merged favorite items`() = runBlocking {
        // Configuro el mock para devolver un Flow que emite otro Flow con la lista combinada de favoritos
        whenever(favoriteRepository.getFavoriteList()).thenReturn(flowOf(flowOf(listOf(sampleApod, samplePhoto))))

        // Ejecuto el caso de uso y recogo el primer valor emitido
        val result = getFavoritesUseCase().first().first()

        // Verifico que el resultado sea la lista mergeada esperada
        assertEquals(listOf(sampleApod, samplePhoto), result)
    }
}