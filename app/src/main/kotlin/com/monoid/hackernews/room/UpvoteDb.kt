package com.monoid.hackernews.room

import androidx.room.Entity

@Entity(tableName = "upvote", primaryKeys = ["username", "itemId"])
data class UpvoteDb(
    val username: String,
    val itemId: Long,
)
