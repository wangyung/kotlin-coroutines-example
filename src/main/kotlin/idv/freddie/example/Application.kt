package idv.freddie.example

import kotlin.coroutines.experimental.*

fun log(message: String) = println("[${Thread.currentThread().name}] $message")

var continuation: Continuation<Int>? = null

class SimpleCoroutine<T>(override val context: CoroutineContext) : Continuation<T> {

    override fun resume(value: T) {
        log("resume in SimpleCoroutine")
    }

    override fun resumeWithException(exception: Throwable) {
        throw exception
    }

    fun startCoroutine(block: suspend () -> T) {
        block.startCoroutine(this)
    }
}

fun main(args: Array<String>) {// = runBlocking {

    // Start a coroutine
    val test = SimpleCoroutine<Unit>(EmptyCoroutineContext).startCoroutine {
        log("in launch")
        val a = a()
        log("Result is $a")
    }

    10.downTo(0).forEach {
        log("downTo $it")
        continuation?.resume(it)
    }
    log("Leaving main()")
}

suspend fun a(): Int {
    log("running a()")
    val result = b()
    log("leaving a()")
    return result
}

suspend fun b(): Int {
    log("running b()")
    while (true) {
        log("before suspend")
        val i: Int = suspendCoroutine { cont ->
            log("assign continuation")
            continuation = cont
        }
        log("After suspend, i = $i")
        if (i == 0) {
            log("leaving b()")
            return 0
        }
    }
}
