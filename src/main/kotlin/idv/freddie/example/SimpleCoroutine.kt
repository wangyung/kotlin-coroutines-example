package idv.freddie.example

import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.EmptyCoroutineContext
import kotlin.coroutines.experimental.startCoroutine

class SimpleCoroutine<T>(override val context: CoroutineContext = EmptyCoroutineContext) : Continuation<T> {

    override fun resume(value: T) {
        log("[POINT12] resume in SimpleCoroutine")
    }

    override fun resumeWithException(exception: Throwable) {
        throw exception
    }

    fun startCoroutine(block: suspend () -> T) {
        block.startCoroutine(this)
    }
}
