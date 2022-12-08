package kr.or.mrhi.cinemastorage.view.activity.account

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kr.or.mrhi.cinemastorage.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.User
import kr.or.mrhi.cinemastorage.databinding.ActivitySignUpBinding
import java.io.File

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private var filePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            getExternalContentUri()
            uploadExternalContentUri()
        }
    }

    private fun uploadExternalContentUri() {
        binding.btnSignUp.setOnClickListener {
            val nickname = binding.edtNickname.text
            val password = binding.edtPassword.text

            /*userDAO: 미리 생성둔 user의 데이터베이스(realtimeDB, storage)연결객체 클래스.*/
            val userDAO = UserDAO()
            val userKey = userDAO.databaseReference?.push()?.key
            val user = User(userKey.toString(), nickname.toString(), password.toString())

            /*프로필이미지, 닉네임, 비밀번호 전부 입력해야 버튼이벤트가 실행되도록 방어*/
            if (filePath.isNullOrBlank() || nickname.isBlank() || password.isBlank()) {
                setToast("Please enter your profile, nickname and password at all")
                return@setOnClickListener
            } else {
                /* firebase의 realtimeDB의 데이터들을 향상된 포문으로
                * 입력된 닉네임과 같은 닉네임을 갖은 데이터가 있는지 검사해서 동일한 닉네임 방어*/
                userDAO.databaseReference?.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            val globalUser = data.getValue(User::class.java)
                            if (globalUser?.nickname == nickname.toString()) {
                                setToast("Duplicate nickname")
                                return
                            }
                        }
                        /*storage에 이미지 이름을 userKey로 설정, 컨텐트리졸버로 저장한 파일패스로 파일Uri 변수 생성
                        * putFile로 스토리지에 이미지 저장, addOnSuccessListener로 리얼타임에 유저정보 저장*/
                        val imageReference =
                            userDAO.storage?.reference?.child("images/${userKey}.jpg")
                        val file = Uri.fromFile(File(filePath.toString()))

                        imageReference?.putFile(file)?.apply {
                            addOnSuccessListener {
                                userDAO.databaseReference?.child(userKey.toString())?.setValue(user)
                                    .apply {
                                        addOnSuccessListener { setToast("Success to upload user info") }
                                        addOnFailureListener { setToast("Failed to upload user info") }
                                    }
                            }
                            addOnFailureListener { setToast("Failed to upload user profile") }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        setToast(error.message)
                    }
                })
            }
        }
    }

    /*이미지를 클릭하면 디바이스의 갤러리로 이동해(intent) 선택한 이미지의 컨텐트리졸버로 파일경로를 저장.
    Glide로 선택한 이미지를 바인딩 */
    private fun getExternalContentUri() {
        val requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Glide.with(this).load(it.data?.data).into(binding.ivProfile)

                val cursor = contentResolver.query(
                    it.data?.data!!, arrayOf(MediaStore.Images.Media.DATA), null, null, null
                )
                cursor?.moveToFirst().let {
                    if (cursor != null) filePath = cursor.getString(0)
                    cursor?.close()
                }
            }
        }
        binding.ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }
    }

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}