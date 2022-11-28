package kr.or.mrhi.cinemastorage.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.cinemastorage.data.Review
import kr.or.mrhi.cinemastorage.databinding.ItemReviewUserBinding

class UserReviewAdapter(private val reviewList: ArrayList<Review>):RecyclerView.Adapter<UserReviewAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ItemReviewUserBinding) :
        RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}