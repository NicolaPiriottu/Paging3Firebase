package com.example.paging3firebase.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paging3firebase.api.FirebaseDataSourceImp
import com.example.paging3firebase.api.FirestoreDataImpl
import com.example.paging3firebase.ui.home.HomeViewModel
import com.example.paging3firebase.ui.home.PostModel
import com.example.paging3firebase.utils.Event
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: FirestoreDataImpl) :
    ViewModel() {

    //region UseCase
    sealed class UseCaseLiveData {
        data class ShowItems(val items: MutableList<PostModel>) : UseCaseLiveData()
        data class ShowTitle(val title: String) : UseCaseLiveData()

    }
    //endregion UseCase

    //https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
    /*private val useCaseLiveData =
        MutableStateFlow<Event<UseCaseLiveData>>((Event(UseCaseLiveData.ShowItems(
            mutableListOf()))))
    val uiState: StateFlow<Event<UseCaseLiveData>> = useCaseLiveData*/

     val useCaseLiveData = MutableLiveData<Event<UseCaseLiveData>>()
    private val items: MutableList<PostModel> = mutableListOf()
    private var entrato = 0

    init {
        getPosts()
    }

    fun addItems() {
        repository.addPosts()
    }

    fun showFragment() {
        entrato = entrato.sumPage()
        viewModelScope.launch(Dispatchers.IO) {
            useCaseLiveData.postValue(((Event(UseCaseLiveData.ShowTitle(entrato.toString())))))
        }

        viewModelScope.launch(Dispatchers.IO) {
            useCaseLiveData.postValue(((Event(UseCaseLiveData.ShowItems(items)))))
        }

    }


    private fun getPosts() {

        val response = repository.getPosts().orderBy("id", Query.Direction.ASCENDING)
        val itemsNew: MutableList<PostModel> = mutableListOf()
        response.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("niko", "${document.id} => ${document.data}")
                    val myClass = document.toObject(PostModel::class.java)
                    itemsNew.add(myClass)
                }
                items.addAll(itemsNew)
                useCaseLiveData.value = ((Event(UseCaseLiveData.ShowItems(itemsNew))))
            }
            .addOnFailureListener { exception ->
                Log.w("niko", "Error getting documents.", exception)
            }
    }

    fun updatePost(postModel: PostModel) {
        repository.updatePost(postModel)
    }

    private fun Int.sumPage():Int{
        return this+1
    }

    private fun PostModel.map(){
        this.id
    }
}