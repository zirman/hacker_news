package com.monoid.hackernews.common.api

import com.monoid.hackernews.common.room.ItemDb
import com.monoid.hackernews.common.room.UserDb
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ItemId")
@JvmInline
value class ItemId(
    @SerialName("long")
    val long: Long,
)

@Serializable
@SerialName("UserApi")
data class UserApi(
    @SerialName("id")
    val id: String,
    @SerialName("created")
    val created: Long,
    @SerialName("about")
    val about: String? = null,
    @SerialName("karma")
    val karma: Int,
    @SerialName("submitted")
    val submitted: List<ItemId> = emptyList(),
)

fun UserApi.toUserApiUpdate(
    lastUpdate: Long = Clock.System.now().epochSeconds,
): UserDb {
    return UserDb(
        id = id,
        lastUpdate = lastUpdate,
        created = created,
        karma = karma,
        about = about,
    )
}

@Serializable
@SerialName("ItemApi")
sealed class ItemApi(
    @SerialName("deleted")
    val deleted: Boolean = false,
    @SerialName("kids")
    val kids: List<ItemId>? = null,
) {
    abstract val id: ItemId
    abstract val time: Long?

    @Serializable
    @SerialName("story")
    data class Story(
        @SerialName("id")
        override val id: ItemId,
        @SerialName("time")
        override val time: Long? = null,
        @SerialName("by")
        val by: String? = null,
        @SerialName("descendants")
        val descendants: Int? = null,
        @SerialName("score")
        val score: Int? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("text")
        val text: String? = null,
        @SerialName("url")
        val url: String? = null,
    ) : ItemApi()

    @Serializable
    @SerialName("job")
    data class Job(
        @SerialName("id")
        override val id: ItemId,
        @SerialName("time")
        override val time: Long? = null,
        @SerialName("by")
        val by: String? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("text")
        val text: String? = null,
        @SerialName("url")
        val url: String? = null,
    ) : ItemApi()

    @Serializable
    @SerialName("poll")
    data class Poll(
        @SerialName("id")
        override val id: ItemId,
        @SerialName("time")
        override val time: Long? = null,
        @SerialName("by")
        val by: String? = null,
        @SerialName("parts")
        val parts: List<ItemId>,
        @SerialName("descendants")
        val descendants: Int? = null,
        @SerialName("score")
        val score: Int? = null,
        @SerialName("title")
        val title: String? = null,
    ) : ItemApi()

    @Serializable
    @SerialName("pollopt")
    data class PollOpt(
        @SerialName("id")
        override val id: ItemId,
        @SerialName("time")
        override val time: Long? = null,
        @SerialName("poll")
        val poll: ItemId,
        @SerialName("by")
        val by: String? = null,
        @SerialName("score")
        val score: Int? = null,
        @SerialName("title")
        val title: String? = null,
    ) : ItemApi()

    @Serializable
    @SerialName("comment")
    data class Comment(
        @SerialName("id")
        override val id: ItemId,
        @SerialName("time")
        override val time: Long? = null,
        @SerialName("by")
        val by: String? = null,
        @SerialName("parent")
        val parent: ItemId,
        @SerialName("text")
        val text: String? = null,
    ) : ItemApi()
}

fun ItemApi.toItemDb(
    lastUpdate: Long? = Clock.System.now().epochSeconds,
): ItemDb {
    return when (this) {
        is ItemApi.Comment -> {
            ItemDb(
                id = id.long,
                lastUpdate = lastUpdate,
                type = "comment",
                time = time,
                deleted = deleted,
                by = by,
                text = text,
                parent = parent.long,
            )
        }

        is ItemApi.Job -> {
            ItemDb(
                id = id.long,
                lastUpdate = lastUpdate,
                type = "job",
                time = time,
                deleted = deleted,
                by = by,
                title = title,
                text = text,
                url = url,
            )
        }

        is ItemApi.Poll -> {
            ItemDb(
                id = id.long,
                lastUpdate = lastUpdate,
                type = "poll",
                time = time,
                deleted = deleted,
                by = by,
                descendants = descendants,
                score = score,
                title = title,
            )
        }

        is ItemApi.PollOpt -> {
            ItemDb(
                id = id.long,
                lastUpdate = lastUpdate,
                type = "pollopt",
                time = time,
                deleted = deleted,
                by = by,
                score = score,
                title = title,
            )
        }

        is ItemApi.Story -> {
            ItemDb(
                id = id.long,
                lastUpdate = lastUpdate,
                type = "story",
                time = time,
                deleted = deleted,
                by = by,
                descendants = descendants,
                score = score,
                title = title,
                text = text,
                url = url,
            )
        }
    }
}
