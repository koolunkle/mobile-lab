package kr.or.mrhi.cinemastorage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.cinemastorage.data.Review
import kr.or.mrhi.cinemastorage.databinding.AdapterReviewPersonalBinding

class ReviewPersonalAdapter(private val reviewList: ArrayList<Review>) :
    RecyclerView.Adapter<ReviewPersonalAdapter.ReviewViewHolder>() {
    inner class ReviewViewHolder(private val binding: AdapterReviewPersonalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Review) {
            Glide.with(itemView.context).load(data.thumbnail).into(binding.ivPoster)
            binding.tvTitle.text = data.title
            binding.tvDate.text = data.date
            binding.tvComment.text = data.comment
            binding.ratingBar.rating = data.rating.toFloat()

            /*아이템 클릭하면 어디로 가나요
             binding.root.setOnClickListener{
                 val intent = Intent(itemView.context, ::class.java)
                intent.putExtra("", adapterPosition)
                 binding.root.context.startActivity(intent)
             }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding =
            AdapterReviewPersonalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    override fun getItemCount(): Int = reviewList.size

}