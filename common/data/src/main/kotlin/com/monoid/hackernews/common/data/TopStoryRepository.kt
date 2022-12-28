package com.monoid.hackernews.common.data

import com.monoid.hackernews.common.api.ItemId
import com.monoid.hackernews.common.api.getTopStories
import com.monoid.hackernews.common.room.TopStoryDao
import com.monoid.hackernews.common.room.TopStoryDb
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopStoryRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val topStoryDao: TopStoryDao,
) : Repository<OrderedItem> {
    override fun getItems(): Flow<List<OrderedItem>> {
        return topStoryDao.getTopStories()
            .map { topStories ->
                topStories.map {
                    OrderedItem(
                        itemId = ItemId(it.itemId),
                        order = it.order,
                    )
                }
            }
    }

    override suspend fun updateItems() {
        topStoryDao.replaceTopStories(
            httpClient.getTopStories().mapIndexed { order, storyId ->
                TopStoryDb(
                    itemId = storyId,
                    order = order,
                )
            }
        )
    }
}