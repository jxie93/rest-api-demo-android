package com.example.apiconsumerdemo.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiconsumerdemo.data.ContentRepo
import com.example.apiconsumerdemo.domain.DemoContent
import com.example.apiconsumerdemo.usecases.GetListContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal sealed class ListUiState {
    object Loading: ListUiState()
    data class Content(val data: List<DemoContent>) : ListUiState()
}

@HiltViewModel
internal class ListViewModel @Inject constructor(
    private val getListContentUseCase: GetListContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var remoteDataJob: Job? = null

    fun reloadData() {
        _uiState.tryEmit(ListUiState.Loading)
        remoteDataJob?.cancel()
        remoteDataJob = viewModelScope.launch(Dispatchers.IO) {
            val contentData = getListContentUseCase.invoke()
            withContext(Dispatchers.Main) {
                _uiState.emit(ListUiState.Content(contentData))
            }
        }
    }

}