package com.example.apiconsumerdemo.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiconsumerdemo.data.ContentError
import com.example.apiconsumerdemo.domain.DemoContent
import com.example.apiconsumerdemo.usecases.GetLocalListContentUseCase
import com.example.apiconsumerdemo.usecases.GetRemoteListContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal sealed class ListUiState {
    object Loading: ListUiState()
    data class Content(val data: List<DemoContent>) : ListUiState()
}

@HiltViewModel
internal class ListViewModel @Inject constructor(
    private val getRemoteListContentUseCase: GetRemoteListContentUseCase,
    private val getLocalListContentUseCase: GetLocalListContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var localDataJob: Job? = null
    private var remoteDataJob: Job? = null

    fun loadLocalData() {
        localDataJob?.cancel()
        localDataJob = viewModelScope.launch(Dispatchers.IO) {
            val contentData = getLocalListContentUseCase.invoke()
            withContext(Dispatchers.Main) {
                _uiState.emit(ListUiState.Content(contentData))
            }
        }
    }

    fun reloadData() {
        _uiState.tryEmit(ListUiState.Loading)
        remoteDataJob?.cancel()
        remoteDataJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val contentData = getRemoteListContentUseCase.invoke()
                if (localDataJob?.isActive == true) localDataJob?.cancel()
                withContext(Dispatchers.Main) {
                    _uiState.emit(ListUiState.Content(contentData))
                }
            } catch (e: Exception) {
                Log.e(this.toString(), ContentError.NoContentFound(e).toString())
            }
        }
    }

}