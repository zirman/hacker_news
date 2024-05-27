package com.monoid.hackernews.common.data

import android.os.Parcelable
import com.monoid.hackernews.common.api.ItemId
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimpleItemUiStateImpl(
    override val id: ItemId,
    override val lastUpdate: Long? = null,
    override val type: String? = null,
    override val time: Long? = null,
    override val deleted: Boolean? = null,
    override val by: String? = null,
    override val descendants: Int? = null,
    override val score: Int? = null,
    override val title: String? = null,
    override val text: String? = null,
    override val url: String? = null,
    override val parent: ItemId? = null,
    override val kids: List<ItemId>? = null,
    override val isUpvote: Boolean? = null,
    override val isFavorite: Boolean? = null,
    override val isFlag: Boolean? = null,
    override val isExpanded: Boolean? = null,
    override val isFollowed: Boolean? = null,
) : SimpleItemUiState, Parcelable

actual fun makeSimpleItemUiState(
    id: ItemId,
    lastUpdate: Long?,
    type: String?,
    time: Long?,
    deleted: Boolean?,
    by: String?,
    descendants: Int?,
    score: Int?,
    title: String?,
    text: String?,
    url: String?,
    parent: ItemId?,
    kids: List<ItemId>?,
    isUpvote: Boolean?,
    isFavorite: Boolean?,
    isFlag: Boolean?,
    isExpanded: Boolean?,
    isFollowed: Boolean?,
): SimpleItemUiState = SimpleItemUiStateImpl(
    id = id,
    lastUpdate = lastUpdate,
    type = type,
    time = time,
    deleted = deleted,
    by = by,
    descendants = descendants,
    score = score,
    title = title,
    text = text,
    url = url,
    parent = parent,
    kids = kids,
    isUpvote = isUpvote,
    isFavorite = isFavorite,
    isFlag = isFlag,
    isExpanded = isExpanded,
    isFollowed = isFollowed,
)
