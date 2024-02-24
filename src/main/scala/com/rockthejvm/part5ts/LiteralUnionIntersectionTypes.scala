package com.rockthejvm.part5ts

object LiteralUnionIntersectionTypes {

  // 1 - Literal types (from Scala 3) - introduced by scala 2.13
  val aNumber = 3
  val three: 3 = 3

  def passNumber(n: Int) = println(n)
  passNumber(45) // ok
  passNumber(three) // ok, 3 <: Int

  def passStrick(n: 3) = println(n)
  passStrick(3) // ok
  passStrick(three) // ok, 3 <: 3
  // passStrick(45) // not ok, 45 <: 3 - type mismatch

  // available for strings, doubles, booleans, etc.
  val pi: 3.14 = 3.14
  val truth: true = true
  val favLang: "Scala" = "Scala"

  // literal types can be used as type arguments (just like any other type)
  def doSomethingWithYourLife(meaning: Option[42]) = meaning.foreach(println)

  // 2 - Union Types
  val truthor42: Boolean | Int = 43

  def ambivalentMethod(arg: String | Int) = arg match {
    case _: String => println("I am a string")
    case _: Int    => println("I am an integer")
  } // PM complete

  val theNumber = ambivalentMethod(56)
  val aString = ambivalentMethod("Scala")

  // type inference chooses a LCA of the two types instead of the String | Int
  val stringOrInt = if (43 > 0) "a string" else 45
  val stringOrInt_v2: String | Int = if (43 > 0) "a string" else 45

  // union types + nulls
  type Maybe[T] = T | Null // not Null

  def handleMaybe(someValue: Maybe[String]) = {
    if (someValue != null) someValue.length // flow typing
    else 0
  }

  type ErrorOr[T] = T | "error"
//  def handleResource(arg: ErrorOr[Int]): Unit =
//    if(arg != "error") println(arg + 1) // flow typing doesn't work
//    else println(s"Resource: $arg")

  // 3 - Intersection Types
  class Animal
  trait Carnivore
  class Crocodile extends Animal with Carnivore

  val carnivoreAnimal: Animal & Carnivore = new Crocodile

  trait Gadget {
    def use(): Unit
  }
  trait Camera extends Gadget {
    def takePicture() = println("Smile!")
    override def use(): Unit = println("Snap")
  }

  trait Phone extends Gadget {
    def makePhoneCall() = println("Calling...")
    override def use(): Unit = println("Ring ring")
  }

  def useSmartDevice(device: Camera & Phone) = {
    device.takePicture()
    device.makePhoneCall()
    device.use() // diamond problem
  }

  class SmartPhone extends Camera with Phone // diamond problem

  def main(args: Array[String]): Unit = {
    useSmartDevice(new SmartPhone)
  }

}
