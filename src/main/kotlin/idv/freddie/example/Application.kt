package idv.freddie.example

import kotlin.coroutines.experimental.*

fun log(message: String) = println("[${Thread.currentThread().name}] $message")

var continuation: Continuation<Int>? = null

class SimpleCoroutine<T>(override val context: CoroutineContext) : Continuation<T> {

    override fun resume(value: T) {
        log("[POINT12] resume in SimpleCoroutine2")
    }

    override fun resumeWithException(exception: Throwable) {
        throw exception
    }

    fun startCoroutine(block: suspend () -> T) {
        block.startCoroutine(this)
    }
}

fun main(args: Array<String>) {

    // Start a coroutine
    SimpleCoroutine<Unit>(EmptyCoroutineContext).startCoroutine {
        log("[POINT1] Before invoking a()")
        val a = a()
        log("[POINT2] Result is $a")
    }

    5.downTo(0).forEach {
        log("[POINT3] it: $it")
        continuation?.resume(it)
    }
    log("[POINT4] Leaving main()")
}

suspend fun a(): Int {
    log("[POINT5] entering a")
    val result = b()
    log("[POINT6] leaving b")
    return result
}

suspend fun b(): Int {
    log("[POINT7] entering b()")
    while (true) {
        log("[POINT8] before suspend")
        val i: Int = suspendCoroutine { cont ->
            log("[POINT9] assign continuation")
            continuation = cont
        }
        log("[POINT10] After suspend, i = $i")
        if (i == 0) {
            log("[POINT11] leaving b()")
            return 0
        }
    }
}
