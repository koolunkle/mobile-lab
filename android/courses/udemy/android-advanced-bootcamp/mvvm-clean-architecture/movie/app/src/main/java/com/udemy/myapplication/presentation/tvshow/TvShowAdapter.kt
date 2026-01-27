package com.udemy.myapplication.presentation.tvshow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.udemy.myapplication.R
import com.udemy.myapplication.data.model.tvshow.TvShow
import com.udemy.myapplication.databinding.ListItemBinding

class TvShowAdapter : RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder>() {

    inner class TvShowViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShow) {
            binding.tvTitle.text = tvShow.name
            binding.tvDescription.text = tvShow.overview

            val posterURL = "https://image.tmdb.org/t/p/w500/" + tvShow.posterPath
            Glide.with(binding.ivListItem.context).load(posterURL).into(binding.ivListItem)
        }
    }

    private val tvShowList = ArrayList<TvShow>()

    fun setList(tvShows: List<TvShow>) {
        tvShowList.clear()
        tvShowList.addAll(tvShows)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TvShowAdapter.TvShowViewHolder {
        val binding: ListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_item, parent, false
        )
        return TvShowViewHolder(binding)
    }

    override fun getItemCount(): Int = tvShowList.size

    override fun onBindViewHolder(holder: TvShowAdapter.TvShowViewHolder, position: Int) {
        holder.bind(tvShowList[position])
    }
}