package com.rockthejvm.part1as

import scala.annotation.tailrec

object Recap {

  // values, types , expressions
  val aCondition = false // vals are constant
  val anIfExpression = if (aCondition) 42 else 65 // if is an expression

  // code blocks
  val aCodeBlock = {
    if (aCondition) 74
    56
  }

  // types: Unit, Int, String, Boolean, Char, Double, Float, Long, Short, Byte
  // Unit = () == "Void" in other languages
  val theUnit = println("Hello, Scala")

  // functions
  def aFunction(x: Int) = x + 1

  // recursion: stack and tail
  @tailrec
  def factorial(n: Int, accumulator: Int): Int =
    if (n <= 0) accumulator
    else factorial(n - 1, n * accumulator)

  val fact10 = factorial(10, 1)

  // object-oriented programming
  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog // subtyping polymorphism

  // abstract classes
  trait Carnivore {
    infix def eat(a: Animal): Unit
  }

  class crocodile extends Animal with Carnivore {
    override infix def eat(a: Animal): Unit = println("Crunch!")
  }

  // method notations
  val aCroc = new crocodile
  aCroc.eat(aDog)
  aCroc eat aDog // infix notation = object method argument

  // anonymous classes
  val aCarnivore = new Carnivore {
    override infix def eat(a: Animal): Unit = println("Roar!")
  }

  // generics
  abstract class LList[A] {
    // type A is known inside the implementation of these methods
  }

  // singletons and companions
  object LList // companion object, used for instance-independent methods (static)

  // case classes
  case class Person(name: String, age: Int)

  // enums
  enum BasicColor {
    case Red, Green, Blue
  }

  // exceptions and try/catch/finally
  def throwSomeException(): Int = throw new RuntimeException

  val aPotentailFailure = try {
    throwSomeException()
  } catch {
    case e: Exception => 43
  } finally {
    println("some logs")
  }

  // functional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(x: Int): Int = x + 1
  }

  val two = incrementer(1) // 2

  // lambdas
  val anonymousIncrementer = (x: Int) => x + 1

  // hofs = higher-order functions
  val anIncrementerList = List(1,2,3).map(anonymousIncrementer) // List(2,3,4)

  // map, flatMap, filter

  // for-comprehensions
  val pairs = for {
    num <- List(1,2,3) // if condition
    char <- List('a', 'b', 'c')
  } yield num + "-" + char

  // Scala collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples

  // options, try
  val anOption: Option[Int] = Option(2) // Some(2)

  // pattern matching
  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case _ => x + "th"
  }

  val bob = Person("Bob", 43)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }

  // braceless syntax
  val pairs_v2 = for
    num <- List(1,2,3)
    char <- List('a', 'b', 'c')
  yield num + "-" + char

  def main(args: Array[String]): Unit = {

  }

}
