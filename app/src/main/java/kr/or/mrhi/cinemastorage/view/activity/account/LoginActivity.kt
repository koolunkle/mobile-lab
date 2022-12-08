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
            /*editTextView에 값을 모두 입력해야 이벤트 발생하도록 방어*/
            if (binding.edtNickname.text.isEmpty() || binding.edtPassword.text.isEmpty()) {
                setToast("Please enter your nickname and password")
                return@setOnClickListener
            }

            val userDAO = UserDAO()
            val intent = Intent(applicationContext, MainActivity::class.java)
            /*firebase realtimeDB에 입력한 닉네임과 패스워드가 같은 데이터가 있는지 검사해서
            * isUser(flag)를 true로 변경.SharedPreferences에 로그인한 user의 key를 저장해
            * 앱을 사용하는 동안 다른 액티비티에서 유저의 정보가 필요할때 DB에서 검색하는데 이용할수 있도록함*/
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
                        setToast("Login Successful")
                        SharedPreferences.setToken(applicationContext, loginUser?.key!!)
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

}