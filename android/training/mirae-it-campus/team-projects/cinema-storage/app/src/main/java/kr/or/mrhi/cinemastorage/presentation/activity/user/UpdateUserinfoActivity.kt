package kr.or.mrhi.cinemastorage.presentation.activity.user

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
import kr.or.mrhi.cinemastorage.data.dao.ReviewDAO
import kr.or.mrhi.cinemastorage.data.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.model.Review
import kr.or.mrhi.cinemastorage.data.model.User
import kr.or.mrhi.cinemastorage.databinding.ActivityUpdateUserinfoBinding
import kr.or.mrhi.cinemastorage.util.SharedPreferences
import kr.or.mrhi.cinemastorage.presentation.activity.MainActivity
import java.io.File

class UpdateUserinfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserinfoBinding

    private var globalUser: User? = null

    private var loginUser: User? = null

    private var filePath: String? = null

    private val userDAO = UserDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            getExternalContentUri()
            setPreviousUserInfo()
            setUpdateUserInfo()
            moveToMain()
        }
    }

    private fun setPreviousUserInfo() {
        userDAO.databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    globalUser = data.getValue(User::class.java)
                    if (globalUser?.key == SharedPreferences.getToken(applicationContext)) {
                        loginUser =
                            User(globalUser?.key!!, globalUser?.nickname, globalUser?.password)
                    }
                }
                val imageReference =
                    userDAO.storage!!.reference.child("images/${loginUser?.key}.jpg")
                imageReference.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful) Glide.with(applicationContext).load(it.result)
                        .into(binding.ivProfile)
                }
                binding.edtNickname.setText(loginUser?.nickname)
                binding.edtPw.setText(loginUser?.password!!)
            }

            override fun onCancelled(error: DatabaseError) {
                setToast(error.message)
            }
        })
    }

    private fun updateReviewer(
        loginUserKey: String, hashMap: HashMap<String, Any>, nickname: String
    ) {
        userDAO.databaseReference?.child(loginUserKey)?.updateChildren(hashMap)?.apply {
            addOnSuccessListener {
                val reviewDAO = ReviewDAO()
                val reviewMap: HashMap<String, Any> = HashMap()
                reviewMap["reviewer"] = nickname

                reviewDAO.databaseReference?.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            val review = data.getValue(Review::class.java)!!
                            if (review.key == SharedPreferences.getToken(applicationContext)) {
                                reviewDAO.databaseReference?.child(review.date.toString())
                                    ?.updateChildren(reviewMap)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        setToast(error.message)
                    }
                })
            }
            addOnFailureListener { setToast("Failed to update user nickname") }
        }
    }

    private fun updateProfile(loginUserKey: String) {
        if (filePath != null) {
            val imageReference = userDAO.storage?.reference?.child("images/${loginUserKey}.jpg")
            val file = Uri.fromFile(File(filePath!!))

            imageReference?.putFile(file)?.apply {
                addOnFailureListener { setToast("Failed to update user profile") }
            }
        }
    }

    private fun setUpdateUserInfo() {
        binding.btnSave.setOnClickListener {
            val nickname = binding.edtNickname.text.toString()
            val password = binding.edtPw.text.toString()
            val loginUserKey = SharedPreferences.getToken(applicationContext)

            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["nickname"] = nickname
            hashMap["password"] = password

            userDAO.databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        globalUser = data.getValue(User::class.java)
                        if (globalUser?.key != SharedPreferences.getToken(applicationContext) && globalUser?.nickname == nickname) {
                            setToast("Duplicate nickname")
                            return
                        }
                    }
                    updateReviewer(loginUserKey, hashMap, nickname)
                    updateProfile(loginUserKey)
                    setToast("Succeed to update user nickname, profile")
                }

                override fun onCancelled(error: DatabaseError) {
                    setToast(error.message)
                }
            })
        }
    }

    private fun getExternalContentUri() {
        val requestLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
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

    private fun moveToMain() {
        binding.btnCancel.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}
