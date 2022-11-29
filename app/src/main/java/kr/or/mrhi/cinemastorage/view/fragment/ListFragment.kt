package kr.or.mrhi.cinemastorage.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kr.or.mrhi.cinemastorage.data.Cinema
import kr.or.mrhi.cinemastorage.data.CinemaRepository
import kr.or.mrhi.cinemastorage.databinding.FragmentListBinding
import kr.or.mrhi.cinemastorage.view.adapter.ListAdapter

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!

    private val cinemaList = ArrayList<Cinema>()

    private lateinit var listAdapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.apply {
            getPopularCinema()
            setRecyclerView()
        }
        return binding.root
    }

    private fun getPopularCinema() {
        CinemaRepository.getPopularCinema(onSuccess = ::onPopularCinemaFetched, onError = ::onError)
    }

    private fun onPopularCinemaFetched(cinema: List<Cinema>) {
        listAdapter.updateCinema(cinema)
        Log.d("ListFragment", "Cinema : $cinema")
    }

    private fun onError() {
        Toast.makeText(
            requireContext(), "Please check your internet connection", Toast.LENGTH_SHORT
        ).show()
    }

    private fun setRecyclerView() {
        listAdapter = ListAdapter(cinemaList)
        binding.recyclerView.apply {
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