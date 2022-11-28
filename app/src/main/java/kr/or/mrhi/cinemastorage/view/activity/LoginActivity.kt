package kr.or.mrhi.cinemastorage.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.cinemastorage.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            moveToMain()
        }
    }

    private fun moveToMain() {
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}