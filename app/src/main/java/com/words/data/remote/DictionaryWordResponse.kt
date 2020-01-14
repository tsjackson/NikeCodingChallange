package com.words.data.remote

import com.google.gson.annotations.SerializedName
import com.words.data.model.DictionaryWord


data class DictionaryWordResponse(
    @SerializedName("list")
    val words: List<DictionaryWord>
)
