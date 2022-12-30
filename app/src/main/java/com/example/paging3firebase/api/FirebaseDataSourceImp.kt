package com.example.paging3firebase.api

import android.content.ContentValues
import android.util.Log
import com.example.paging3firebase.ui.home.PostModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FirebaseDataSourceImp @Inject constructor() {

    private val database =  Firebase.database.reference

      fun getPosts(): DatabaseReference {

       return database.child("posts")
    }

     fun updatePost(post: PostModel) {
        val childUpdates = hashMapOf<String, Any>(
            "/posts/${post.id}" to post.toMap(),
        )
         database.updateChildren(childUpdates)
    }

    fun addPosts(){

        val myRef = database.child("posts")
        var i=4
        while (i<30){

            val key = myRef.push().key
            val post= PostModel(id = key,title = "title $i", message = "message", likeCount = 0)
            if (key!=null)
                myRef.child(key) .setValue(post)
            i++
        }
    }
}