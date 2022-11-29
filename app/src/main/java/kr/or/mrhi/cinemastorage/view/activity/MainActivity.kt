package kr.or.mrhi.cinemastorage.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import kr.or.mrhi.cinemastorage.R
import kr.or.mrhi.cinemastorage.databinding.ActivityMainBinding
import kr.or.mrhi.cinemastorage.view.adapter.MainAdapter
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null

    private val binding get() = _binding!!

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setViewPager()
            navigationSelected()
        }
    }

    private fun setViewPager() {
        binding.viewpager.apply {
            adapter = MainAdapter(this@MainActivity)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNavigation.menu.getItem(position).isChecked = true
                }
            })
        }
    }

    private fun navigationSelected() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_list -> {
                    binding.viewpager.currentItem = LIST_FRAGMENT
                    true
                }
                else -> {
                    binding.viewpager.currentItem = REVIEW_FRAGMENT
                    true
                }
            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime >= 1500) {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(
                this, resources.getString(R.string.toast_back_pressed), Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            exitProcess(0)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        const val LIST_FRAGMENT = 0
        const val REVIEW_FRAGMENT = 1
    }

}