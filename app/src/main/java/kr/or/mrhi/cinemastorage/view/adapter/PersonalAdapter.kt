package kr.or.mrhi.cinemastorage.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.or.mrhi.cinemastorage.view.activity.review.PersonalDetailActivity
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

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PersonalDetailActivity::class.java)
                intent.putExtra(PERSONAL_REVIEWER, data.reviewer)
                intent.putExtra(PERSONAL_TITLE, data.title)
                intent.putExtra(PERSONAL_DATE, data.date)
                intent.putExtra(PERSONAL_COMMENT, data.comment)
                intent.putExtra(PERSONAL_RATING, data.rating.toFloat())
                intent.putExtra(PERSONAL_POSTER, data.poster)
                intent.putExtra(PERSONAL_BACKDROP, data.backdrop)
                itemView.context.startActivity(intent)
            }
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

    companion object {
        const val PERSONAL_REVIEWER = "extra_reviewer"
        const val PERSONAL_TITLE = "extra_review_title"
        const val PERSONAL_DATE = "extra_review_date"
        const val PERSONAL_COMMENT = "extra_review_comment"
        const val PERSONAL_RATING = "extra_review_rating"
        const val PERSONAL_POSTER = "extra_review_poster"
        const val PERSONAL_BACKDROP = "extra_review_backdrop"
    }
}