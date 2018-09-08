package idv.freddie.example

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = runBlocking {
    
    val threadRunningTime = useThread()
    val coroutineRunningTime = useCoroutines()

    print(
            "thread: $threadRunningTime\n" +
            "coroutines: $coroutineRunningTime\n"
    )
}


fun useThread(): Long =
    measureTimeMillis {
        val jobs = List(10000) {
            thread {
                Thread.sleep(1000)
                print(".\n")
            }
        }
        jobs.forEach { it.join() }
    }

suspend fun useCoroutines(): Long =
        measureTimeMillis {
            val jobs = List(10000) {
                launch {
                    delay(1000)
                    print(".\n")
                }
            }
            jobs.forEach { it.join() }
        }

