package kr.or.mrhi.cinemastorage.view.activity.review

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kr.or.mrhi.cinemastorage.BuildConfig
import kr.or.mrhi.cinemastorage.databinding.ActivityReviewDetailBinding
import kr.or.mrhi.cinemastorage.view.adapter.ReviewAdapter.Companion.REVIEWER
import kr.or.mrhi.cinemastorage.view.adapter.ReviewAdapter.Companion.REVIEW_BACKDROP
import kr.or.mrhi.cinemastorage.view.adapter.ReviewAdapter.Companion.REVIEW_COMMENT
import kr.or.mrhi.cinemastorage.view.adapter.ReviewAdapter.Companion.REVIEW_DATE
import kr.or.mrhi.cinemastorage.view.adapter.ReviewAdapter.Companion.REVIEW_POSTER
import kr.or.mrhi.cinemastorage.view.adapter.ReviewAdapter.Companion.REVIEW_RATING
import kr.or.mrhi.cinemastorage.view.adapter.ReviewAdapter.Companion.REVIEW_TITLE

class ReviewDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            getReviewDetail()
            returnTo()
        }
    }

    private fun returnTo() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getReviewDetail() {
        val extras = intent.extras
        if (extras != null) setReviewDetail(extras)
        else finish()
    }

    private fun setReviewDetail(extras: Bundle) {
        extras.getString(REVIEW_POSTER)?.let { poster ->
            Glide.with(this).load(BuildConfig.TMDB_POSTER_URL + poster).transform(CenterCrop())
                .into(binding.ivPoster)
        }
        extras.getString(REVIEW_BACKDROP)?.let { backdrop ->
            Glide.with(this).load(BuildConfig.TMDB_BACKDROP_URL + backdrop).transform(CenterCrop())
                .into(binding.ivBackdrop)
        }
        binding.tvReviewerContent.text = extras.getString(REVIEWER, "")
        binding.tvDateContent.text = extras.getString(REVIEW_DATE, "")
        binding.tvTitleContent.text = extras.getString(REVIEW_TITLE, "")
        binding.tvCommentContent.text = extras.getString(REVIEW_COMMENT, "")
        binding.ratingBar.rating = extras.getFloat(REVIEW_RATING, 0f)
    }

}