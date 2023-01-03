package com.example.paging3firebase.data

import android.content.ContentValues
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3firebase.api.FirebaseDataSourceImp
import com.example.paging3firebase.ui.home.HomeViewModel
import com.example.paging3firebase.ui.home.PostModel
import com.example.paging3firebase.utils.Event
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.io.IOException

class PostPagingSource(private val repository: FirebaseDataSourceImp) :
    PagingSource<Int, PostModel>() {

    private var postIdList: MutableList<String> = mutableListOf()
    private var postId = ""
    private val items: MutableList<PostModel> = mutableListOf()
    private var next: Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostModel> {
        var position: Int = params.key ?: 0
        return try {
            postIdList.add((position - 1), postId)
            val response = repository.getPosts().orderByKey().startAfter(postIdList[position - 1])

            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    Log.w(ContentValues.TAG,
                        "Niko loadPost:onDataChange dataSnapshot: $dataSnapshot")
                    // Get Post object and use the values to update the UI
                    for (objSnapshot in dataSnapshot.children) {
                        Log.w(ContentValues.TAG, "Niko loadPost:onDataChange post: $objSnapshot")
                        val myClass = objSnapshot.getValue(PostModel::class.java)
                        myClass?.let { items.add(it) }

                    }

                    if (items.isNullOrEmpty().not()) {
                        postId = items.last().id ?: ""
                        position += 1
                        next = position
                    } else {
                        next = null
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(ContentValues.TAG,
                        "Niko loadPost:onCancelled",
                        databaseError.toException())
                }
            }

            response.addValueEventListener(postListener)
            LoadResult.Page(data = items, prevKey = null, nextKey = next)
        } catch (ex: IOException) {
            return LoadResult.Error(ex)
        } catch (ex: HttpException) {
            return LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PostModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->

            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}