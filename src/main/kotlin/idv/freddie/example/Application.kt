package idv.freddie.example

import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.thread
import kotlin.coroutines.experimental.*
import kotlin.math.abs

fun log(message: String) = println("[${Thread.currentThread().name}] $message")

var continuation: Continuation<Int>? = null

val useThread = false

fun main(args: Array<String>) {

    // Start a coroutine
    SimpleCoroutine<Unit>().startCoroutine {
        log("[POINT1] Before invoking a()")
        val resultA = a()
        log("[POINT2] Result is $resultA")
    }

    if (!useThread) {
        (4990..5010).forEach {
            continuation?.resume(it) ?: return@forEach
        }
    }

    log("[POINT3] Leaving main()")
}

suspend fun a(): Int {
    log("[POINT4] entering b in a")
    val result = b()
    log("[POINT5] leaving b in a")
    return result
}

suspend fun b(): Int {
    log("[POINT6] entering b()")
    var counter = 0
    while (true) {
        log("[POINT7] before suspend")
        val i: Int = suspendCoroutine { cont ->
            log("[POINT8] in suspendCoroutine")
            if (useThread) {
                thread {
                    val sleepTime = abs(ThreadLocalRandom.current().nextLong()) % 1000
                    Thread.sleep(sleepTime)
                    cont.resume(sleepTime.toInt())
                }
            } else {
                continuation = cont
            }
        }
        log("[POINT9] After suspend, i = $i")
        counter += i
        if (counter > 5000) {
            log("[POINT10] leaving b()")
            continuation = null
            return counter
        }
    }
}
