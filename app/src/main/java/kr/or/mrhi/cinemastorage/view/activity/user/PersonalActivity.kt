package kr.or.mrhi.cinemastorage.view.activity.user

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.cinemastorage.R
import kr.or.mrhi.cinemastorage.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.Review
import kr.or.mrhi.cinemastorage.data.User
import kr.or.mrhi.cinemastorage.databinding.ActivityPersonalBinding
import kr.or.mrhi.cinemastorage.view.adapter.ReviewAdapter

class PersonalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalBinding
    private lateinit var reviewAdapter: ReviewAdapter
    private var reviewList: ArrayList<Review>? = arrayListOf()
    private lateinit var user: User
    lateinit var key: String
    private var isUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("key")) {
            key = intent.getStringExtra("key")!!
            Log.d("intent check", key)
        }

        /*get the profile image from FirebaseStorage*/
        val userDAO = UserDAO()
        val imageName = user.key
        val imgRef = userDAO.storage!!.reference.child("images/${imageName}.jpg")

        /*binding image*/
        imgRef.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                Glide.with(applicationContext).load(it.result).into(binding.ivProfile)
            }
        }
        /*binding nickname*/
        var user: User? = null
        userDAO.databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    user = data.getValue(User::class.java)
                    if (key == user?.key) isUser = true
                }
                if (isUser) {
                    binding.tvNickname.text = user?.nickname
                } else setToast("Nickname or Password does not match")
            }

            override fun onCancelled(error: DatabaseError) {
                setToast(error.message)
            }
        })


        /*해당 유저가 작성한 리뷰 갯수만 가져와야 함
         binding.tvRvCountNo.text = reviewList?.size.toString()*/

        reviewAdapter = ReviewAdapter(reviewList as ArrayList<Review>)
        binding.recyclerView.apply {
            adapter = reviewAdapter
            setHasFixedSize(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> Log.d(" cinamestorage", "PersonalActivity omOptionsItemSelected()")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.review_search, menu)
        val searchMenu = menu?.findItem(R.id.menu_search)
        val searchView = searchMenu?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                reviewList?.clear()

                /*firebase에서 review데이터 검색해서 불러오기*/

//                reviewPersonalAdapter.notifyDataSetChanged()
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}