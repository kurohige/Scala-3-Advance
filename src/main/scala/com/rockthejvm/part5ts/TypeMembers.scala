package com.rockthejvm.part5ts

object TypeMembers {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal // upper bounded type member
    type SuperBoundedAnimal >: Dog <: Animal // lower and upper bounded type member
    type AnimalCage[T <: Animal] // type member with a type parameter
  }

  def main(args: Array[String]): Unit = {}

}
