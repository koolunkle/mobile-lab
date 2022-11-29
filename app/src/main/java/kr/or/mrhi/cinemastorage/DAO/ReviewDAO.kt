package kr.or.mrhi.cinemastorage.DAO

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kr.or.mrhi.cinemastorage.data.Review

class ReviewDAO {
    /*firebaseRealtimeDatabase reviewTbl */
    var databaseReference: DatabaseReference? = null

    init {
        /*get instance realtime database of firebase*/
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("user")
    }

    /*insert into reviewTbl @realtime database*/
    fun insertUser(review: Review?): Task<Void> {
        return databaseReference!!.push().setValue(review)
    }

    /*realtime database reviewTbl select*/
    fun selectUser(): Query? {
        return databaseReference
    }

    /*realtime database reviewTbl update*/
    fun updateUser(key: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(key).updateChildren(hashMap)
    }

    /*realtime database reviewTbl delete*/
    fun deleteUser(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }
}