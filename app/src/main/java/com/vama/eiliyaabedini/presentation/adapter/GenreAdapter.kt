package com.vama.eiliyaabedini.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vama.eiliyaabedini.databinding.ListItemGenreBinding
import com.vama.eiliyaabedini.domain.model.Genre

class GenreAdapter : ListAdapter<Genre, RecyclerView.ViewHolder>(GenreDiffCallback()) {

    private var itemClickListener: ((Genre) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GenreViewHolder).bind(getItem(position))
    }

    fun setOnItemClick(clickListener: (Genre) -> Unit) {
        this.itemClickListener = clickListener
    }

    inner class GenreViewHolder(private val binding: ListItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.genreTextView.text = genre.name

            itemView.setOnClickListener {
                itemClickListener?.invoke(genre)
            }
        }
    }
}

class GenreDiffCallback : DiffUtil.ItemCallback<Genre>() {

    override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem == newItem
    }
}
