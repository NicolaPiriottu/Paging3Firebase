package com.example.paging3firebase.ui.home

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class PostModel(
    val id: String? = "",
    val title: String = "",
    val message: String = "",
    val likeCount: Int = 0,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "message" to message,
            "title" to title,
            "likeCount" to likeCount,
        )
    }
}