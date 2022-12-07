package kr.or.mrhi.cinemastorage.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kr.or.mrhi.cinemastorage.data.Review

class ReviewDAO {
    /*firebaseRealtimeDatabase reviewTbl */
    var databaseReference: DatabaseReference? = null

    init {
        /*get instance reviewTbl @realtimeDB */
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("review")
    }

    /*insert into reviewTbl @realtimeDB*/
    fun insertReview(review: Review?): Task<Void> {
        return databaseReference!!.push().setValue(review)
    }

    /*select reviewTbl @realtimeDB*/
    fun selectReview(): Query? {
        return databaseReference
    }

    /*update reviewTbl @realtimeDB*/
    fun updateReview(key: String, hashMap: HashMap<String, Any>): Task<Void> {
        return databaseReference!!.child(key).updateChildren(hashMap)
    }

    /*delete reviewTbl @realtimeDB*/
    fun deleteReview(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }
}