package kr.or.mrhi.cinemastorage.presentation.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.cinemastorage.R
import kr.or.mrhi.cinemastorage.data.dao.ReviewDAO
import kr.or.mrhi.cinemastorage.data.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.model.Review
import kr.or.mrhi.cinemastorage.data.model.User
import kr.or.mrhi.cinemastorage.databinding.ActivityMainBinding
import kr.or.mrhi.cinemastorage.presentation.activity.user.PersonalActivity
import kr.or.mrhi.cinemastorage.presentation.activity.user.UpdateUserinfoActivity
import kr.or.mrhi.cinemastorage.util.SharedPreferences
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var backPressedTime: Long = 0

    private var globalUser: User? = null

    private var loginUser: User? = null

    private var isUser = false

    private lateinit var globalReview: Review

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setNavigationFragment()
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
                if (isUser) setNavigationHeaderView("Nickname : " + loginUser?.nickname!!)
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
                R.id.nav_my_page -> startActivity(
                    Intent(this, PersonalActivity::class.java)
                )
                R.id.nav_edit_profile -> startActivity(
                    Intent(this, UpdateUserinfoActivity::class.java)
                )
                R.id.nav_logout -> setLogout()
                R.id.nav_delete_account -> deleteAccountAlertDialog()
            }
            true
        }
    }

    private fun setLogout() {
        SharedPreferences.removeToken(applicationContext)
        val intent = Intent(this, IntroActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun deleteAccountAlertDialog() {
        val listener = DialogInterface.OnClickListener { dialogInterface, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> deleteAccount()
                DialogInterface.BUTTON_NEGATIVE -> dialogInterface.dismiss()
            }
        }
        val builder = AlertDialog.Builder(this).setTitle("DELETE ACCOUNT")
            .setMessage("Are you sure you want to delete your current account?")
            .setPositiveButton("YES", listener).setNegativeButton("NO", listener)
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
                setToast(error.message)
            }
        })
        setLogout()
    }

    private fun setDrawerLayout() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_drawer_list_24)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setNavigationFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        val drawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START)
        else setExitProcess()
    }

    private fun setExitProcess() {
        if (System.currentTimeMillis() - backPressedTime >= 1500) {
            backPressedTime = System.currentTimeMillis()
            setToast(resources.getString(R.string.toast_back_pressed))
        } else {
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            exitProcess(0)
        }
    }

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}