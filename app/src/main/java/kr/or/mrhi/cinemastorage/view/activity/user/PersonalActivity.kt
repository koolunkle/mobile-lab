package kr.or.mrhi.cinemastorage.view.activity.user

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.cinemastorage.dao.ReviewDAO
import kr.or.mrhi.cinemastorage.data.Review
import kr.or.mrhi.cinemastorage.databinding.ActivityPersonalBinding
import kr.or.mrhi.cinemastorage.util.SharedPreferences
import kr.or.mrhi.cinemastorage.view.adapter.PersonalAdapter

class PersonalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalBinding

    private var reviewList: ArrayList<Review> = arrayListOf()

    private lateinit var personalAdapter: PersonalAdapter

    private lateinit var globalReview: Review

    private lateinit var localReview: Review

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setUserReview()
            setRecyclerView()
        }
    }

    private fun setUserReview() {
        val reviewDAO = ReviewDAO()
        reviewDAO.databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    globalReview = data.getValue(Review::class.java)!!
                    if (globalReview.key == SharedPreferences.getToken(applicationContext)) {
                        localReview = Review(
                            globalReview.key,
                            globalReview.reviewer,
                            globalReview.title,
                            globalReview.date,
                            globalReview.comment,
                            globalReview.rating,
                            globalReview.poster,
                            globalReview.backdrop
                        )
                        reviewList.add(localReview)
                    }
                }
                personalAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                setToast(error.message)
            }
        })
    }

    private fun setRecyclerView() {
        personalAdapter = PersonalAdapter(reviewList)
        binding.recyclerView.apply {
            adapter = personalAdapter
            setHasFixedSize(true)
        }
    }

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}