package com.example.paging3firebase.api

import android.util.Log
import com.example.paging3firebase.ui.home.PostModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirestoreDataImpl @Inject constructor(){

   private val database = Firebase.firestore

    fun addPosts(){
        val myRef = database.collection("posts")

        var i=0
        while (i<3){

            val key = myRef.document().id
            val post= PostModel(id = key,title = "title $i", message = "message", likeCount = 0).toMap()

                myRef.document(key).set(post)
                    .addOnSuccessListener { documentReference ->
                    Log.d("niko", "DocumentSnapshot added with ID: ${documentReference}")
                }
                    .addOnFailureListener { e ->
                        Log.w("niko", "Error adding document", e)
                    }
                i++
        }
    }

    fun getPosts(): CollectionReference {
       return database.collection("posts")
    }

    fun updatePost(postModel: PostModel){
        database.collection("posts").document(postModel.id?:"")
            .update(mapOf(
                "likeCount" to FieldValue.increment(1),
            ))
    }
}