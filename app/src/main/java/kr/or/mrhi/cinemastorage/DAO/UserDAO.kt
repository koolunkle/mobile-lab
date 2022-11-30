package kr.or.mrhi.cinemastorage.DAO

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kr.or.mrhi.cinemastorage.data.User

class UserDAO {
    /*firebaseRealtimeDatabase userTbl */
    var databaseReference: DatabaseReference? = null

    /*firebaseStorage*/
    var storage: FirebaseStorage? = null

    init {
        /*get instance userTbl @realtimeDB & storage*/
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("user")
        storage = Firebase.storage
    }

    /*insert into userTBL @realtimeDB*/
    fun insertUser(user: User?): Task<Void> {
        return databaseReference!!.push().setValue(user)
    }

    /*select userTBL @realtimeDB */
    fun selectUser(): Query? {
        return databaseReference
    }

    /*update userTBL @realtimeDB*/
    fun updateUser(key: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(key).updateChildren(hashMap)
    }

    /*delete userTBL @realtimeDB*/
    fun deleteUser(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }
}