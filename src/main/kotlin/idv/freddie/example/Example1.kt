package idv.freddie.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

fun runExample1() = runBlocking {
    val threadRunningTime = useThread()
    val coroutineRunningTime = useCoroutines()

    print(
            "using $threadRunningTime ms in threads\n" +
            "using $coroutineRunningTime ms in coroutines\n"
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
                GlobalScope.launch {
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
