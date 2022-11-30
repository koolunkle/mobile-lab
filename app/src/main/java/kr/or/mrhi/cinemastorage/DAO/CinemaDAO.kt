package kr.or.mrhi.cinemastorage.DAO

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kr.or.mrhi.cinemastorage.data.User

class CinemaDAO {
    /*firebaseRealtimeDatabase cinemaTBL */
    var databaseReference: DatabaseReference? = null

    /*firebaseStorage*/
    var storage: FirebaseStorage? = null

    init {
        /*get instance cinemaTBL @realtimeDB & storage*/
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("cinema")
        storage = Firebase.storage
    }

    /*insert into cinemaTBL @realtimeDB*/
    fun insertUser(user: User?): Task<Void> {
        return databaseReference!!.push().setValue(user)
    }

    /*select cinemaTBL @realtimeDB */
    fun selectUser(): Query? {
        return databaseReference
    }

    /*update cinemaTBL @realtimeDB*/
    fun updateUser(key: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(key).updateChildren(hashMap)
    }

    /*delete cinemaTBL @realtimeDB*/
    fun deleteUser(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }

}