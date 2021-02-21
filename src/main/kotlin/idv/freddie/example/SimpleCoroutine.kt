package idv.freddie.example

import idv.freddie.example.log
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

class SimpleCoroutine<T>(
    override val context: CoroutineContext = EmptyCoroutineContext
) : Continuation<T> {

    override fun resumeWith(result: Result<T>) {
        log("[POINT13] resume in SimpleCoroutine")
    }

    fun startCoroutine(block: suspend () -> T) {
        block.startCoroutine(this)
    }
}
