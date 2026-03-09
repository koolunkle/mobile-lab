package kr.or.mrhi.cinemastorage.data.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class ReviewDAO {
    var databaseReference: DatabaseReference? = null

    init {
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = db.getReference("review")
    }

    fun selectReview(): Query? {
        return databaseReference
    }

    fun deleteReview(key: String): Task<Void> {
        return databaseReference!!.child(key).removeValue()
    }
}