package com.monoid.hackernews.data

import com.monoid.hackernews.api.ItemId
import com.monoid.hackernews.api.getJobStories
import com.monoid.hackernews.room.JobStoryDao
import com.monoid.hackernews.room.JobStoryDb
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class JobStoryRepository(
    private val httpClient: HttpClient,
    private val jobStoryDao: JobStoryDao,
) : Repository<OrderedItem> {
    override fun getItems(): Flow<List<OrderedItem>> {
        return jobStoryDao.getJobStories()
            .map { jobStories ->
                jobStories.map {
                    OrderedItem(
                        itemId = ItemId(it.itemId),
                        order = it.order,
                    )
                }
            }
    }

    override suspend fun updateItems() {
        jobStoryDao.replaceJobStories(
            httpClient.getJobStories().mapIndexed { order, storyId ->
                JobStoryDb(
                    itemId = storyId,
                    order = order,
                )
            }
        )
    }
}
