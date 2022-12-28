package com.example.paging3firebase.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paging3firebase.utils.Event
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {

    //region UseCase
    sealed class UseCaseLiveData {
        data class ShowItems(val items: MutableList<PostModel>) : UseCaseLiveData()
    }
    //endregion UseCase

    //region LiveData
    val useCaseLiveData = MutableLiveData<Event<UseCaseLiveData>>()

   private val database = Firebase.database.reference
   private val myRef = database.child ("posts")

    fun getItems() {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val items: MutableList<PostModel> = mutableListOf()
                // Get Post object and use the values to update the UI
                for (objSnapshot in dataSnapshot.children) {
                    Log.w(TAG, "Niko loadPost:onDataChange post: $objSnapshot")
                    val myClass= objSnapshot.getValue(PostModel::class.java)
                    myClass?.let { items.add(it) }
                }

                useCaseLiveData.value = Event(UseCaseLiveData.ShowItems(items))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "Niko loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)

    }

    fun updatePost(post:PostModel){
        val updatePost:PostModel= PostModel(id = post.id, title = post.title, message = post.message, likeCount = post.likeCount+1)

        val childUpdates = hashMapOf<String, Any>(
            "/posts/${post.id}" to updatePost.toMap(),
        )
        database.updateChildren(childUpdates)
    }

    /*    binding.test.setOnClickListener {
            val database = Firebase.database
            val myRef = database.getReference("posts")
            var i=4
            while (i<30){

                val key = myRef.push().key
                val post= PostModel(id = key,title = "title $i", message = "message", likeCount = 0)
                if (key!=null)
                myRef.child(key) .setValue(post)
                i++
            }

        }*/

}