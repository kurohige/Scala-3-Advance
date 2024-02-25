package com.rockthejvm.part5ts

object FBoundedPolymorphism {

  object Problem {
    trait Animal {
      def breed: List[Animal]
    }

    class Cat extends Animal {
      override def breed: List[Animal] = List(new Cat, new Dog) // <- problem!!
    }

    class Dog extends Animal {
      override def breed: List[Animal] =
        List(new Dog, new Dog, new Dog) // List[Dog]
    }
    /// losing type safety
  }

  object NaiveSolution {
    trait Animal {
      def breed: List[Animal]
    }

    class Cat extends Animal {
      override def breed: List[Cat] = List(new Cat, new Cat)
    }

    class Dog extends Animal {
      override def breed: List[Animal] =
        List(new Dog, new Dog, new Dog) // List[Dog]
    }
    // I have to write the proper type signatures
    // problem: want the compiler to help
  }

  object FBP {
    trait Animal[A <: Animal[A]] { // recursive type F-Bounded Polymorphism
      def breed: List[A]
    }

    class Cat extends Animal[Cat] {
      override def breed: List[Cat] = List(new Cat, new Cat)
    }

    class Dog extends Animal[Dog] {
      override def breed: List[Dog] =
        List(new Dog, new Dog, new Dog) // List[Dog]
    }
    // type safety
  }

  // example: some ORM Libraries
  trait Entity[E <: Entity[E]] // recursive type F-Bounded Polymorphism
  // example: java sorting library
  class Person extends Comparable[Person] { // F-Bounded Polymorphism
    override def compareTo(o: Person): Int = 0
  }

  // FBP + self types
  object FBPSelfTypes {
    trait Animal[A <: Animal[A]] { // recursive type F-Bounded Polymorphism
      self: A =>
      def breed: List[A]
    }

    class Cat extends Animal[Cat] {
      override def breed: List[Cat] = List(new Cat, new Cat)
    }

    class Dog extends Animal[Dog] {
      override def breed: List[Dog] =
        List(new Dog, new Dog, new Dog) // List[Dog]
    }
    // type safety

//    class Crocodile extends Animal[Dog] { // will not compile as it must extend itself
//      override def breed: List[Dog] = List(new Dog, new Dog)
//    }

    // can go one level deeper
    trait Fish extends Animal[Fish]
    class Cod extends Fish {
      override def breed: List[Fish] = List(new Cod, new Cod)
    }

    class Shark extends Fish {
      override def breed: List[Animal[Fish]] = List(new Cod)
    }

    // solution level 2
    trait FishL2[A <: FishL2[A]] { // recursive type F-Bounded Polymorphism
      self: A =>
      def breed: List[A]
    }

    class Tuna extends FishL2[Tuna] {
      override def breed: List[Tuna] = List(new Tuna, new Tuna)
    }

//    class Salmon extends FishL2[Tuna] { // not ok as it must extend itself
//      override def breed: List[Tuna] = List(new Tuna, new Tuna)
//    }

  }

  def main(args: Array[String]): Unit = {}
}
