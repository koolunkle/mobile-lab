package kr.or.mrhi.cinemastorage.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.cinemastorage.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private var _binding: ActivitySignUpBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}