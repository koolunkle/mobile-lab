package kr.or.mrhi.cinemastorage.view.activity.account

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kr.or.mrhi.cinemastorage.data.User
import kr.or.mrhi.cinemastorage.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private var _binding: ActivitySignUpBinding? = null

    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    lateinit var filePath: String

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference

        var requestLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Glide.with(applicationContext)
                    .load(it.data?.data)
                    .into(binding.ivProfile)
                imageUri = it.data?.data
                var cursor = contentResolver.query(it.data?.data as Uri,
                    arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null)
                cursor?.moveToFirst().let {
                    filePath = cursor!!.getString(0) /*절대경로*/
                }
            }
        }

        /*갤러리에서 이미지파일 골라와서 프로필 이미지에 넣기*/
        binding.ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }

        /*이미지, 닉네임, 비밀번호 입력 후 signUp 버튼 눌렀을때 firebase에 데이터 보냄*/
        binding.btnSignUp.setOnClickListener {
            if (binding.ivProfile.drawable == null || binding.edtNickname.text.isEmpty() || binding.edtPassword.text.isEmpty()) {
                Toast.makeText(this,
                    "Please enter your profile picture, nickname, and password at all.",
                    Toast.LENGTH_SHORT).show()
            } else {
                val nickname = binding.edtNickname.text.toString()
                val pw = binding.edtPassword.text.toString()

                FirebaseStorage.getInstance()
                    .reference.child("userProfileImage").child("$nickname/photo")
                    .putFile(imageUri!!).addOnSuccessListener {
                        var userProfile: Uri? = null
                        FirebaseStorage.getInstance().reference.child("userProfileImage")
                            .child("$nickname/photo").downloadUrl.addOnSuccessListener {
                                userProfile = it
                                Log.d("SignUpActivity", " successful user upload")
                                val user = User("",
                                    nickname.toString(),
                                    pw.toString(),
                                    userProfile.toString())
                                database.child("users").child(nickname.toString()).setValue(user)
                            }
                    }
                Toast.makeText(this, "회원가입완료", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}