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

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val favoriteList = mutableListOf<NasaItem>()

    init {
        loadData()
    }
    fun loadData() {
        viewModelScope.launch {
            getFavoritesUseCase()
                .flowOn(Dispatchers.IO)
                .onStart {
                    favoriteList.clear()
                    _state.update {
                        it.copy(loading = true, items = emptyList())
                    }
                }
                .catch { cause ->
                    _state.update {
                        it.copy(loading = false, error = cause.toError())
                    }
                }
                .onEach { items ->
                    favoriteList.clear()
                    favoriteList.addAll(items.sortedBy { it.date })
                    _state.update {
                        it.copy(items = favoriteList.toList())
                    }
                }
                .onCompletion {
                    if (favoriteList.isEmpty()) {
                        _state.update {
                            it.copy(loading = false, error = Error.Unknown("No hay favoritos"))
                        }
                    } else {
                        _state.update {
                            it.copy(loading = false, items = favoriteList)
                        }
                    }
                }
                .collect()
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val items: List<NasaItem>? = null,
        val onBackPressed: Boolean = false,
        val error: Error? = null
    )
}