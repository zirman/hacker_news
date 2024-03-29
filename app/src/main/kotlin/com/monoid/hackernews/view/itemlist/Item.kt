package com.monoid.hackernews.view.itemlist

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Quickreply
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Flag
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material.icons.twotone.Quickreply
import androidx.compose.material.icons.twotone.ThumbUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.monoid.hackernews.common.api.ItemId
import com.monoid.hackernews.common.data.ItemUi
import com.monoid.hackernews.common.data.LoginAction
import com.monoid.hackernews.common.data.Username
import com.monoid.hackernews.common.room.ItemDb
import com.monoid.hackernews.common.ui.text.ClickableTextBlock
import com.monoid.hackernews.common.ui.util.rememberTimeBy
import com.monoid.hackernews.common.ui.util.userTag
import com.monoid.hackernews.common.view.R
import com.monoid.hackernews.common.view.placeholder.PlaceholderHighlight
import com.monoid.hackernews.common.view.placeholder.placeholder
import com.monoid.hackernews.common.view.placeholder.shimmer
import com.monoid.hackernews.util.rememberAnnotatedString
import com.monoid.hackernews.common.view.TooltipPopupPositionProvider

@Preview(
    showBackground = true,
    wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE
)
@Composable
private fun ItemPreview() {
    Item(
        itemUi = object : ItemUi() {
            override val item: ItemDb = ItemDb(
                id = 0,
                type = "story",
                title = "Hello World",
                text = "Lorum Ipsum",
                url = "https://www.google.com/"
            )
            override val kids: List<ItemId> = emptyList()
            override val isUpvote: Boolean = false
            override val isFavorite: Boolean = false
            override val isFlag: Boolean = false
            override val isExpanded: Boolean = false
            override val isFollowed: Boolean = false
            override suspend fun toggleUpvote(onNavigateLogin: (LoginAction) -> Unit) {}
            override suspend fun toggleFavorite(onNavigateLogin: (LoginAction) -> Unit) {}
            override suspend fun toggleFlag(onNavigateLogin: (LoginAction) -> Unit) {}
            override suspend fun toggleExpanded() {}
            override suspend fun toggleFollowed() {}
        },
        onClickDetail = {},
        onClickReply = {},
        onClickUser = {},
        onClickBrowser = {},
        onClickUpvote = {},
        onClickFavorite = {},
        onClickFollow = {},
        onClickFlag = {}
    )
}

