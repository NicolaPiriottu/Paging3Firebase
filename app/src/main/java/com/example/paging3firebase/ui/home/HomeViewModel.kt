package com.example.paging3firebase.ui.home

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paging3firebase.api.FirebaseDataSourceImp
import com.example.paging3firebase.utils.Event
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FirebaseDataSourceImp) :
    ViewModel() {

    //region UseCase
    sealed class UseCaseLiveData {
        data class ShowItems(val items: MutableList<PostModel>) : UseCaseLiveData()
    }
    //endregion UseCase

    //region LiveData
    val useCaseLiveData = MutableLiveData<Event<UseCaseLiveData>>()

    val items: MutableList<PostModel> = mutableListOf()

    fun getItems(key:String="") {

        val response = repository.getPosts().limitToFirst(10).orderByKey().startAfter(key)

        val itemsNew: MutableList<PostModel> = mutableListOf()
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.w(ContentValues.TAG, "Niko loadPost:onDataChange dataSnapshot: $dataSnapshot")
                // Get Post object and use the values to update the UI
                for (objSnapshot in dataSnapshot.children) {
                    Log.w(ContentValues.TAG, "Niko loadPost:onDataChange post: $objSnapshot")
                    val myClass = objSnapshot.getValue(PostModel::class.java)
                    myClass?.let { items.add(it) }
                }
                itemsNew.addAll(items)
                useCaseLiveData.postValue(Event(UseCaseLiveData.ShowItems(itemsNew)))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "Niko loadPost:onCancelled", databaseError.toException())
            }
        }
        response.addValueEventListener(postListener)
    }

    fun updatePost(post: PostModel) {

        repository.updatePost(
            PostModel(id = post.id,
                title = post.title,
                message = post.message,
                likeCount = post.likeCount + 1)
        )
    }

    fun load() {
       getItems("-NKOA-f8A1GqpXvt8enI")
    }
}