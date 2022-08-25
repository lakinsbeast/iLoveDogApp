package com.sagirov.ilovedog.Utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class FlowUtils {

    @Composable
    fun <T> Flow<T>.flowWithLifecycleStateInAndCollectAsState(
        scope: CoroutineScope, initial: T? = null, context: CoroutineContext = EmptyCoroutineContext
    ): State<T?> {
        val lifecycleOwner = LocalLifecycleOwner.current
        return remember(this, lifecycleOwner) {
            this
                .flowWithLifecycle(
                    lifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                ).stateIn(
                    scope = scope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = initial
                )
        }.collectAsState(context)
    }
}