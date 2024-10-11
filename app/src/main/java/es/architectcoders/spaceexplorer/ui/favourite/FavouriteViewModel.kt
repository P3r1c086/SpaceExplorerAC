package es.architectcoders.spaceexplorer.ui.favourite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.architectcoders.domain.Error
import es.architectcoders.domain.NasaItem
import es.architectcoders.spaceexplorer.framework.toError
import es.architectcoders.usecases.GetFavoritesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavouriteViewModel.UiState())
    val state: StateFlow<FavouriteViewModel.UiState> = _state.asStateFlow()

    private val favoriteList = mutableListOf<NasaItem>()

    init {
        loadData()
    }
    fun loadData() {
            viewModelScope.launch {
                getFavoritesUseCase()
                    .flowOn(Dispatchers.IO)
                    .onStart {
                        favoriteList.clear()//Se limpia la lista favoriteList para asegurarse de que esté vacía antes de cargar nuevos datos.
                        _state.update {
                            it.copy(loading = true, items = favoriteList)//esta lista siempre estará vacía
                        }
                    }
                    .catch { cause ->
                        _state.update {
                            it.copy(
                                loading = false,
                                error = cause.toError()
                            )
                        }
                    }
                    .onEach { flow ->//onEach se ejecuta cada vez que el flujo emite un nuevo elemento.(entendemos elemento como una lista de elementos apod o photo)
                        flow.first { true }.also { items ->//No entiendo este paso. Si items fuera un solo elemento (como parece ser el caso por el uso de first { true }),
                            // entonces este código no estaría haciendo lo esperado, ya que debería estar trabajando con toda la lista emitida por el flujo.
                        favoriteList.addAll(items.sortedBy {
                            it.id})
                            favoriteList//puesto para verificar el valor de la lista mientras se desarrollaba el código, pero no es estrictamente necesaria.
                        }
                    }
                    .onCompletion {//es una función que se ejecuta cuando el flujo termina de emitir elementos, es decir, cuando se ha completado su trabajo.
                        if (favoriteList.isEmpty()) {
                            _state.update {
                                it.copy(loading = false, error = Error.Unknown("No hay favoritos"))
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    loading = false,
                                    items = favoriteList/*.sortedByDescending { favorite -> favorite.date }*/)
                            }
                        }
                    }.collect()// es la función que se utiliza para iniciar la recopilación de los elementos emitidos por el flujo.
            }
        }


    data class UiState(
        val loading: Boolean = false,
        val items: List<NasaItem>? = null,
        val onBackPressed: Boolean = false,
        val error: Error? = null
    )
}