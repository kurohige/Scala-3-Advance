package com.rockthejvm.part4context

// special import
import scala.language.implicitConversions

object ImplicitConversions {

  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name"
  }

  val daniel = Person("Daniel")
  val danielSayHi = daniel.greet // "Hi, my name is Daniel"

  given string2Person: Conversion[String, Person] with
    override def apply(name: String): Person = Person(name)

  val danielSaysHi_v2 = "Daniel".greet // "Hi, my name is Daniel", automatically converted to a Person by the compiler

  def processPerson(person: Person): String =
    if(person.name.startsWith("J")) "Ok"
    else "Not ok"

  val isJaneOk = processPerson("Jane") // "Ok", automatically converted to a Person by the compiler

  /*
    - auto-box types
    - use multiple types for the same code interchangeably
   */

  def main(args: Array[String]): Unit = {}

}
