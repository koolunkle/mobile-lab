package kr.or.mrhi.cinemastorage.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kr.or.mrhi.cinemastorage.dao.ReviewDAO
import kr.or.mrhi.cinemastorage.data.Review
import kr.or.mrhi.cinemastorage.databinding.ActivityWriteBinding
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_BACKDROP
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_POSTER
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_RATING
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_RELEASE_DATE
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_TITLE
import java.text.SimpleDateFormat
import java.util.*

class WriteActivity : AppCompatActivity() {

    private var _binding: ActivityWriteBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setCinemaReview()
            setCinemaInfo()
        }
    }

    private fun setCinemaInfo() {
        val extras = intent.extras
        extras?.getString(MOVIE_BACKDROP)?.let { backdrop ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w1280$backdrop")
                .transform(CenterCrop())
                .into(binding.ivBackdrop)
        }
        extras?.getString(MOVIE_POSTER)?.let { poster ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342$poster")
                .transform(CenterCrop())
                .into(binding.ivPoster)
        }
        binding.tvCinemaTitle.text = extras?.getString(MOVIE_TITLE, "")
        binding.tvReleaseDate.text = extras?.getString(MOVIE_RELEASE_DATE, "")
        binding.ratingBarCinema.rating = extras?.getFloat(MOVIE_RATING, 0f)?.div(2)!!
    }

    private fun setCinemaReview() {
        binding.btnWrite.setOnClickListener {
            val title = binding.etTitle.text
            val date = currentDate()
            val comment = binding.etComment.text
            val rating = binding.ratingBar.rating

            val reviewDAO = ReviewDAO()
            val reviewKey = reviewDAO.databaseReference?.push()?.key
            val review =
                Review(title.toString(), date, comment.toString(), rating.toString())

            if (title.isBlank() || comment.isBlank()) {
                setToast("Please enter your title, image and comment at all")
                return@setOnClickListener
            } else {
                reviewDAO.databaseReference?.child(reviewKey.toString())?.setValue(review)?.apply {
                    addOnSuccessListener { setToast("Success to insert data") }
                    addOnFailureListener { setToast("Failed to insert data") }
                }
            }
        }
    }

    private fun currentDate(): String {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }

    private fun setToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}