package es.architectcoders.spaceexplorer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.architectcoders.domain.usecase.GetApodsUseCase
import es.architectcoders.domain.usecase.RequestApodUseCase
import es.architectcoders.domain.usecase.SaveApodFavoriteUseCase
import es.architectcoders.spaceexplorer.common.Error
import es.architectcoders.spaceexplorer.common.toDomain
import es.architectcoders.spaceexplorer.common.toError
import es.architectcoders.spaceexplorer.common.toViewObject
import es.architectcoders.spaceexplorer.model.ApodObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val requestApodUseCase: RequestApodUseCase,
    private val getApodsUseCase: GetApodsUseCase,
    private val saveApodFavoriteUseCase: SaveApodFavoriteUseCase
) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val apodList: List<ApodObject> = emptyList(),
        val navigateTo: ApodObject? = null,
        val onBackPressed: Boolean = false,
        val error: Error? = null
    )

    private val _state = MutableStateFlow(UiState())

    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val error: Error? = requestApodUseCase()?.toError()
            _state.update { uiState -> uiState.copy(loading = false, error = error) }
            getApodsUseCase().collect { apodList ->
                _state.value = _state.value.copy(error = null, apodList = apodList.map { apod -> apod.toViewObject() })
            }
        }
    }

    fun onApodClicked(apod: ApodObject) {
        _state.value = _state.value.copy(navigateTo = apod)
    }

    fun saveApodAsFavourite(apod: ApodObject) {
        viewModelScope.launch {
            val error: Error? = saveApodFavoriteUseCase(apod.toDomain())?.toError()
            _state.value = _state.value.copy(error = error)
        }
    }

    fun onApodNavigationDone() {
        _state.value = _state.value.copy(error = null, navigateTo = null)
    }

    fun onBackPressed() {
        _state.value = _state.value.copy(onBackPressed = true)
    }

    fun retry() {
        viewModelScope.launch {
            _state.value = _state.value.copy(error = null, loading = true)
            _state.update { uiState ->
                uiState.copy(loading = false, error = requestApodUseCase()?.toError())
            }
        }
    }
}