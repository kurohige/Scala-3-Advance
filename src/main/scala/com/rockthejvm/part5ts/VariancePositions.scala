package com.rockthejvm.part5ts

object VariancePositions {

  /**    1 - type bounds
    */
  class Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  class Cage[
      T <: Animal
  ] // upper bound "<:" means "T must be a subtype of Animal"

  val aCage =
    new Cage[Animal] // OK becuase dog is a subtype of Animal or Dog <: Animal

  class WeirdContainer[
      A >: Animal
  ] // lower bound ">:" means "A must be a supertype of Animal"

  /** 2 - variance positions
    */

  // types of val fields are in COVARIANT position
  // class Vet[-A] { def heal(animal: A): Boolean = true }

  /*
    val garfield = new Cat
    val theVet: Vet[Animal] = new Vet[Animal](garfield) // this is not allowed
    val aDogVet: Vet[Dog] = theVet // possible, theVet is a Vet[Animal]
    val aDog: Dog = aDogVet.favoriteAnimal // must be a dog - type conflict!
   */

  // types of var fields are in COVARIANT position

  // types of var fields are in CONTRAVARIANT position
  // class MutableOption[+T](var contents: T) // this is not allowed

  /*
    val maybeAnimal: MutableOption[Animal] = new MutableOption[Dog](new Dog)
    maybeAnimal.contents = new Cat // type conflict!
   */

  // types of method arguments
  // class MyList[+B] {
  //     def add(element: B): MyList[B] = ???
  // }

  /*
    val animals: MyList[Animal] = new MyList[Cat]
    val biggerListOfAnimals = animals.add(new Dog) // type conflict!
   */

  // method return types are in COVARIANT position
  // abstract class Vet2[-A] {
  //   def rescueAnimal(): A
  // }

  /*
    val vet: Vet2[Animal] = new Vet2[Animal] {
      override def rescueAnimal(): Animal = new Cat
    }
    val lassiesVet: Vet2[Dog] = vet // type conflict!
    val rescueDog: Dog = lassiesVet.rescueAnimal() // must be a dog - type conflict!
   */

  def main(args: Array[String]): Unit = {}

}
