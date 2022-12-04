package kr.or.mrhi.cinemastorage.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import kr.or.mrhi.cinemastorage.R
import kr.or.mrhi.cinemastorage.databinding.ActivityMainBinding
import kr.or.mrhi.cinemastorage.view.activity.user.PersonalActivity
import kr.or.mrhi.cinemastorage.view.activity.user.UpdateUserinfoActivity
import kr.or.mrhi.cinemastorage.view.adapter.MainAdapter
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var backPressedTime: Long = 0

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setViewPager()
            setBottomNavigationSelected()
            setDrawerLayout()
            setDrawerNavigationSelected()
        }
    }

    private fun setDrawerNavigationSelected() {
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_review -> startActivity(
                    Intent(this, PersonalActivity::class.java)
                )
                R.id.nav_profile_update -> startActivity(
                    Intent(this, UpdateUserinfoActivity::class.java)
                )
            }
            true
        }
    }

    private fun setDrawerLayout() {
        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, R.string.drawer_open, R.string.drawer_close
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
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

    private fun setBottomNavigationSelected() {
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

    companion object {
        const val LIST_FRAGMENT = 0
        const val REVIEW_FRAGMENT = 1
    }

}