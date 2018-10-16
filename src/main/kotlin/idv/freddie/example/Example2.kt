package idv.freddie.example

import java.util.concurrent.ThreadLocalRandom
import kotlin.concurrent.thread
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine
import kotlin.math.abs

fun log(message: String) = println("[${Thread.currentThread().name}] $message")

var continuation: Continuation<Int>? = null

const val useThread = true

fun runExample2() {
    // Start a coroutine
    SimpleCoroutine<Unit>().startCoroutine {
        log("[POINT1] Before invoking a()")
        val resultA = a()
        log("[POINT2] Result is $resultA")
    }

    if (!useThread) {
        print("[POINTXXX] before for loop")
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
                    log("[POINT9] before resume")
                    cont.resume(sleepTime.toInt())
                    log("[POINT10] leaving thread")
                }
            } else {
                continuation = cont
            }
        }
        log("[POINT11] After suspend, i = $i")
        counter += i
        if (counter > 5000) {
            log("[POINT12] leaving b()")
            continuation = null
            return counter
        }
    }
}
