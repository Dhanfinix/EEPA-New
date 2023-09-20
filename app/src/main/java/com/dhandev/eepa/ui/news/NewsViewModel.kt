package com.dhandev.eepa.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhandev.eepa.divkit.DivKitApi
import kotlinx.coroutines.launch
import java.io.IOException

class NewsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    init {
        viewModelScope.launch {
            try {
                val res = DivKitApi.service.getNewsUi()
                _text.value = res
            } catch (e: IOException) {
            }
        }
    }
}
