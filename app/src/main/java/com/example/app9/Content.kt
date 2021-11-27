package com.example.app9

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class Content<T>(liveData: LiveData<List<T>>) : LiveData<List<T>>() {

    private val observer = Observer<List<T>> { list -> value = list }

    init {
        liveData.observeForever(observer)
    }
}