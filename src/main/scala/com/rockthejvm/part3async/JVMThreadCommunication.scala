package com.rockthejvm.part3async

object JVMThreadCommunication {

  def main(args: Array[String]): Unit = {
    ProdConsV1.start()
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
