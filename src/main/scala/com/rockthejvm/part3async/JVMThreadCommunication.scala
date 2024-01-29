package com.rockthejvm.part3async

import scala.collection.mutable
import scala.util.Random

object JVMThreadCommunication {

  def main(args: Array[String]): Unit = {
    ProdConsV4.start(4, 2, 5)
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

// insert a larger container
// producer -> [ _ _ _ ] -> consumer
object ProdConsV3 {
  def start(containerCapacity: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]

    val consumer = new Thread(() => {
      val random = new Random(System.nanoTime())

      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println(s"[consumer] I have consumed $x")

          // hey producer, there's space available, are you lazy?
          buffer.notify()
        }

        Thread.sleep(random.nextInt(250))
      }
    })

    val producer = new Thread(() => {
      val random = new Random(System.nanoTime())
      var counter = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == containerCapacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE empty space in the buffer
          val newElement = counter
          counter += 1
          println(s"[producer] I'm producing $counter")
          buffer.enqueue(counter)

          // hey consumer, there's a new value, are you lazy?
          buffer.notify()

        }

        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.join()
  }
}

// large container
object ProdConsV4 {
  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random(System.nanoTime())

      while (true)
        buffer.synchronized {
          /*
            One producer, two consumers
            producer produces value 1 in the buffer
            both consumers are waiting
            producer calls notify, awakens one consumer
            consumer dequeues, calls notify, awakens the other consumer
            the other consumer awakens, tries dequeuing, this will CRASH
           */
          while (buffer.isEmpty) { // buffer full
            println(s"[consumer $id] buffer empty, waiting...")
            buffer.wait()
          }

          // buffer is non-empty
          val newValue = buffer.dequeue()
          println(s"[consumer $id] I have consumed $newValue")

          // notify a producer
          /*
          We need to use notifyAll. Otherwise:
            Scenario: 2 producers, one consumer, capcity = 1
            producer 1 produces value, then waits
            producer 2 sees buffer full, waits
            consumer consumes value, notifies one producer(producer 1)
            consumer sees buffer empty, waits
            producer1 produces a value, calls notify - signal goes to producer 2
            producer1 sees buffer full, waits
            producer2 sees buffer, awaits
            deadlock
           */
          buffer.notifyAll() // signal all sleeping threads on this buffer
        }

      Thread.sleep(random.nextInt(500))
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int)
      extends Thread {
    override def run(): Unit = {
      val random = new Random(System.nanoTime())
      var currentCount = 0

      while (true)
        buffer.synchronized {
          while (buffer.size == capacity) { // buffer full
            println(s"[producer $id] buffer is full, waiting...")
            buffer.wait()
          }

          // buffer is non-full
          println(s"[producer $id] I'm producing $currentCount")
          buffer.enqueue(currentCount)

          // notify a consumer
          buffer.notifyAll() // signal all sleeping threads on this buffer

          currentCount += 1
        }

      Thread.sleep(random.nextInt(500))
    }
  }

  def start(nProducers: Int, nConsumers: Int, containerCapacity: Int): Unit = {
    // start consumers and producers
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val producers =
      (1 to nProducers).map(id => new Producer(id, buffer, containerCapacity))
    val consumers = (1 to nConsumers).map(id => new Consumer(id, buffer))

    producers.foreach(_.start())
    consumers.foreach(_.start())
  }
}
