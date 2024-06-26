package com.monoid.hackernews.view.itemdetail

import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Reply
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Quickreply
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.twotone.ExpandLess
import androidx.compose.material.icons.twotone.ExpandMore
import androidx.compose.material.icons.twotone.Flag
import androidx.compose.material.icons.twotone.MoreVert
import androidx.compose.material.icons.twotone.Quickreply
import androidx.compose.material.icons.twotone.ThumbUp
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.monoid.hackernews.common.api.ItemId
import com.monoid.hackernews.common.data.ItemUiWithThreadDepth
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
import com.monoid.hackernews.util.onClick
import com.monoid.hackernews.util.rememberAnnotatedString
import kotlinx.coroutines.launch

@Composable
fun CommentItem(
    itemUiState: State<ItemUiWithThreadDepth?>,
    onClickUser: (Username) -> Unit,
    onClickReply: (ItemId) -> Unit,
    onNavigateLogin: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .padding(start = (((itemUiState.value?.threadDepth ?: 1) - 1) * 16).dp)
            .then(
                if (itemUiState.value == null) {
                    Modifier.drawWithContent {}
                } else {
                    Modifier
                }
            ),
        shape = MaterialTheme.shapes.medium,
        contentColor = MaterialTheme.colorScheme.secondary,
        tonalElevation = ((itemUiState.value?.itemUi?.kids?.size ?: 0) * 10 + 40).dp
    ) {
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = if (itemUiState.value?.itemUi?.item == null) {
                Modifier
            } else {
                Modifier
                    .clickable {
                        coroutineScope.launch {
                            itemUiState.value?.itemUi?.toggleExpanded()
                        }
                    }
                    .animateContentSize()
                    .placeholder(
                        visible = itemUiState.value?.itemUi?.item?.lastUpdate == null,
                        color = Color.Transparent,
                        shape = MaterialTheme.shapes.small,
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = LocalContentColor.current.copy(alpha = .5f)
                        )
                    )
            },
        ) {
            val isDeleted =
                itemUiState.value?.itemUi?.item?.text == null &&
                    itemUiState.value?.itemUi?.item?.lastUpdate != null

            Row(verticalAlignment = Alignment.CenterVertically) {
                val timeByUserAnnotatedString: State<AnnotatedString> =
                    rememberUpdatedState(
                        rememberTimeBy(itemUiState.value?.itemUi?.item ?: ItemDb(id = 0))
                    )

                ClickableTextBlock(
                    text = timeByUserAnnotatedString.value,
                    lines = 1,
                    onClick = { offset ->
                        if (itemUiState.value?.itemUi?.isExpanded == true) {
                            val username: Username? = timeByUserAnnotatedString.value
                                .getStringAnnotations(
                                    tag = userTag,
                                    start = offset,
                                    end = offset
                                )
                                .firstOrNull()
                                ?.item
                                ?.let { Username(it) }

                            if (username != null) {
                                onClickUser(username)
                            } else {
                                coroutineScope.launch {
                                    itemUiState.value?.itemUi?.toggleExpanded()
                                }
                            }
                        } else {
                            coroutineScope.launch {
                                itemUiState.value?.itemUi?.toggleExpanded()
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp)
                        .align(Alignment.Top),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = LocalContentColor.current
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                Box {
                    val (expanded: Boolean, setContextExpanded) =
                        remember { mutableStateOf(false) }

                    IconButton(
                        onClick = { setContextExpanded(true) },
                        enabled = itemUiState.value != null
                    ) {
                        Icon(
                            imageVector = Icons.TwoTone.MoreVert,
                            contentDescription = stringResource(id = R.string.more_options),
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { setContextExpanded(false) }
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.reply)) },
                            onClick = {
                                itemUiState.value?.itemUi?.item?.id?.let { onClickReply(ItemId(it)) }
                                setContextExpanded(false)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.TwoTone.Reply,
                                    contentDescription = stringResource(id = R.string.reply)
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(
                                        id = if (itemUiState.value?.itemUi?.isUpvote == true) {
                                            R.string.un_vote
                                        } else {
                                            R.string.upvote
                                        }
                                    )
                                )
                            },
                            onClick = {
                                coroutineScope.launch {
                                    itemUiState.value?.itemUi?.toggleUpvote(
                                        onNavigateLogin = onNavigateLogin
                                    )
                                }

                                setContextExpanded(false)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (
                                        itemUiState.value?.itemUi?.isUpvote == true
                                    ) {
                                        Icons.Filled.ThumbUp
                                    } else {
                                        Icons.TwoTone.ThumbUp
                                    },
                                    contentDescription = stringResource(
                                        id = if (itemUiState.value?.itemUi?.isUpvote == true) {
                                            R.string.un_vote
                                        } else {
                                            R.string.upvote
                                        }
                                    )
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(
                                        id = if (itemUiState.value?.itemUi?.isFollowed == true) {
                                            R.string.unfollow
                                        } else {
                                            R.string.follow
                                        }
                                    )
                                )
                            },
                            onClick = {
                                coroutineScope.launch {
                                    itemUiState.value?.itemUi?.toggleFollowed()
                                }

                                setContextExpanded(false)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (
                                        itemUiState.value?.itemUi?.isFollowed == true
                                    ) {
                                        Icons.Filled.Quickreply
                                    } else {
                                        Icons.TwoTone.Quickreply
                                    },
                                    contentDescription = stringResource(
                                        id = if (itemUiState.value?.itemUi?.isFollowed == true) {
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
                                        id = if (itemUiState.value?.itemUi?.isFlag == true) {
                                            R.string.un_flag
                                        } else {
                                            R.string.flag
                                        }
                                    )
                                )
                            },
                            onClick = {
                                coroutineScope.launch {
                                    itemUiState.value?.itemUi?.toggleFlag(onNavigateLogin)
                                }

                                setContextExpanded(false)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (
                                        itemUiState.value?.itemUi?.isFlag == true
                                    ) {
                                        Icons.Filled.Flag
                                    } else {
                                        Icons.TwoTone.Flag
                                    },
                                    contentDescription = stringResource(
                                        id = if (itemUiState.value?.itemUi?.isFlag == true) {
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

            val htmlText =
                if (isDeleted) {
                    stringResource(id = R.string.deleted)
                } else {
                    itemUiState.value?.itemUi?.item?.text ?: ""
                }

            val annotatedText: AnnotatedString =
                rememberAnnotatedString(htmlText = htmlText)

            val annotatedTextState: State<AnnotatedString> =
                rememberUpdatedState(annotatedText)

            // context must be wrapped or onClick handler is not called
            val contextState: State<Context> =
                rememberUpdatedState(LocalContext.current)

            if (itemUiState.value?.itemUi?.isExpanded == true) {
                SelectionContainer {
                    ClickableTextBlock(
                        text = annotatedText,
                        lines = 2,
                        onClick = { offset ->
                            if (
                                annotatedTextState.value.onClick(
                                    contextState.value,
                                    offset = offset
                                ).not()
                            ) {
                                coroutineScope.launch {
                                    itemUiState.value?.itemUi?.toggleExpanded()
                                }
                            }
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        overflow = TextOverflow.Ellipsis,
                        minHeight = true,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                ClickableTextBlock(
                    text = annotatedText,
                    lines = 2,
                    onClick = {
                        coroutineScope.launch {
                            itemUiState.value?.itemUi?.toggleExpanded()
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (itemUiState.value?.itemUi?.isExpanded?.not() == true &&
                (itemUiState.value?.itemUi?.kids?.size ?: 0) > 0
            ) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "${itemUiState.value?.itemUi?.kids?.size}",
                        maxLines = 1
                    )
                }
            } else {
                Icon(
                    imageVector = if (itemUiState.value?.itemUi?.isExpanded == true) {
                        Icons.TwoTone.ExpandLess
                    } else {
                        Icons.TwoTone.ExpandMore
                    },
                    contentDescription = stringResource(id = R.string.expand),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}
