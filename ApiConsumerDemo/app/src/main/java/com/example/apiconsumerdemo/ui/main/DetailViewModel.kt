package com.example.apiconsumerdemo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiconsumerdemo.domain.DemoContent
import com.example.apiconsumerdemo.usecases.GetDetailContentUseCse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    private val getDetailContentUseCse: GetDetailContentUseCse
) : ViewModel() {

    private val _detailDataFlow = MutableStateFlow<DemoContent?>(null)
    val detailDataFlow = _detailDataFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        _isLoading.value = true
    }

    fun loadContent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val contentData = getDetailContentUseCse.invoke(id)
            withContext(Dispatchers.Main) {
                _isLoading.value = false
                _detailDataFlow.value = contentData
            }
        }
    }

}