@Composable
fun Item(
    itemUi: ItemUi?,
    onClickDetail: () -> Unit,
    onClickReply: () -> Unit,
    onClickUser: (Username?) -> Unit,
    onClickBrowser: () -> Unit,
    onClickUpvote: () -> Unit,
    onClickFavorite: () -> Unit,
    onClickFollow: () -> Unit,
    onClickFlag: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val item = itemUi?.item
    val isLoading = item == null
    val isStoryOrComment = item?.type == "story" || item?.type == "comment"

    Surface(
        modifier = modifier
            .placeholder(
                visible = isLoading,
                color = Color.Transparent,
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = LocalContentColor.current.copy(
                        alpha = .5f
                    )
                )
            )
            .clickable(onClick = onClickDetail),
        contentColor = LocalContentColor.current,
        tonalElevation = ((item?.score ?: 0) / 10).dp
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                SelectionContainer(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item?.title?.let { AnnotatedString(text = it) }
                            ?: item?.text?.let { rememberAnnotatedString(htmlText = it) }
                            ?: AnnotatedString(text = ""),
                        minLines = 2,
                        maxLines = 2,
                        modifier = Modifier.padding(start = 8.dp),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                val (contextExpanded: Boolean, setContextExpanded) =
                    rememberSaveable { mutableStateOf(false) }

                Box {
                    IconButton(
                        onClick = { setContextExpanded(true) },
                        enabled = isStoryOrComment
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.MoreVert,
                            contentDescription = stringResource(id = R.string.more_options),
                        )
                    }

                    DropdownMenu(
                        expanded = contextExpanded,
                        onDismissRequest = { setContextExpanded(false) },
                        modifier = Modifier
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(
                                        id = if (itemUi?.isFavorite == true) {
                                            R.string.un_favorite
                                        } else {
                                            R.string.favorite
                                        }
                                    )
                                )
                            },
                            onClick = {
                                onClickFavorite()
                                setContextExpanded(false)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (itemUi?.isFavorite == true) {
                                        Icons.Filled.Favorite
                                    } else {
                                        Icons.TwoTone.Favorite
                                    },
                                    contentDescription = stringResource(
                                        id = if (itemUi?.isFavorite == true) {
                                            R.string.un_favorite
                                        } else {
                                            R.string.favorite
                                        }
                                    )
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(
                                        id = if (itemUi?.isFollowed == true) {
                                            R.string.unfollow
                                        } else {
                                            R.string.follow
                                        }
                                    )
                                )
                            },
                            onClick = {
                                onClickFollow()
                                setContextExpanded(false)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (itemUi?.isFollowed == true) {
                                        Icons.Filled.Quickreply
                                    } else {
                                        Icons.TwoTone.Quickreply
                                    },
                                    contentDescription = stringResource(
                                        id = if (itemUi?.isFollowed == true) {
                                            R.string.unfollow
                                        } else {
                                            R.string.follow
                                        }
                                    )
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(
                                        id = if (itemUi?.isFlag == true) {
                                            R.string.un_flag
                                        } else {
                                            R.string.flag
                                        }
                                    )
                                )
                            },
                            onClick = {
                                onClickFlag()
                                setContextExpanded(false)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (itemUi?.isFlag == true) {
                                        Icons.Filled.Flag
                                    } else {
                                        Icons.TwoTone.Flag
                                    },
                                    contentDescription = stringResource(
                                        id = if (itemUi?.isFlag == true) {
                                            R.string.un_flag
                                        } else {
                                            R.string.flag
                                        }
                                    )
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            val timeUserAnnotatedString: State<AnnotatedString> =
                rememberUpdatedState(
                    item
                        ?.let { rememberTimeBy(it) }
                        ?: AnnotatedString("")
                )

            ClickableTextBlock(
                text = timeUserAnnotatedString.value,
                lines = 1,
                onClick = { offset ->
                    val username = timeUserAnnotatedString.value
                        .getStringAnnotations(
                            tag = userTag,
                            start = offset,
                            end = offset,
                        )
                        .firstOrNull()
                        ?.item
                        ?.let { Username(it) }

                    if (username != null) {
                        onClickUser(username)
                    } else {
                        onClickDetail()
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .let {
                        if (isLoading) {
                            it.fillMaxWidth()
                        } else {
                            it
                        }
                    },
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = LocalContentColor.current
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                key("score") {
                    TooltipBox(
                        positionProvider = TooltipPopupPositionProvider(),
                        tooltip = { Surface { Text(stringResource(id = R.string.upvote)) } },
                        state = rememberTooltipState(),
                    ) {
                        IconButton(
                            onClick = onClickUpvote,
                            enabled = isStoryOrComment,
                        ) {
                            Icon(
                                imageVector = if (itemUi?.isUpvote == true) {
                                    Icons.Filled.ThumbUp
                                } else {
                                    Icons.TwoTone.ThumbUp
                                },
                                contentDescription = stringResource(
                                    id = if (itemUi?.isUpvote == true) {
                                        R.string.un_vote
                                    } else {
                                        R.string.upvote
                                    }
                                )
                            )
                        }
                    }

                    val score = item?.score

                    Text(
                        text = remember(score) { score?.toString() ?: "" },
                        maxLines = 1,
                        modifier = Modifier.widthIn(min = 24.dp),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                key("comments") {
                    val descendants = item?.descendants

                    TooltipBox(
                        positionProvider = TooltipPopupPositionProvider(),
                        tooltip = { Surface { Text(stringResource(id = R.string.comment)) } },
                        state = rememberTooltipState(),
                    ) {
                        IconButton(
                            onClick = onClickReply,
                            enabled = isStoryOrComment,
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.TwoTone.Comment,
                                contentDescription = null,
                            )
                        }
                    }

                    Text(
                        text = remember(descendants) { descendants?.toString() ?: "" },
                        maxLines = 1,
                        modifier = Modifier.widthIn(min = 24.dp),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }

                key("url") {
                    if (item?.url != null) {
                        val host: String = remember(item.url) {
                            item.url?.let { Uri.parse(it) }?.host ?: ""
                        }

                        Text(
                            text = host,
                            maxLines = 1,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelLarge,
                        )

                        TooltipBox(
                            positionProvider = TooltipPopupPositionProvider(),
                            tooltip = { Surface { Text(stringResource(id = R.string.open_in_browser)) } },
                            state = rememberTooltipState(),
                        ) {
                            IconButton(onClick = onClickBrowser) {
                                Icon(
                                    imageVector = Icons.Filled.OpenInBrowser,
                                    contentDescription = stringResource(id = R.string.open_in_browser),
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(
                thickness = Dp.Hairline,
                color = LocalContentColor.current,
            )
        }
    }
}
