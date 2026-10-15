
package com.karthik.pro.engr.github.api.android_interview_mastery.startup

import com.karthik.pro.engr.github.api.android_interview_mastery.domain.auth.TokenStoreInitializer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStoreStartupCoordinator @Inject constructor(
    private val initializer: TokenStoreInitializer
) {

    // Owns the shared state transitions.
    private val mutex = Mutex()

    // This coordinator is app-scoped, so this work can continue
    // even if an individual caller is canceled.
    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    private var isInitialized = false
    private var activeAttempt: CompletableDeferred<Unit>? = null

    suspend fun ensureInitialized() {
        var isOwner = false

        val attempt = mutex.withLock {
            if (isInitialized) {
                return
            }

            activeAttempt ?: CompletableDeferred<Unit>().also { newAttempt ->
                activeAttempt = newAttempt
                isOwner = true
            }
        }

        // Only the caller that created the attempt starts the work.
        if (isOwner) {
            scope.launch {
                performInitialization(attempt)
            }
        }

        // Owners and joiners observe the same result.
        attempt.await()
    }

    private suspend fun performInitialization(
        attempt: CompletableDeferred<Unit>
    ) {
        try {
            initializer.initialize()

            mutex.withLock {
                isInitialized = true

                if (activeAttempt === attempt) {
                    activeAttempt = null
                }
            }

            // Release all callers waiting on this attempt.
            attempt.complete(Unit)

        } catch (exception: CancellationException) {
            clearActiveAttempt(attempt)
            attempt.completeExceptionally(exception)
            throw exception

        } catch (exception: Exception) {
            clearActiveAttempt(attempt)
            attempt.completeExceptionally(exception)
        }
    }

    private suspend fun clearActiveAttempt(
        attempt: CompletableDeferred<Unit>
    ) {
        mutex.withLock {
            if (activeAttempt === attempt) {
                activeAttempt = null
            }
        }
    }
}
