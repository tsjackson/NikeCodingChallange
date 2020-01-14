package com.words.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.words.R
import com.words.data.model.DictionaryWord
import com.words.ui.adapter.listener.WordClickListener
import com.words.ui.adapter.viewholder.WordViewHolder

class WordAdapter constructor(val dictionaryWords: MutableList<DictionaryWord>, private val wordClickListener: WordClickListener) :
    RecyclerView.Adapter<WordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        WordViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_holder_word, parent, false))

    override fun getItemCount() = dictionaryWords.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bindItem(dictionaryWords[position])
        holder.itemView.setOnClickListener {
            wordClickListener.onClick(dictionaryWords[holder.adapterPosition])
        }
    }
}
