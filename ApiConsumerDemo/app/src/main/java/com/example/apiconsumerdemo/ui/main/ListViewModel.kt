package com.example.apiconsumerdemo.ui.main

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

@HiltViewModel
internal class ListViewModel @Inject constructor(
    private val getListContentUseCase: GetListContentUseCase
) : ViewModel() {

    private val _listDataFlow = MutableStateFlow<List<DemoContent>>(emptyList())
    val listDataFlow = _listDataFlow.asStateFlow()

    //TODO to ui state
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var remoteDataJob: Job? = null

    init {
        reloadData()
    }

    fun reloadData() {
        _isLoading.value = true
        remoteDataJob?.cancel()
        remoteDataJob = viewModelScope.launch(Dispatchers.IO) {
            val contentData = getListContentUseCase.invoke()
            withContext(Dispatchers.Main) {
                _isLoading.value = false
                _listDataFlow.value = contentData
            }
        }
    }

}