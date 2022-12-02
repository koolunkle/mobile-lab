package kr.or.mrhi.cinemastorage.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kr.or.mrhi.cinemastorage.data.Review

class ReviewDAO {
    /*firebaseRealtimeDatabase reviewTbl */
    var databaseReference: DatabaseReference? = null

    /*firebaseStorage*/
    var storage: FirebaseStorage? = null

    init {
        /*get instance reviewTbl @realtimeDB */
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("review")
        storage = Firebase.storage
    }

    /*insert into reviewTbl @realtimeDB*/
    fun insertUser(review: Review?): Task<Void> {
        return databaseReference!!.push().setValue(review)
    }

    /*select reviewTbl @realtimeDB*/
    fun selectUser(): Query? {
        return databaseReference
    }

    /*update reviewTbl @realtimeDB*/
    fun updateUser(key: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(key).updateChildren(hashMap)
    }

    /*delete reviewTbl @realtimeDB*/
    fun deleteUser(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }
}