package com.example.apiconsumerdemo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiconsumerdemo.domain.DemoContent
import com.example.apiconsumerdemo.usecases.GetDetailContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal sealed class DetailUiState {
    object Loading: DetailUiState()
    data class Content(val data: DemoContent) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    private val getDetailContentUseCase: GetDetailContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadContent(id: String) {
        _uiState.tryEmit(DetailUiState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val contentData = getDetailContentUseCase.invoke(id)
            withContext(Dispatchers.Main) {
                _uiState.emit(
                    if (contentData != null) {
                        DetailUiState.Content(contentData)
                    } else {
                        DetailUiState.Error("Content not found!")
                    }
                )
            }
        }
    }

}