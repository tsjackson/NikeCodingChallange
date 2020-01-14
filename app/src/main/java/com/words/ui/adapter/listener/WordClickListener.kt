package com.words.ui.adapter.listener

import com.words.data.model.DictionaryWord

interface WordClickListener {
    fun onClick(dictionaryWord: DictionaryWord)
}
