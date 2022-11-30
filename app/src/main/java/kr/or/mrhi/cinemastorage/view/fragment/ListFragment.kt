package kr.or.mrhi.cinemastorage.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.cinemastorage.data.cinema.Cinema
import kr.or.mrhi.cinemastorage.data.cinema.CinemaRepository
import kr.or.mrhi.cinemastorage.databinding.FragmentListBinding
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_BACKDROP
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_OVERVIEW
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_POSTER
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_RATING
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_RELEASE_DATE
import kr.or.mrhi.cinemastorage.view.activity.ListDetailActivity.Companion.MOVIE_TITLE
import kr.or.mrhi.cinemastorage.view.adapter.ListAdapter

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!

    private val cinemaList = listOf<Cinema>()

    private val popularAdapter = ListAdapter(cinemaList) { cinema -> showCinemaDetail(cinema) }

    private val topRatedAdapter = ListAdapter(cinemaList) { cinema -> showCinemaDetail(cinema) }

    private val upcomingAdapter = ListAdapter(cinemaList) { cinema -> showCinemaDetail(cinema) }

    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.apply {
            getPopularCinema()
            getTopRatedCinema()
            getUpcomingCinema()
            setRecyclerView(recyclerViewPopular, popularAdapter)
            setRecyclerView(recyclerViewTopRated, topRatedAdapter)
            setRecyclerView(recyclerViewUpcoming, upcomingAdapter)
            setVideoView()
        }
        return binding.root
    }

    private fun showCinemaDetail(cinema: Cinema) {
        val intent = Intent(requireContext(), ListDetailActivity::class.java)
        intent.putExtra(MOVIE_BACKDROP, cinema.backdrop)
        intent.putExtra(MOVIE_POSTER, cinema.poster)
        intent.putExtra(MOVIE_TITLE, cinema.title)
        intent.putExtra(MOVIE_RELEASE_DATE, cinema.release)
        intent.putExtra(MOVIE_RATING, cinema.rating)
        intent.putExtra(MOVIE_OVERVIEW, cinema.overview)
        startActivity(intent)
    }

    private fun getPopularCinema() {
        CinemaRepository.getPopularCinema(onSuccess = ::onPopularCinemaFetched, onError = ::onError)
    }

    private fun onPopularCinemaFetched(cinema: List<Cinema>) {
        popularAdapter.updateCinema(cinema)
        Log.d("ListFragment", "Cinema : $cinema")
    }

    private fun getTopRatedCinema() {
        CinemaRepository.getTopRatedCinema(
            onSuccess = ::onTopRatedCinemaFetched, onError = ::onError
        )
    }

    private fun onTopRatedCinemaFetched(cinema: List<Cinema>) {
        topRatedAdapter.updateCinema(cinema)
        Log.d("ListFragment", "Cinema : $cinema")
    }

    private fun getUpcomingCinema() {
        CinemaRepository.getUpcomingCinema(
            onSuccess = ::onUpcomingCinemaFetched, onError = ::onError
        )
    }

    private fun onUpcomingCinemaFetched(cinema: List<Cinema>) {
        upcomingAdapter.updateCinema(cinema)
        Log.d("ListFragment", "Cinema : $cinema")
    }

    private fun onError() {
        Toast.makeText(
            requireContext(), "Please check your internet connection", Toast.LENGTH_SHORT
        ).show()
    }

    private fun setVideoView() {
        binding.videoView.apply {
            setVideoURI(Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))
            requestFocus()
            setOnPreparedListener { start() }
            setOnCompletionListener { start() }
        }
    }

    private fun setRecyclerView(recyclerView: RecyclerView, listAdapter: ListAdapter) {
        recyclerView.apply {
            adapter = listAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}