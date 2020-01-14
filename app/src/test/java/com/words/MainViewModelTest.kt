package com.words

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.words.data.model.DictionaryWord
import com.words.data.remote.DictionaryWordResponse
import com.words.data.repository.DictionaryRepositoryImpl
import com.words.ui.activity.MainActivity
import com.words.viewmodel.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import java.net.UnknownHostException


@RunWith(BlockJUnit4ClassRunner::class)
class MainViewModelTest {


    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var dictionaryRepositoryImpl: DictionaryRepositoryImpl


    lateinit var mainViewModel: MainViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mainViewModel = MainViewModel(dictionaryRepositoryImpl)
    }

    @Test
    fun findDefinitionShouldReturnWords() {

        //When
        val words = listOf(
            DictionaryWord("Demo 1", "Demo 1", "Demo 1", 11, "Demo 1", "Demo 1", "Example", 1, 1),
            DictionaryWord("Demo ", "Demo ", "Demo ", 11, "Demo 1", "Demo 1", "Example", 1, 1)
        )
        every { dictionaryRepositoryImpl.findDefinition(any()) } returns (Single.just(
            DictionaryWordResponse(words)
        ))

        //Here in expected output we have removed duplicate entry and based on title sorting
        //the last entry should come first


        //Then

        mainViewModel.getDefinition("hello")


        //Verify
        Assert.assertEquals(
            words,
            (mainViewModel.loadingState.value as MainViewModel.LoadingState.SUCCESS).dictionaryWords
        )
    }

    @Test
    fun findDefinition_successEmptyList() {

        //When
        every { dictionaryRepositoryImpl.findDefinition(any()) } returns (Single.just(
            DictionaryWordResponse(emptyList())
        ))


        //Then
        mainViewModel.getDefinition("hello")

        //Verify
        Assert.assertEquals(
            "No Definition found",
            (mainViewModel.loadingState.value as MainViewModel.LoadingState.ERROR).message
        )
    }


    @Test
    fun findDefinition_networkError() {

        every { dictionaryRepositoryImpl.findDefinition(any()) } returns (Single.error(
            UnknownHostException("Abc")
        ))

        mainViewModel.getDefinition("hello")
        Assert.assertEquals(
            "No Network",
            (mainViewModel.loadingState.value as MainViewModel.LoadingState.ERROR).message
        )
    }


    @Test
    fun findDefinition_otherError() {
        every { dictionaryRepositoryImpl.findDefinition(any()) } returns Single.error(
            RuntimeException("Abc")
        )

        mainViewModel.getDefinition("hello")

        Assert.assertEquals(
            "Abc",
            (mainViewModel.loadingState.value as MainViewModel.LoadingState.ERROR).message
        )
    }

    @Test
    fun getActivityTest() {
        val activityClass = mainViewModel.getActivity()
        assertTrue(activityClass == MainActivity::class.java)
    }
}