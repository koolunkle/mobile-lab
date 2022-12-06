package kr.or.mrhi.cinemastorage.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.cinemastorage.R
import kr.or.mrhi.cinemastorage.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.User
import kr.or.mrhi.cinemastorage.databinding.ActivityMainBinding
import kr.or.mrhi.cinemastorage.util.SharedPreferences
import kr.or.mrhi.cinemastorage.view.activity.user.PersonalActivity
import kr.or.mrhi.cinemastorage.view.activity.user.UpdateUserinfoActivity
import kr.or.mrhi.cinemastorage.view.adapter.MainAdapter
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var backPressedTime: Long = 0

    private lateinit var toggle: ActionBarDrawerToggle

    private var globalUser: User? = null

    private var loginUser: User? = null

    private var isUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setViewPager()
            setBottomNavigationSelected()
            setDrawerLayout()
            setDrawerNavigationSelected()
            getUserNickname()
        }
    }

    private fun getUserNickname() {
        val userDAO = UserDAO()
        userDAO.databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    globalUser = data.getValue(User::class.java)
                    if (globalUser?.key == SharedPreferences.getToken(applicationContext)) {
                        loginUser =
                            User(globalUser?.key!!, globalUser?.nickname, globalUser?.password)
                        isUser = true
                    }
                }
                if (isUser) setNavigationHeaderView(loginUser?.nickname!!)
            }

            override fun onCancelled(error: DatabaseError) {
                setToast(error.message)
            }
        })
    }

    private fun setNavigationHeaderView(nickname: String) {
        val navigation = binding.navigationView
        val header = navigation.getHeaderView(0)
        val headerValue = header.findViewById<TextView>(R.id.tv_nickname)
        headerValue.text = nickname
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
        val drawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
        else if (System.currentTimeMillis() - backPressedTime >= 1500) {
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

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val LIST_FRAGMENT = 0
        const val REVIEW_FRAGMENT = 1
    }

}