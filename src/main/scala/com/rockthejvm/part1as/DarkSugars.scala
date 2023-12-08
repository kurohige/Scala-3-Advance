package com.rockthejvm.part1as

import scala.util.Try

object DarkSugars {

  // 1 - sugar for methods with one argument
  def singleArgMethod(arg: Int): Int = arg + 1

  val aMethodCall = singleArgMethod {
    // code
    42
  }

  val aMethodCall_v2 = singleArgMethod(42)


  // example : Try, Future
  val aTryInstance = Try {
    throw new RuntimeException("Scala rocks")
  }

  // with hofs
  val anIncrementedList = List(1,2,3).map { x =>
    x + 1
  }

  // 2 - single abstract method pattern
  trait Action {
    def act(x: Int): Int
  }

  val anActionInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  val anActionInstance_v2: Action = (x: Int) => x + 1

  // example: Runnable
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Scala rocks")
  })

  val aSweeterThread = new Thread(() => println("Scala rocks"))

  // 3 - the :: and #:: methods ending in a colon are right associative
  val aList = List(1,2,3)
  val aPrependedList = 0 :: aList // List(0,1,2,3)
  val aBigList = 0 :: 1 :: 2 :: 3 :: List(4,5) // List(0,1,2,3,4,5)

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this // actual implementation here
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  // 4 - multi-word method naming
  class Talker(name: String) {
    infix def `and then said`(message: String): Unit = println(s"$name then said $message")
  }

  val daniel = new Talker("Daniel")
  val danielStatement =  daniel `and then said` "Scala rocks"

  // example: HTTP libraries
  object `Content-Type`{
    val `application/json` = "application/json"
  }

  // 5 - infix types
  import scala.annotation.targetName
  @targetName("Arrow")// for more readable bytecode + java interop
  infix class -->[A,B]
  val compositeTypes: Int --> String = new -->[Int, String]

  // 6 - update()
  val anArray = Array(1,2,3,4)
  anArray.update(2, 45)
  anArray(2) = 45 // same as above

  // 7 - mutable fields
  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member = internalMember // getter
    def member_=(value: Int): Unit = internalMember = value // setter
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // aMutableContainer.member_=(42)

  // 8 - variable arguments( varargs )
  def methodWithVarArgs(args: Int*)= 45

  val callWithZeroArgs = methodWithVarArgs()
  val callWithOneArg = methodWithVarArgs(78)
  val callWithTwoArgs = methodWithVarArgs(78, 45)
  val aCollection = List(1,2,3)

  def main(args: Array[String]): Unit = {

  }

}
