package com.rockthejvm.part3async

object JVMConcurrencyIntro {

  def basicThreads(): Unit = {
    val runnable = new Runnable {
      override def run(): Unit = {
        println("waiting...")
        Thread.sleep(1000)
        println("running on some thread")
      }
    }

    // threads on the JVM
    val aThread = new Thread(runnable)
    aThread.start() // gives the signal to the JVM to start a JVM thread
    // JVM thread == OS thread (soon to change via project loom)
    aThread.join() // block until aThread finishes running
  }

  // order of operations is not guaranteed
  def orderOfExecution(): Unit = {
    val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
    val threadGoodbye = new Thread(() =>
      (1 to 5).foreach(_ => println("goodbye"))
    )
    threadHello.start()
    threadGoodbye.start()

  }

  // executors
  def demoExecutors(): Unit = {
    import java.util.concurrent.Executors

    val threadPool = Executors.newFixedThreadPool(4)
    // submit a computation
    threadPool.execute(() => println("something in the thread pool"))
    threadPool.execute(() => println("something else"))

    threadPool.shutdown()
  }
  def main(args: Array[String]): Unit = {
    //basicThreads()
    orderOfExecution()
  }

}
