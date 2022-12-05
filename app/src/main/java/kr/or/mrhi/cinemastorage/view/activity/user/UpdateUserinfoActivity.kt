package kr.or.mrhi.cinemastorage.view.activity.user

import android.content.Intent
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
import kr.or.mrhi.cinemastorage.databinding.ActivityUpdateUserinfoBinding
import kr.or.mrhi.cinemastorage.util.SharedPreferences

class UpdateUserinfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserinfoBinding

    private var globalUser: User? = null

    private var loginUser: User? = null

    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setPreviousUserInfo()
            getExternalContentUri()
            uploadExternalContentUri()
        }

    }

    /*기존의 프로필이미지, 아이디, 비밀번호 불러와서 셋팅*/
    private fun setPreviousUserInfo() {
        val userDAO = UserDAO()
        userDAO.databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    globalUser = data.getValue(User::class.java)
                    if (globalUser?.key == SharedPreferences.getToken(applicationContext)) {
                        loginUser =
                            User(globalUser?.key!!, globalUser?.nickname, globalUser?.password)
                    }
                }
                val imagereference =
                    userDAO.storage!!.reference.child("images/${loginUser?.key}.jpg")
                imagereference.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Glide.with(applicationContext).load(it.result).into(binding.ivProfile)
                    }
                }
                binding.edtNickname.setText(loginUser?.nickname)
                binding.edtPw.setText(loginUser?.password!!)
            }

            override fun onCancelled(error: DatabaseError) {
                setToast(error.message)
            }
        })
    }

    /*변경된 이미지나 닉네임, 패스워드 firebase에 넣기*/
    private fun uploadExternalContentUri() {
        binding.btnSave.setOnClickListener {
            val nickname = binding.edtNickname.text
            val password = binding.edtPw.text
            val loginUserKey = SharedPreferences.getToken(applicationContext)
            val userDAO = UserDAO()
//            val intent = Intent(applicationContext, MainActivity::class.java)

            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["key"] = loginUserKey
            hashMap["nickname"] = nickname
            hashMap["password"] = password

//            val user = User(loginUserKey, nickname.toString(), password.toString())
            userDAO.databaseReference?.child("user/${loginUserKey}")?.updateChildren(hashMap)


/*            userDAO.updateUser(loginUserKey.toString(), hashMap).addOnSuccessListener {
                val imageReference = userDAO.storage?.reference?.child("images/${loginUserKey}.jpg")
                val file = Uri.fromFile(File(filePath))
                imageReference?.putFile(file)?.addOnSuccessListener {
                    Toast.makeText(this, "update user success", Toast.LENGTH_SHORT).show()

                    *//*val intent = Intent(applicationContext, MainActivity::class.java)*//*
                    startActivity(intent)
                }?.addOnFailureListener {
                    Toast.makeText( this, "update user profile failed", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "update user id/pw failed", Toast.LENGTH_SHORT).show()
            }*/
        }
    }

    /*프로필이미지 클릭하고 디바이스 갤러리에서 사진 가져오기*/
    private fun getExternalContentUri() {
        val requestLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    Glide.with(this).load(it.data?.data).into(binding.ivProfile)

                    val cursor = contentResolver.query(it.data?.data!!,
                        arrayOf(MediaStore.Images.Media.DATA),
                        null,
                        null,
                        null)
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
