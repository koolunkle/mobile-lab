package kr.or.mrhi.cinemastorage.presentation.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.cinemastorage.BuildConfig
import kr.or.mrhi.cinemastorage.R
import kr.or.mrhi.cinemastorage.data.model.cinema.Cinema
import kr.or.mrhi.cinemastorage.data.repository.CinemaRepository
import kr.or.mrhi.cinemastorage.databinding.FragmentListBinding
import kr.or.mrhi.cinemastorage.presentation.activity.ListDetailActivity
import kr.or.mrhi.cinemastorage.presentation.activity.ListDetailActivity.Companion.MOVIE_BACKDROP
import kr.or.mrhi.cinemastorage.presentation.activity.ListDetailActivity.Companion.MOVIE_OVERVIEW
import kr.or.mrhi.cinemastorage.presentation.activity.ListDetailActivity.Companion.MOVIE_POSTER
import kr.or.mrhi.cinemastorage.presentation.activity.ListDetailActivity.Companion.MOVIE_RATING
import kr.or.mrhi.cinemastorage.presentation.activity.ListDetailActivity.Companion.MOVIE_RELEASE_DATE
import kr.or.mrhi.cinemastorage.presentation.activity.ListDetailActivity.Companion.MOVIE_TITLE
import kr.or.mrhi.cinemastorage.presentation.adapter.ListAdapter

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private val popularAdapter = ListAdapter()

    private val topRatedAdapter = ListAdapter()

    private val upcomingAdapter = ListAdapter()

    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        binding.apply {
            getPopularCinema()
            getTopRatedCinema()
            getUpcomingCinema()
            setRecyclerView(recyclerViewPopular, popularAdapter)
            setRecyclerView(recyclerViewTopRated, topRatedAdapter)
            setRecyclerView(recyclerViewUpcoming, upcomingAdapter)
            showCinemaDetail(popularAdapter)
            showCinemaDetail(topRatedAdapter)
            showCinemaDetail(upcomingAdapter)
            setVideoView()
        }
    }

    private fun showCinemaDetail(listAdapter: ListAdapter) {
        listAdapter.setOnItemClickListener {
            val intent = Intent(activity, ListDetailActivity::class.java)
            intent.putExtra(MOVIE_BACKDROP, it.backdrop)
            intent.putExtra(MOVIE_POSTER, it.poster)
            intent.putExtra(MOVIE_TITLE, it.title)
            intent.putExtra(MOVIE_RELEASE_DATE, it.release)
            intent.putExtra(MOVIE_RATING, it.rating)
            intent.putExtra(MOVIE_OVERVIEW, it.overview)
            startActivity(intent)
        }
    }

    private fun getPopularCinema() {
        CinemaRepository.getPopularCinema(onSuccess = ::onPopularCinemaFetched, onError = ::onError)
    }

    private fun onPopularCinemaFetched(cinema: List<Cinema>) {
        popularAdapter.differ.submitList(cinema)
    }

    private fun getTopRatedCinema() {
        CinemaRepository.getTopRatedCinema(
            onSuccess = ::onTopRatedCinemaFetched, onError = ::onError
        )
    }

    private fun onTopRatedCinemaFetched(cinema: List<Cinema>) {
        topRatedAdapter.differ.submitList(cinema)
    }

    private fun getUpcomingCinema() {
        CinemaRepository.getUpcomingCinema(
            onSuccess = ::onUpcomingCinemaFetched, onError = ::onError
        )
    }

    private fun onUpcomingCinemaFetched(cinema: List<Cinema>) {
        upcomingAdapter.differ.submitList(cinema)
    }

    private fun onError() {
        Toast.makeText(
            activity, "Please check your internet connection", Toast.LENGTH_SHORT
        ).show()
    }

    private fun setVideoView() {
        binding.videoView.apply {
            setVideoURI(Uri.parse(BuildConfig.MAIN_VIDEO))
            requestFocus()
            setOnPreparedListener { start() }
            setOnCompletionListener { start() }
        }
    }

    private fun setRecyclerView(recyclerView: RecyclerView, listAdapter: ListAdapter) {
        recyclerView.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.videoView.apply {
            if (!isPlaying) {
                seekTo(position)
                start()
            }
        }
    }

    override fun onPause() {
        binding.videoView.apply {
            position = currentPosition
            pause()
        }
        super.onPause()
    }

    override fun onStop() {
        binding.videoView.apply { if (isPlaying) stopPlayback() }
        super.onStop()
    }

}