package kr.or.mrhi.cinemastorage.view.fragment

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
import kr.or.mrhi.cinemastorage.view.adapter.ListAdapter

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!

    private val cinemaList = listOf<Cinema>()

    private val popularAdapter = ListAdapter(cinemaList)

    private val topRatedAdapter = ListAdapter(cinemaList)

    private val upcomingAdapter = ListAdapter(cinemaList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

    private fun getPopularCinema() {
        CinemaRepository.getPopularCinema(onSuccess = ::onPopularCinemaFetched, onError = ::onError)
    }

    private fun onPopularCinemaFetched(cinema: List<Cinema>) {
        popularAdapter.updateCinema(cinema)
        Log.d("ListFragment", "Cinema : $cinema")
    }

    private fun getTopRatedCinema() {
        CinemaRepository.getTopRatedCinema(
            onSuccess = ::onTopRatedCinemaFetched,
            onError = ::onError
        )
    }

    private fun onTopRatedCinemaFetched(cinema: List<Cinema>) {
        topRatedAdapter.updateCinema(cinema)
        Log.d("ListFragment", "Cinema : $cinema")
    }

    private fun getUpcomingCinema() {
        CinemaRepository.getUpcomingCinema(
            onSuccess = ::onUpcomingCinemaFetched,
            onError = ::onError
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}