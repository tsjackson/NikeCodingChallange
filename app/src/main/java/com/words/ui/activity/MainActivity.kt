package com.words.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.words.R
import com.words.data.model.DictionaryWord
import com.words.data.remote.WebServices
import com.words.data.repository.DictionaryRepositoryImpl
import com.words.ui.adapter.WordAdapter
import com.words.ui.adapter.listener.WordClickListener
import com.words.ui.dialog.WordDetailDialog
import com.words.viewmodel.MainViewModel
import com.words.viewmodel.factory.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var wordAdapter: WordAdapter
    private val wordClickListener: WordClickListener = object : WordClickListener {
        override fun onClick(dictionaryWord: DictionaryWord) {
            val wordDetailDialog = WordDetailDialog.instance(dictionaryWord)
            wordDetailDialog.show(supportFragmentManager, "WORD_DETAIL")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProviders.of(
            this,
            MainViewModelFactory(DictionaryRepositoryImpl(WebServices.instance))
        )
            .get(MainViewModel::class.java)




        viewModel.loadingState.observe(this, Observer {
            when (it) {
                is MainViewModel.LoadingState.LOADING -> displayProgressbar()
                is MainViewModel.LoadingState.SUCCESS -> displayList(it.dictionaryWords)
                is MainViewModel.LoadingState.ERROR -> displayMessageContainer(it.message)
                else -> displayMessageContainer("Unknown Error")
            }
        })


        btnRetry.setOnClickListener {
            viewModel.getDefinition(etTerm.text.toString())
        }
        btnSearch.setOnClickListener {
            viewModel.getDefinition(etTerm.text.toString())
        }
        btnSortByUpVotes.setOnClickListener {
            viewModel.sortByUpVotes()
        }
        btnSortByDownVotes.setOnClickListener {
            viewModel.sortByDownVotes()
        }

    }

    private fun displayProgressbar() {
        progressbar.visibility = View.VISIBLE
        rvDefinitions.visibility = View.GONE
        llMessageContainer.visibility = View.GONE
    }

    private fun displayMessageContainer(message: String) {
        btnSortByUpVotes.visibility = View.GONE
        btnSortByDownVotes.visibility = View.GONE
        llMessageContainer.visibility = View.VISIBLE
        rvDefinitions.visibility = View.GONE
        progressbar.visibility = View.GONE
        tvMessage.text = message
    }

    private fun displayList(dictionaryWords: List<DictionaryWord>) {
        btnSortByUpVotes.visibility = View.VISIBLE
        btnSortByDownVotes.visibility = View.VISIBLE
        wordAdapter.dictionaryWords.clear()
        wordAdapter.dictionaryWords.addAll(dictionaryWords)
        wordAdapter.notifyDataSetChanged()
        llMessageContainer.visibility = View.GONE
        rvDefinitions.visibility = View.VISIBLE
        progressbar.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        rvDefinitions.layoutManager = LinearLayoutManager(this)
        wordAdapter = WordAdapter(mutableListOf(), wordClickListener)
        rvDefinitions.adapter = wordAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.getDefinition(etTerm.text.toString())
                true
            }
            else -> super.onOptionsItemSelected(item)

        }

    }

}
