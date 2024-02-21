package com.rockthejvm.part5ts

object TypeMembers {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal // upper bounded type member (Animal or a subtype)
    type SuperBoundedAnimal >: Dog <: Animal // lower and upper bounded type member
    type AnimalAlias = Cat // type alias
    type AnimalCage[T <: Animal] // type member with a type parameter
    type NestedOption =
      List[Option[Option[Int]]] // often used to alias complex/nested types
  }

  // using type members
  val ac = new AnimalCollection
  val anAnimal: ac.AnimalType = ???

  //val cat: ac.BoundedAnimal = new Cat // code does not compile because BoundedAnimal might be Dog
  val cat: ac.AnimalAlias = new Cat // compiles
  val aDog: ac.SuperBoundedAnimal = new Dog // compiles

  def main(args: Array[String]): Unit = {}

}
