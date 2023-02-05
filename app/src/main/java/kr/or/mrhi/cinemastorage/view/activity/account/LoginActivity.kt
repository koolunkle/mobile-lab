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
import kr.or.mrhi.cinemastorage.util.SharedPreferences
import kr.or.mrhi.cinemastorage.view.activity.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var globalUser: User? = null

    private var loginUser: User? = null

    private var isUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            moveToMain()
        }
    }

    private fun moveToMain() {
        binding.btnLogin.setOnClickListener {
            val nickname = binding.edtNickname.text.toString()
            val password = binding.edtPassword.text.toString()

            if (binding.edtNickname.text.isBlank() || binding.edtPassword.text.isBlank()) {
                setToast("Please enter your nickname and password")
                return@setOnClickListener
            }
            val userDAO = UserDAO()
            userDAO.databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        globalUser = data.getValue(User::class.java)
                        if (nickname == globalUser?.nickname && password == globalUser?.password) {
                            loginUser =
                                User(globalUser?.key!!, globalUser?.nickname, globalUser?.password)
                            isUser = true
                        }
                    }
                    if (isUser) {
                        setToast("Thanks for coming, ${loginUser?.nickname}!")
                        SharedPreferences.setToken(applicationContext, loginUser?.key!!)
                        startActivity(Intent(applicationContext, MainActivity::class.java))
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

}