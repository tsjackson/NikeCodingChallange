package com.words.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.words.R
import com.words.data.model.DictionaryWord
import kotlinx.android.synthetic.main.dialog_word_detail.*

class WordDetailDialog : DialogFragment() {
    companion object {
        const val ARG_WORD = "arg_word"
        fun instance(dictionaryWord: DictionaryWord): WordDetailDialog {
            val wordDetailDialog = WordDetailDialog()
            val arguments = Bundle()
            arguments.putParcelable(ARG_WORD, dictionaryWord)
            wordDetailDialog.arguments = arguments
            return wordDetailDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_word_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val word = arguments?.getParcelable<DictionaryWord>(ARG_WORD)
        word?.apply {
            tvExample.text = this.example
        }
        btnOk.setOnClickListener { dismiss() }

    }
}