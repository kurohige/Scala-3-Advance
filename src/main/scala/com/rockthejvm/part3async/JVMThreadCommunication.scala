package com.rockthejvm.part3async

object JVMThreadCommunication {

  def main(args: Array[String]): Unit = {
    ProdConsV2.start()
  }

}
// example: the producer-consumer problem
class SimpleContainer {
  private var value: Int = 0

  def isEmpty: Boolean = value == 0
  def get = {
    val result = value
    value = 0
    result
  }
  def set(newValue: Int) = value = newValue
}

// producer part 1: one producer, one consumer
object ProdConsV1 {
  def start(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      // busy waiting
      while (container.isEmpty) {
        println("[consumer] actively waiting...")
      }

      println(s"[consumer] I have consumed ${container.get}")
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42
      println(s"[producer] I am producing, after LONG work, the value $value")
      container.set(value)
    })

    consumer.start()
    producer.start()
  }
}

// wait + notify
object ProdConsV2 {
  def start(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")

      container
        .synchronized { // block all other threads trying to "lock" this object
          // thread-safe code
          if (container.isEmpty)
            container.wait() // release the lock + suspend the thread
          println(s"[consumer] I have consumed ${container.get}")
        }

    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42

      container
        .synchronized { // block all other threads trying to "lock" this object
          println(
            s"[producer] I am producing, after LONG work, the value $value"
          )
          container.set(value)
          container.notify() // signal ONE sleeping thread on this object
        }

    })

    consumer.start()
    producer.start()
  }
}
