package com.nokopi.nfctest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _nfcData: MutableLiveData<String> =
        MutableLiveData<String>().also { mutableLiveData ->
            mutableLiveData.value = "ready"
        }
    val nfcData: LiveData<String>
        get() = _nfcData

    fun updateNFCData(data: String){
        _nfcData.value = data
    }
}