package com.naufal.capstonewasteclassification.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _jumlahSampah = MutableLiveData<Int>()
    val jumlahSampah: LiveData<Int> get() = _jumlahSampah

    init {
        _jumlahSampah.value = 0
    }

    fun tambahSampah() {
        _jumlahSampah.value = (_jumlahSampah.value ?: 0) + 1
    }
}