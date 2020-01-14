package com.words.viewmodel

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.words.data.model.DictionaryWord
import com.words.data.repository.DictionaryRepository
import com.words.ui.activity.MainActivity
import io.reactivex.disposables.CompositeDisposable
import java.net.UnknownHostException
import java.util.*

class MainViewModel constructor(private val dictionaryRepository: DictionaryRepository) :
    ViewModel() {
    private val disposable = CompositeDisposable()
    fun getDefinition(word: String) {
        if (word.isEmpty()) {
            loadingState.value = LoadingState.ERROR("Please enter word")
        } else {
            loadingState.value = LoadingState.LOADING
            disposable.add(
                dictionaryRepository.findDefinition(word)
                    .subscribe({
                        lastFetchedTime = Date()
                        if (it.words.isEmpty()) {

                            loadingState.value = LoadingState.ERROR("No Definition found")
                        } else {

                            loadingState.value = LoadingState.SUCCESS(it.words)
                        }
                    }, {
                        lastFetchedTime = Date()

                        it.printStackTrace()
                        loadingState.value = LoadingState.ERROR(
                            when (it) {
                                is UnknownHostException -> "No Network"
                                else -> it.localizedMessage
                            }
                        )


                    })
            )
        }
    }

    var lastFetchedTime: Date? = null

    val loadingState = MutableLiveData<LoadingState>()

    sealed class LoadingState {
        object LOADING : LoadingState()
        data class SUCCESS(val dictionaryWords: List<DictionaryWord>) : LoadingState()
        data class ERROR(val message: String) : LoadingState()
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    fun getActivity(): Class<out Activity> {
        return MainActivity::class.java
    }

    fun sortByUpVotes() {
        if (loadingState.value is LoadingState.SUCCESS) {
            loadingState.value =
                LoadingState.SUCCESS((loadingState.value as LoadingState.SUCCESS).dictionaryWords.sortedBy { it.thumbsUp })
        }
    }

    fun sortByDownVotes() {

        if (loadingState.value is LoadingState.SUCCESS) {
            loadingState.value =
                LoadingState.SUCCESS((loadingState.value as LoadingState.SUCCESS).dictionaryWords.sortedBy { it.thumbsDown })
        }
    }
    //
}