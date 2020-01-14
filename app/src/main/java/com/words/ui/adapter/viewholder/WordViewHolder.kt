package com.words.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.words.data.model.DictionaryWord
import kotlinx.android.synthetic.main.view_holder_word.view.*

class WordViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    fun bindItem(dictionaryWord: DictionaryWord) {
        itemView.tvDefinition.text = dictionaryWord.definition
        itemView.tvUpVote.text = dictionaryWord.thumbsUp.toString()
        itemView.tvDownVote.text = dictionaryWord.thumbsDown.toString()
    }
}