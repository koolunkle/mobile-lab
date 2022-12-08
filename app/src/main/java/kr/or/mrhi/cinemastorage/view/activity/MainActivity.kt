package kr.or.mrhi.cinemastorage.view.activity

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

    private var globalUser: User? = null

    private var loginUser: User? = null

    private var isUser = false

    private lateinit var globalReview: Review

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
    /*로그인 할때 sharedPreference에 저장해둔 key값으로 DB에서 유저 데이터를 가져옴*/
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

    /*getHeaderView를 사용해 닉네임을 header의 textview에 적용*/
    private fun setNavigationHeaderView(nickname: String) {
        val navigation = binding.navigationView
        val header = navigation.getHeaderView(0)
        val headerValue = header.findViewById<TextView>(R.id.tv_nickname)
        headerValue.text = nickname
    }

    /*navigation메뉴 선택 이벤트 설정*/
    private fun setDrawerNavigationSelected() {
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_review -> startActivity(
                    Intent(this, PersonalActivity::class.java)
                )
                R.id.nav_profile_update -> startActivity(
                    Intent(this, UpdateUserinfoActivity::class.java)
                )
                R.id.nav_logout -> setLogout()
                R.id.nav_delete_account -> deleteAccountAlertDialog()
            }
            true
        }
    }

    /*로그아웃시 sharedpreferences에 저장된 값을 지우고 인트로 페이지로 이동하면서
    * 유저가 앱을 사용하면서 쌓인 인텐트를 정리함*/
    private fun setLogout() {
        SharedPreferences.removeToken(applicationContext)
        val intent = Intent(this, IntroActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    /*회원탈퇴 다이얼로그*/
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

    /*회원탈퇴(계정삭제)시 그간 유저가 작성한 리뷰, 유저정보를 firebase에서 삭제 하고
    * logout함수를 불러 인텐트로 이동,sharedpreference 데이터 지우기, 액티비티 정리 한다.*/
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

    /*drawerlayout 설정, 툴바의 아이콘을 클릭해도 열리도록 함 */
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

    companion object {
        const val LIST_FRAGMENT = 0
        const val REVIEW_FRAGMENT = 1
    }

}