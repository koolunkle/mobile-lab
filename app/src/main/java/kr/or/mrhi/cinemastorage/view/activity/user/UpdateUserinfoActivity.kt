package kr.or.mrhi.cinemastorage.view.activity.user

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
import kr.or.mrhi.cinemastorage.dao.ReviewDAO
import kr.or.mrhi.cinemastorage.dao.UserDAO
import kr.or.mrhi.cinemastorage.data.Review
import kr.or.mrhi.cinemastorage.data.User
import kr.or.mrhi.cinemastorage.databinding.ActivityUpdateUserinfoBinding
import kr.or.mrhi.cinemastorage.util.SharedPreferences
import kr.or.mrhi.cinemastorage.view.activity.MainActivity
import java.io.File

class UpdateUserinfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserinfoBinding

    private var globalUser: User? = null

    private var loginUser: User? = null

    private var filePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setPreviousUserInfo()
            getExternalContentUri()
            uploadExternalContentUri()
            cancelButtonClick()
        }
    }

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

    private fun uploadExternalContentUri() {
        binding.btnClearNickname.setOnClickListener { binding.edtNickname.text.clear() }
        binding.btnDeletePw.setOnClickListener { binding.edtPw.text.clear() }

        binding.btnSave.setOnClickListener {
            val nickname = binding.edtNickname.text.toString()
            val password = binding.edtPw.text.toString()
            val loginUserKey = SharedPreferences.getToken(applicationContext)

            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["nickname"] = nickname
            hashMap["password"] = password

            val userDAO = UserDAO()
            userDAO.databaseReference?.child(loginUserKey)?.updateChildren(hashMap)?.apply {
                addOnSuccessListener {
                    val reviewMap: HashMap<String, Any> = HashMap()
                    val reviewDAO = ReviewDAO()

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

                    val imageReference =
                        userDAO.storage?.reference?.child("images/${loginUserKey}.jpg")
                    val file = Uri.fromFile(File(filePath))

                    imageReference?.putFile(file)?.apply {
                        addOnSuccessListener {
                            setToast("User information update succeeded.")
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }
                        addOnFailureListener { setToast("User information update failed.") }
                    }
                }
                addOnFailureListener {
                    setToast("User information update failed.")
                }
            }
              userDAO.databaseReference?.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            globalUser = data.getValue(User::class.java)
                            if (globalUser?.key != SharedPreferences.getToken(applicationContext) && globalUser?.nickname == nickname) {
                                setToast("Duplicate nickname")
                                userDAO.databaseReference!!.removeEventListener(this)
                                return
                            }
                        }
                        userDAO.databaseReference?.child(loginUserKey)?.updateChildren(hashMap)
                            ?.addOnSuccessListener {
                                if(filePath != null){
                                    val imageReference =
                                        userDAO.storage?.reference?.child("images/${loginUserKey}.jpg")
                                    val file = Uri.fromFile(File(filePath!!))

                                    imageReference?.putFile(file)?.addOnSuccessListener {
                                        setToast("User information update succeeded.")
                                    }
                                }

                                startActivity(intent)
                                finish()
                            }?.addOnFailureListener {
                                setToast("User information update failed.")
                            }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        setToast("User information update failed.")
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

    private fun cancelButtonClick() {
        binding.btnCancel.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}
