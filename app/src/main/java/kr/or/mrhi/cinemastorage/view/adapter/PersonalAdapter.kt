package kr.or.mrhi.cinemastorage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.cinemastorage.data.Review
import kr.or.mrhi.cinemastorage.databinding.AdapterPersonalBinding

class PersonalAdapter(private val reviewList: ArrayList<Review>) :
    RecyclerView.Adapter<PersonalAdapter.PersonalViewHolder>() {

    inner class PersonalViewHolder(private val binding: AdapterPersonalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Review) {
            binding.tvTitle.text = data.title
            binding.tvDate.text = data.date
            binding.tvComment.text = data.comment
            binding.ratingBar.rating = data.rating?.toFloat()!!
            Glide.with(itemView.context).load(data.poster).into(binding.ivPoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalViewHolder {
        val binding =
            AdapterPersonalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonalViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    override fun getItemCount(): Int = reviewList.size
}