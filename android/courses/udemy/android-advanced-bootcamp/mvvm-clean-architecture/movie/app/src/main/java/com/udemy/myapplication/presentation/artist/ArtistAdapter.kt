package com.udemy.myapplication.presentation.artist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.udemy.myapplication.R
import com.udemy.myapplication.data.model.artist.Artist
import com.udemy.myapplication.databinding.ListItemBinding

class ArtistAdapter : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    inner class ArtistViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: Artist) {
            binding.tvTitle.text = artist.name
            binding.tvDescription.text = artist.popularity.toString()

            val posterURL = "https://image.tmdb.org/t/p/w500/" + artist.profilePath
            Glide.with(binding.ivListItem.context).load(posterURL).into(binding.ivListItem)
        }
    }

    private val artistList = ArrayList<Artist>()

    fun setList(artists: List<Artist>) {
        artistList.clear()
        artistList.addAll(artists)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ArtistAdapter.ArtistViewHolder {
        val binding: ListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_item, parent, false
        )
        return ArtistViewHolder(binding)
    }

    override fun getItemCount(): Int = artistList.size

    override fun onBindViewHolder(holder: ArtistAdapter.ArtistViewHolder, position: Int) {
        holder.bind(artistList[position])
    }
}