package com.words.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.words.data.repository.DictionaryRepository
import com.words.viewmodel.MainViewModel

class MainViewModelFactory constructor(private val dictionaryRepository: DictionaryRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(dictionaryRepository) as T
    }

}