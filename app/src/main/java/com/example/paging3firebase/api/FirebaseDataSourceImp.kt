package com.example.paging3firebase.api

import com.example.paging3firebase.di.FirebaseAppModule.database
import com.example.paging3firebase.ui.home.PostModel
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class FirebaseDataSourceImp @Inject constructor() {


      fun getPosts(): DatabaseReference {

       return database().child("posts")
    }

     fun updatePost(post: PostModel) {
        val childUpdates = hashMapOf<String, Any>(
            "/posts/${post.id}" to post.toMap(),
        )
         database().updateChildren(childUpdates)
    }

    fun addPosts(){

        val myRef = database().child("posts")
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