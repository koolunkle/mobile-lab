package kr.or.mrhi.cinemastorage.view.activity.account

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.cinemastorage.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.User
import kr.or.mrhi.cinemastorage.databinding.ActivityLoginBinding
import kr.or.mrhi.cinemastorage.view.activity.MainActivity

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null

    private val binding get() = _binding!!

    private var isUser = false

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
            val nickname = binding.edtNickname.text.toString()
            val password = binding.edtPassword.text.toString()

            if (binding.edtNickname.text.isEmpty() || binding.edtPassword.text.isEmpty()) {
                setToast("Please enter your nickname and password")
                return@setOnClickListener
            }

            val userDAO = UserDAO()
            val intent = Intent(applicationContext, MainActivity::class.java)

            userDAO.databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val user = data.getValue(User::class.java)
                        if (nickname == user?.nickname && password == user.password) isUser = true
                    }
                    if (isUser) {
                        setToast("Login Successful")
                        startActivity(intent)
                    } else setToast("Nickname or Password does not match")
                }

                override fun onCancelled(error: DatabaseError) {
                    setToast(error.message)
                }
            })
        }
    }

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}