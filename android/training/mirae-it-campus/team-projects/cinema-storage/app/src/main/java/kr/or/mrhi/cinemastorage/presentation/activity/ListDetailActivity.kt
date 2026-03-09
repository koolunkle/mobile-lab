package kr.or.mrhi.cinemastorage.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kr.or.mrhi.cinemastorage.BuildConfig
import kr.or.mrhi.cinemastorage.databinding.ActivityListDetailBinding

class ListDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            getCinemaDetail()
            moveToReview()
        }
    }

    private fun moveToReview() {
        binding.btnReview.setOnClickListener {
            val extras = intent.extras
            val intent = Intent(this, WriteActivity::class.java)
            intent.putExtra(MOVIE_BACKDROP, extras?.getString(MOVIE_BACKDROP))
            intent.putExtra(MOVIE_POSTER, extras?.getString(MOVIE_POSTER))
            intent.putExtra(MOVIE_TITLE, extras?.getString(MOVIE_TITLE))
            intent.putExtra(MOVIE_RELEASE_DATE, extras?.getString(MOVIE_RELEASE_DATE))
            intent.putExtra(MOVIE_RATING, extras?.getFloat(MOVIE_RATING))
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
            Glide.with(this).load(BuildConfig.TMDB_BACKDROP_URL + backdrop).transform(CenterCrop())
                .into(binding.ivBackdrop)
        }
        extras.getString(MOVIE_POSTER)?.let { poster ->
            Glide.with(this).load(BuildConfig.TMDB_POSTER_URL + poster).transform(CenterCrop())
                .into(binding.ivPoster)
        }
        binding.tvTitle.text = extras.getString(MOVIE_TITLE, "")
        binding.tvReleaseDate.text = extras.getString(MOVIE_RELEASE_DATE, "")
        binding.ratingBar.rating = extras.getFloat(MOVIE_RATING, 0f) / 2
        binding.tvOverview.text = extras.getString(MOVIE_OVERVIEW, "")
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