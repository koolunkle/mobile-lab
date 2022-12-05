package kr.or.mrhi.cinemastorage.view.activity.user

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
import kr.or.mrhi.cinemastorage.R
import kr.or.mrhi.cinemastorage.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.User
import kr.or.mrhi.cinemastorage.databinding.ActivityUserinfoUpdateBinding
import java.io.File

class UpdateUserinfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserinfoUpdateBinding

    private lateinit var user: User

    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserinfoUpdateBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_userinfo_update)

        val userDAO = UserDAO()
        val imageName = user.key
        val imgRef = userDAO.storage!!.reference.child("image/${imageName}.jpg")

        imgRef.downloadUrl.addOnCompleteListener {
            if (it.isSuccessful) {
                Glide.with(applicationContext).load(it.result).into(binding.ivProfile)
            }
        }

        binding.apply {
            edtNickname.setText(user.nickname)
            edtPw.setText(user.password)
            getExternalContentUri()
            uploadExternalContentUri()
        }

    }

    private fun uploadExternalContentUri() {
        binding.btnSave.setOnClickListener {
            val nickname = binding.edtNickname.text
            val password = binding.edtPw.text

            val userDAO = UserDAO()
            val userKey = userDAO.databaseReference?.push()?.key
            val user = User(userKey.toString(), nickname.toString(), password.toString())

            if (binding.ivProfile.drawable == null || nickname.isBlank() || password.isBlank()) {
                setToast("Please enter your profile picture, nickname, and password at all")
                return@setOnClickListener
            } else {
                userDAO.databaseReference?.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val child: Iterator<DataSnapshot> = snapshot.children.iterator()
                        while (child.hasNext()) {
                            if (nickname.toString() == child.next().key) {
                                setToast("Duplicate nickname")
                                userDAO.databaseReference!!.removeEventListener(this)
                                return
                            }
                        }
                        val imageReference =
                            userDAO.storage?.reference?.child("images/${userKey}.jpg")
                        val file = Uri.fromFile(File(filePath))

                        imageReference?.putFile(file)?.apply {
                            addOnSuccessListener {
                                userDAO.databaseReference?.child(nickname.toString())
                                    ?.setValue(user).apply {
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
        val requestLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
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