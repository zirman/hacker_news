package com.monoid.hackernews.repo

import com.monoid.hackernews.api.ItemId
import com.monoid.hackernews.api.getBestStories
import com.monoid.hackernews.room.BestStoryDb
import com.monoid.hackernews.room.BestStoryDao
import io.ktor.client.HttpClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BestStoryRepo(
    private val httpClient: HttpClient,
    private val bestStoryDao: BestStoryDao,
) : OrderedItemRepo {
    override fun getRepoItems(): Flow<List<OrderedItem>> {
        return bestStoryDao.getBestStories()
            .map { bestStories ->
                bestStories.map {
                    OrderedItem(
                        itemId = ItemId(it.itemId),
                        order = it.order,
                    )
                }
            }
    }

    override suspend fun updateRepoItems() {
        try {
            bestStoryDao.replaceBestStories(
                httpClient.getBestStories().mapIndexed { order, storyId ->
                    BestStoryDb(
                        itemId = storyId,
                        order = order,
                    )
                }
            )
        } catch (error: Throwable) {
            if (error is CancellationException) throw error
        }
    }
}
