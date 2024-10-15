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
    fun `Invoke returns combined favorite items`() = runBlocking {
        // Simulamos el comportamiento del repositorio para devolver una lista combinada de favoritos
        whenever(favoriteRepository.getFavoriteList()).thenReturn(flowOf(listOf(sampleApod, samplePhoto)))

        // Ejecutamos el caso de uso
        val result = getFavoritesUseCase().first()

        // Verificamos que el resultado devuelto sea la lista combinada de favoritos esperada
        val expectedItems = listOf(sampleApod, samplePhoto)
        assertEquals(expectedItems, result)
    }
}