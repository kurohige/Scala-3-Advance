package com.rockthejvm.part5ts

object Variance {

  class Animal
  class Dog(name: String) extends Animal

  // variance question for List: if Dog extends Animal, then should a List[Dog] extend List[Animal]?
  // for List, Yes - List is covariant
  val lassie = new Dog("Lassie")
  val hachi = new Dog("Hachi")
  val laika = new Dog("Laika")

  val anAnimal: Animal =
    lassie // ok, Dog <: Animal "<:" means "is a subtype of"

  val myDogs: List[Animal] = List(
    lassie,
    hachi,
    laika
  ) // List is COVARIANT: a list of dogs is a list of animals

  class MyList[+A] // adding a "+" makes MyList covariant

  val aListOfAnimals: MyList[Animal] =
    new MyList[Dog] // ok, MyList is covariant

  // if NO, then the type is INVARIANT
  trait semiGroup[A] { // no marker = INVARIANT
    def combine(x: A, y: A): A
  }

  // java generics are INVARIANT
  //val aJavaList: java.util.ArrayList[Animal] = new java.util.ArrayList[Dog] // not ok, type mismatch: java generics are all INVARIANT

  // Hell NO - CONTRAVARIANT
  trait Vet[-A] {
    def heal(x: A): Boolean
  }

  val myVet: Vet[Dog] = new Vet[Animal] {
    override def heal(x: Animal): Boolean = {
      println("I'm healing an animal")
      true
    }
  }
  /*
    Rule of thumb:
    - if your type PRODUCES or RETRIEVES a value( e.g. a list), then it should be COVARIANT
    - if your type ACTS ON or CONSUMES a value (e.g. a semiGroup), then it should be CONTRAVARIANT
    - if your type does neither, then it should be INVARIANT
   */

  /** Exercises
    */
  // 1 - which type should be invariant, covariant, contravariant
  class RandomGenerator[+A] // covariant
  class MyFunction1[-A, +B] // contravariant in A, covariant in B
  class MyOption[+A] // covariant
  class JSONSerializer[
      -A
  ] // contravariant because it consumes values and turns them into strings

  // 2 - add variance modifiers to this "Library"
  abstract class LList[+A] {
    def head: A
    def tail: LList[A]
  }

  case object EmptyList extends LList[Nothing] {
    override def head = throw new NoSuchElementException
    override def tail = throw new NoSuchElementException
  }

  case class Cons[+A](override val head: A, override val tail: LList[A])
      extends LList[A]

  val aList: LList[Int] = EmptyList // fine
  val anotherList: LList[String] = EmptyList // also fine
  // Nothing <: A, then LList[Nothing] <: LList[A] - LList is covariant

  def main(args: Array[String]): Unit = {}

}
