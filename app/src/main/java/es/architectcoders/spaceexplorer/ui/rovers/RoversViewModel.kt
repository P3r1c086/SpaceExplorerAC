package es.architectcoders.spaceexplorer.ui.rovers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.architectcoders.domain.Error
import es.architectcoders.domain.Photo
import es.architectcoders.spaceexplorer.framework.toError
import es.architectcoders.usecases.GetRoversUseCase
import es.architectcoders.usecases.RequestRoversUseCase
import es.architectcoders.usecases.SaveRoversFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoversViewModel @Inject constructor(
    private val requestRoversUseCase: RequestRoversUseCase,
    getRoversUseCase: GetRoversUseCase,
    private val saveRoverFavoriteUseCase: SaveRoversFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { _state.value.copy(loading = true) }
            val error = requestRoversUseCase()
            _state.update { _state.value.copy(loading = false, error = error) }
            getRoversUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect{ photos -> _state.update {
                    UiState(photoList = photos) } }
        }
    }

    fun saveRoversAsFavourite(photo: Photo) {
        viewModelScope.launch {
            val error: Error? = saveRoverFavoriteUseCase(photo)
            _state.value = _state.value.copy(error = error)
        }
    }

    fun retry() {
        viewModelScope.launch {
            _state.value = _state.value.copy(error = null, loading = true)
            _state.update { uiState ->
                uiState.copy(loading = false, error = requestRoversUseCase())
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val photoList: List<Photo>? = null,
        val onBackPressed: Boolean = false,
        val error: Error? = null
    )
}