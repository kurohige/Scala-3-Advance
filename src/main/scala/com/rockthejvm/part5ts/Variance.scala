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

  def main(args: Array[String]): Unit = {}

}
