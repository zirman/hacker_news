package com.monoid.hackernews.domain

import android.net.ConnectivityManager
import com.monoid.hackernews.data.Repository
import com.monoid.hackernews.ui.util.getNetworkConnectivityStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LiveUpdateUseCase<out T>(
    private val connectivityManager: ConnectivityManager,
    private val repository: Repository<T>
) {
    fun getItems(updatePeriod: Long): Flow<List<T>> = flow {
        coroutineScope {
            // start job to update periodically while flow is observed and there is network
            // connectivity
            launch {
                getNetworkConnectivityStateFlow(
                    coroutineScope = this,
                    connectivityManager = connectivityManager,
                ).collectLatest { hasConnectivity ->
                    while (hasConnectivity) {
                        try {
                            repository.updateItems()
                        } catch (error: Throwable) {
                            if (error is CancellationException) throw error
                        }

                        delay(updatePeriod)
                    }
                }
            }

            emitAll(repository.getItems())
        }
    }
}
