package kr.or.mrhi.cinemastorage.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.cinemastorage.R
import kr.or.mrhi.cinemastorage.data.dao.ReviewDAO
import kr.or.mrhi.cinemastorage.data.model.Review
import kr.or.mrhi.cinemastorage.databinding.FragmentReviewBinding
import kr.or.mrhi.cinemastorage.presentation.adapter.ReviewAdapter

class ReviewFragment : Fragment() {

    private lateinit var binding: FragmentReviewBinding

    private var reviewList = ArrayList<Review>()

    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReviewBinding.bind(view)
        binding.apply {
            getReviewList()
            setRecyclerView()
        }
    }

    private fun getReviewList() {
        val reviewDAO = ReviewDAO()
        reviewDAO.databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                reviewList.clear()
                for (data in snapshot.children) {
                    val review = data.getValue(Review::class.java)
                    if (review != null) reviewList.add(review)
                }
                reviewAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setRecyclerView() {
        reviewAdapter = ReviewAdapter(reviewList)
        binding.recyclerView.apply {
            adapter = reviewAdapter
            setHasFixedSize(true)
        }
    }

}