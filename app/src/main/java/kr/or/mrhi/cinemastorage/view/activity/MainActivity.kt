package kr.or.mrhi.cinemastorage.view.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.cinemastorage.R
import kr.or.mrhi.cinemastorage.dao.ReviewDAO
import kr.or.mrhi.cinemastorage.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.Review
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

    private lateinit var globalReview: Review

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
        userDAO.databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
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
                R.id.nav_logout -> logout()
                R.id.nav_delete_account -> deleteAccountAlertDialog()
            }
            true
        }
    }

    private fun logout() {
        SharedPreferences.removeToken(applicationContext)
        val intent = Intent(this, IntroActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun deleteAccountAlertDialog() {
        val listner = DialogInterface.OnClickListener { dialogInterface, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> deleteAccount()
                DialogInterface.BUTTON_NEGATIVE -> dialogInterface.dismiss()
            }
        }
        val builder = AlertDialog.Builder(this)
            .setTitle("DELETE ACOUNT")
            .setMessage("Are you sure you want to delete your current account?")
            .setPositiveButton("YES", listner)
            .setNegativeButton("NO", listner)
        builder.show()
    }

    private fun deleteAccount() {
        val userDAO = UserDAO()
        val reviewDAO = ReviewDAO()
        val loginUserKey = SharedPreferences.getToken(applicationContext)
        userDAO.deleteUser(loginUserKey)
        reviewDAO.selectReview()?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    globalReview = data.getValue(Review::class.java)!!
                    if (globalReview.key == loginUserKey) {
                        val reviewKey = globalReview.date.toString()
                        reviewDAO.deleteReview(reviewKey)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("MainActivity", "failed delete data of the account")
            }
        })
        logout()
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

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val LIST_FRAGMENT = 0
        const val REVIEW_FRAGMENT = 1
    }

}