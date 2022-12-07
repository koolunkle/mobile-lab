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

            val userDAO = UserDAO()
            val userKey = userDAO.databaseReference?.push()?.key
            val user = User(userKey.toString(), nickname.toString(), password.toString())

            if (filePath.isNullOrBlank() || nickname.isBlank() || password.isBlank()) {
                setToast("Please enter your profile picture, nickname and password at all")
                return@setOnClickListener
            } else {
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
                        val imageReference =
                            userDAO.storage?.reference?.child("images/${userKey}.jpg")
                        val file = Uri.fromFile(File(filePath.toString()))

                        imageReference?.putFile(file)?.apply {
                            addOnSuccessListener {
                                userDAO.databaseReference?.child(userKey.toString())?.setValue(user)
                                    .apply {
                                        addOnSuccessListener { setToast("Success to insert data") }
                                        addOnFailureListener { setToast("Failed to insert data") }
                                    }
                            }
                            addOnFailureListener { setToast("Failed to insert data") }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        setToast(error.message)
                    }
                })
            }
        }
    }

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
        binding.tvProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }
    }

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}