package kr.or.mrhi.cinemastorage.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kr.or.mrhi.cinemastorage.databinding.ActivityListDetailBinding

class ListDetailActivity : AppCompatActivity() {

    private var _binding: ActivityListDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            getCinemaDetail()
            moveToReview()
        }
    }

    private fun moveToReview() {
        binding.btnReview.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getCinemaDetail() {
        val extras = intent.extras
        if (extras != null) setCinemaDetail(extras)
        else finish()
    }

    private fun setCinemaDetail(extras: Bundle) {
        extras.getString(MOVIE_BACKDROP)?.let { backdrop ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w1280$backdrop")
                .transform(CenterCrop())
                .into(binding.ivBackdrop)
        }
        extras.getString(MOVIE_POSTER)?.let { poster ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342$poster")
                .transform(CenterCrop())
                .into(binding.ivPoster)
        }
        binding.tvTitle.text = extras.getString(MOVIE_TITLE, "")
        binding.tvReleaseDate.text = extras.getString(MOVIE_RELEASE_DATE, "")
        binding.ratingBar.rating = extras.getFloat(MOVIE_RATING, 0f) / 2
        binding.tvOverview.text = extras.getString(MOVIE_OVERVIEW, "")
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        const val MOVIE_BACKDROP = "extra_movie_backdrop"
        const val MOVIE_POSTER = "extra_movie_poster"
        const val MOVIE_TITLE = "extra_movie_title"
        const val MOVIE_RELEASE_DATE = "extra_movie_release_date"
        const val MOVIE_RATING = "extra_movie_rating"
        const val MOVIE_OVERVIEW = "extra_movie_overview"
    }

}