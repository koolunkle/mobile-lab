package kr.or.mrhi.cinemastorage.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.or.mrhi.cinemastorage.data.Cinema
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
            setRecyclerView()
        }
        return binding.root
    }

    private fun setRecyclerView() {
        listAdapter = ListAdapter(cinemaList)
        binding.recyclerView.apply {
            adapter = listAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}