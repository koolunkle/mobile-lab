package kr.or.mrhi.cinemastorage.view.activity.user

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.cinemastorage.dao.ReviewDAO
import kr.or.mrhi.cinemastorage.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.Review
import kr.or.mrhi.cinemastorage.data.User
import kr.or.mrhi.cinemastorage.databinding.ActivityPersonalBinding
import kr.or.mrhi.cinemastorage.util.SharedPreferences
import kr.or.mrhi.cinemastorage.view.adapter.PersonalAdapter
import kr.or.mrhi.cinemastorage.view.decoration.Decoration

class PersonalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalBinding

    private var reviewList: ArrayList<Review> = arrayListOf()

    private lateinit var personalAdapter: PersonalAdapter

    private lateinit var globalReview: Review

    private lateinit var localReview: Review

    private var globalUser: User? = null

    private var loginUser: User? = null

    private var isUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setUserReview()
            setUserInfo()
            setRecyclerView()
        }
    }

    private fun setUserInfo() {
        val userDAO = UserDAO()

        userDAO.databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    globalUser = data.getValue(User::class.java)
                    if (globalUser?.key == SharedPreferences.getToken(applicationContext)) {
                        loginUser =
                            User(globalUser?.key!!, globalUser?.nickname, globalUser?.password)
                        isUser = true
                    }
                }
                if (isUser) {
                    binding.tvNickname.text = loginUser?.nickname
                    binding.tvReviewCountNo.text = reviewList.size.toString()

                    val imageReference =
                        userDAO.storage?.reference?.child("images/${loginUser?.key}.jpg")

                    imageReference?.downloadUrl?.addOnCompleteListener {
                        if (it.isSuccessful) Glide.with(applicationContext).load(it.result)
                            .into(binding.ivProfile)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                setToast(error.message)
            }
        })
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
            addItemDecoration(Decoration())
        }
    }

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}