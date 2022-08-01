package com.vama.eiliyaabedini.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vama.eiliyaabedini.databinding.ListItemAlbumBinding
import com.vama.eiliyaabedini.domain.model.Album

class MusicAlbumAdapter : ListAdapter<Album, RecyclerView.ViewHolder>(AlbumDiffCallback()) {

    private var itemClickListener: ((Album) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MusicAlbumViewHolder).bind(getItem(position))
    }

    fun setOnItemClick(clickListener: (Album) -> Unit) {
        this.itemClickListener = clickListener
    }

    inner class MusicAlbumViewHolder(private val binding: ListItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.albumNameTextView.text = album.name
            binding.artistNameTextView.text = album.artist

            Picasso.get().load(album.thumbnail).into(binding.avatarImageView)

            itemView.setOnClickListener {
                itemClickListener?.invoke(album)
            }
        }
    }
}

class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {

    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }
}
