package com.words.data.repository

import com.words.data.remote.DictionaryWordResponse
import io.reactivex.Single

interface DictionaryRepository {
    fun findDefinition(word: String): Single<DictionaryWordResponse>
}