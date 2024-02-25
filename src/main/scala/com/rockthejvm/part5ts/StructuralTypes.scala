package com.rockthejvm.part5ts

import reflect.Selectable.reflectiveSelectable

object StructuralTypes {

  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark")
  }

  class Car {
    def makeSound(): Unit = println("vroom")
  }

  val dog: SoundMaker = new Dog // Ok because Dog has a makeSound method
  val car: SoundMaker = new Car // Ok because Car has a makeSound method
  // duck typing

  // type refinements
  abstract class Animal {
    def eat(): String
  }

  type WalkingAnimal = Animal { // refined type
    def walk(): Int
  }

  // why: creating type-safe APIs for existing types following the same structure, but no connection between them
  type JavaCloseable = java.io.Closeable
  class CustomCloseable {
    def close(): Unit = println("I'm closing")
    def closeSilently(): Unit = println("I'm closing silently")
  }

  // solution: structural types
  type UnifiedCloseable = {
    def close(): Unit
  }

  def closeResource(closeable: UnifiedCloseable): Unit = closeable.close()
  val jCloseable = new JavaCloseable {
    override def close(): Unit = println("Closing Java resource")
  }

  val cCloseable = new CustomCloseable

  def main(args: Array[String]): Unit = {
    dog.makeSound() // through reflection (slow)
    car.makeSound()
  }

}
