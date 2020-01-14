package com.words.data.repository

import com.words.data.model.DictionaryWord
import com.words.data.remote.DictionaryWordResponse
import com.words.data.remote.WebServices
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class DictionaryRepositoryImpl(private val webServices: WebServices) : DictionaryRepository {
    override fun findDefinition(word: String): Single<DictionaryWordResponse> {
        return webServices.findDefinition(word)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    }
}