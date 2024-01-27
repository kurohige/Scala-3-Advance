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

  def main(args: Array[String]): Unit = {}

}
