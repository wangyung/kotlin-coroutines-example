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
            "thread: $threadRunningTime ms in threads\n" +
            "using $coroutineRunningTime ns in coroutines\n"
    )
}


fun useThread(): Long =
    measureTimeMillis {
        val threadMap = mutableMapOf<String, Int>()
        val jobs = List(10000) {
            try {
                thread {
                    Thread.sleep(1000)
                    if (!threadMap.containsKey(Thread.currentThread().name)) {
                        threadMap[Thread.currentThread().name] = it
                    }
                    print(".")
                }
            } catch (e: OutOfMemoryError) {
                print("[OOM] oops!\n")
            }
        }
        jobs.forEach { (it as? Thread)?.join() }
        print("\nThe number of threads: ${threadMap.size}\n")
    }

suspend fun useCoroutines(): Long =
        measureTimeMillis {
            val threadMap = mutableMapOf<String, Int>()
            val jobs = List(10000) {
                launch {
                    delay(1000)
                    if (!threadMap.containsKey(Thread.currentThread().name)) {
                        threadMap[Thread.currentThread().name] = it
                    }
                    print(".")
                }
            }
            jobs.forEach { it.join() }
            print("\nThe number of threads of coroutines: ${threadMap.size}\n")
        }

