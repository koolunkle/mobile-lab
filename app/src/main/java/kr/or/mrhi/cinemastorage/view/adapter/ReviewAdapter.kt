package kr.or.mrhi.cinemastorage.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.cinemastorage.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.Review
import kr.or.mrhi.cinemastorage.databinding.AdapterReviewBinding
import kr.or.mrhi.cinemastorage.view.activity.review.ReviewDetailActivity

class ReviewAdapter(private val reviewList: ArrayList<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(private val binding: AdapterReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Review) {
            val userDAO = UserDAO()
            val reviewKey = data.key
            val imageReference = userDAO.storage?.reference?.child("images/${reviewKey}.jpg")

            imageReference?.downloadUrl?.addOnCompleteListener {
                if (it.isSuccessful) Glide.with(itemView).load(it.result).into(binding.ivProfile)
            }

            binding.tvReviewerContent.text = data.reviewer
            binding.tvTitle.text = data.title
            binding.tvDate.text = data.date
            binding.tvComment.text = data.comment
            binding.ratingBar.rating = data.rating?.toFloat()!!
            Glide.with(itemView.context).load(data.poster).into(binding.ivPoster)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ReviewDetailActivity::class.java)
                intent.putExtra(REVIEWER, data.reviewer)
                intent.putExtra(REVIEW_TITLE, data.title)
                intent.putExtra(REVIEW_DATE, data.date)
                intent.putExtra(REVIEW_COMMENT, data.comment)
                intent.putExtra(REVIEW_RATING, data.rating.toFloat())
                intent.putExtra(REVIEW_POSTER, data.poster)
                intent.putExtra(REVIEW_BACKDROP, data.backdrop)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding =
            AdapterReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

    override fun getItemCount(): Int = reviewList.size

    companion object {
        const val REVIEWER = "extra_reviewer"
        const val REVIEW_TITLE = "extra_review_title"
        const val REVIEW_DATE = "extra_review_date"
        const val REVIEW_COMMENT = "extra_review_comment"
        const val REVIEW_RATING = "extra_review_rating"
        const val REVIEW_POSTER = "extra_review_poster"
        const val REVIEW_BACKDROP = "extra_review_backdrop"
    }
}