package com.rockthejvm.part5ts

object AdvanceInheritance {

  // 1 - composite types can be used on their own
  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Stream[T] {
    def foreach(f: T => Unit): Unit
  }

  trait Closeable {
    def close(status: Int): Unit
  }

  def processStream[T](
      stream: Stream[T] with Closeable with Writer[T]
  ): Unit = {
    stream.foreach(stream.write)
    stream.close(0)
  }

  // 2 - diamond problem
  trait Animal {
    def name: String
  }

  trait Lion extends Animal {
    override def name: String = "lion"
  }
  trait Tiger extends Animal {
    override def name: String = "tiger"
  }

  class Liger extends Lion with Tiger

  def demoLiger(): Unit = {
    val liger = new Liger
    println(liger.name) // tiger
  }

  // 3 - the super problems

  trait Cold {
    def print: Unit = println("cold")
  }

  trait Green extends Cold {
    override def print: Unit = {
      println("green")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("blue")
      super.print
    }
  }

  class Red {
    def print: Unit = println("red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }

  def demoColorInheritance(): Unit = {
    val color = new White
    color.print
  }

  def main(args: Array[String]): Unit = {
    demoLiger()
    demoColorInheritance()
  }

}
