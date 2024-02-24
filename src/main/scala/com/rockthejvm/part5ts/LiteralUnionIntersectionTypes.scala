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

  def main(args: Array[String]): Unit = {}

}
