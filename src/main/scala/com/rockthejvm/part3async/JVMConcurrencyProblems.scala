package com.rockthejvm.part3async

object JVMConcurrencyProblems {

  def runInParallel(): Unit = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()

    println(x) // race condition
  }

  case class BankAccount(var amount: Int) {
    def withdraw(money: Int) = this.amount -= money
  }

  def buy(account: BankAccount, thing: String, price: Int): Unit = {
    account.withdraw(price)
  }

  def buysafe(account: BankAccount, thing: String, price: Int): Unit = {
    account
      .synchronized { // does not allow multiple threads to evaluate this block at the same time
        account.withdraw(price)
      }
  }

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "iPhone12", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000)
        println(s"AHA I've just broken the bank: ${account.amount}")
    }
  }

  /** Exercises
    * 1. create "inception" threads
    *   thread1 -> thread2 -> thread3 -> thread4 -> thread1
    *   print "hello from thread #"
    *   in REVERSE ORDER
    *
    * 2. what's the biggest value possible for x?
    *   var x = 0
    *    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    *
    *  3. sleep fallacy
    */
  // 1- inception threads
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread =
    new Thread(() => {
      if (i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"Hello from thread $i")
    })

  def minMaxX(): Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
    threads.foreach(_.join())
    println(x)
  }

  def demoSleepFallacy(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })

    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(2000)
    awesomeThread.join()
    println(message)
  }

  def main(args: Array[String]): Unit = {}

}
