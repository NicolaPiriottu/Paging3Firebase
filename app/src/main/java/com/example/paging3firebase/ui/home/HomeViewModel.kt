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
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FirebaseDataSourceImp) :
    ViewModel() {

    //region UseCase
    sealed class UseCaseLiveData {
        data class ShowItems(val items: MutableList<PostModel>) : UseCaseLiveData()
        data class ShowTitle(val title:String): UseCaseLiveData()
        data class ShowTitle2(val title:String): UseCaseLiveData()
    }
    //endregion UseCase

    //region LiveData
    val useCaseLiveData = MutableStateFlow<Event<UseCaseLiveData>>((Event(UseCaseLiveData.ShowItems(
        mutableListOf()))))

    val uiState: StateFlow<Event<UseCaseLiveData>> = useCaseLiveData

    fun getItems(key: String = "") {

        val response = repository.getPosts().limitToFirst(10).orderByKey().startAfter(key)


        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val itemsNew: MutableList<PostModel> = mutableListOf()
                Log.w(ContentValues.TAG, "Niko loadPost:onDataChange dataSnapshot: $dataSnapshot")
                // Get Post object and use the values to update the UI
                for (objSnapshot in dataSnapshot.children) {
                    Log.w(ContentValues.TAG, "Niko loadPost:onDataChange post: $objSnapshot")
                    val myClass = objSnapshot.getValue(PostModel::class.java)
                    myClass?.let { itemsNew.add(it) }
                }
                useCaseLiveData.value = ((Event(UseCaseLiveData.ShowItems(itemsNew))))
                useCaseLiveData.value = ((Event(UseCaseLiveData.ShowTitle("TITOLO"))))
                useCaseLiveData.value = ((Event(UseCaseLiveData.ShowTitle2("SOTTOTITOLO"))))
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