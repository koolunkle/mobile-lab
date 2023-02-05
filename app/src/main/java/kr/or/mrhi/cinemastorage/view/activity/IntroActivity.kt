package kr.or.mrhi.cinemastorage.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.cinemastorage.R
import kr.or.mrhi.cinemastorage.databinding.ActivityIntroBinding
import kr.or.mrhi.cinemastorage.view.activity.account.LoginActivity
import kr.or.mrhi.cinemastorage.view.activity.account.SignUpActivity
import kr.or.mrhi.cinemastorage.view.adapter.IntroAdapter

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    private var backPressedTime: Long = 0

    var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setViewPager()
            setUpEvents()
        }
    }

    inner class PagerRunnable : Runnable {
        private val handler = Handler(Looper.getMainLooper()) {
            setPage()
            true
        }

        private fun setPage() {
            if (currentPosition == END_BANNER) currentPosition = START_BANNER
            binding.viewpager.setCurrentItem(currentPosition, true)
            currentPosition += 1
        }

        override fun run() {
            while (true) {
                Thread.sleep(1000)
                handler.sendEmptyMessage(0)
            }
        }
    }

    private fun setViewPager() {
        val adapter = IntroAdapter(this)
        binding.viewpager.adapter = adapter

        val thread = Thread(PagerRunnable())
        thread.start()

        binding.dotsIndicator.setViewPager(binding.viewpager)
    }

    private fun setUpEvents() {
        val onClickListener = View.OnClickListener {
            when (it) {
                binding.btnLogin -> startActivity(Intent(this, LoginActivity::class.java))
                binding.btnSignUp -> startActivity(Intent(this, SignUpActivity::class.java))
                binding.btnLater -> setExit()
            }
        }
        binding.btnLogin.setOnClickListener(onClickListener)
        binding.btnSignUp.setOnClickListener(onClickListener)
        binding.btnLater.setOnClickListener(onClickListener)
    }

    private fun setExit() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(this.getString(R.string.dialog_title))
        alert.setMessage(this.getString(R.string.dialog_message))
        alert.setPositiveButton(resources.getString(R.string.dialog_positive)) { _, _ ->
            finish()
        }
        alert.setNegativeButton(resources.getString(R.string.dialog_negative), null)
        alert.show()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime >= 1500) {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(
                this, resources.getString(R.string.toast_back_pressed), Toast.LENGTH_SHORT
            ).show()
        } else finish()
    }

    companion object {
        const val START_BANNER = 0
        const val END_BANNER = 3
    }

}