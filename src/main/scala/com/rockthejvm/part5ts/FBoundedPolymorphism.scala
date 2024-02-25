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

  def main(args: Array[String]): Unit = {}
}
