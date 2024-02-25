package com.rockthejvm.part5ts

object SelfTypes {

  // self-types are a way to declare dependencies between traits
  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer {
    self: Instrumentalist => // self-type: whoever implements Singer MUST also implement Instrumentalist
    // name can be anything, usually called "self"
    // DO NOT confuse this with Lambdas

    // rest of your API
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  // not Ok,because I've not extended Instrumentalist
//  class Vocalist extends Singer {
//    override def sing(): Unit = ???
//  }
  val jamesHetfield = new Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("guitar solo")
  }

  val ericClapton = new Guitarist with Singer {
    override def sing(): Unit = ???
  }

  // self-types vs inheritance
  class A
  class B extends A // B is an A

  trait T
  trait S { self: T => } // S requires a T

  // self-types for DI = "cake pattern"
  class Component {
    // main general API
  }

  class ComponentA extends Component {
    // specific API
  }

  class ComponentB extends Component {
    // specific API
  }
  class DependentComponent(val component: Component) //regular DI

  // cake pattern
  trait ComponentLayer1 {
    // API
    def actionLayer1(x: Int): String
  }
  trait ComponentLayer2 { self: ComponentLayer1 =>
    // API
    def actionLayer2(s: String): Int
  }

  trait Application { self: ComponentLayer1 with ComponentLayer2 =>
    // API
  }

  // example: a photo taking app API in the style of instragram
  // layer 1 - small components
  trait Picture extends ComponentLayer1 {
    def takePicture(): String
  }
  trait Stats extends ComponentLayer1 {
    def showStats(picture: String): String
  }

  // layer 2 - compose the app
  trait ProfilePage extends ComponentLayer2 with Picture {
    def showProfile(user: String): String
  }
  trait Analytics extends ComponentLayer2 with Stats {
    def showMostPopular(): String
  }

  // layer 3 - the app
  trait AnalyticsApp extends Application with Analytics
  // dependencies are explicit in layers, like baking a cake
  // when you put the pieces together, you can pick a possible implementation from each layer

  // self-types: prserve the "this" instance
  class SingerWithInnerClass {
    self => // self-type with no type requirement, self == this
    class Voice {
      def sing() =
        this.toString // this == the Voice instance, use "self" to refer to the outer instance
    }
  }

  // cyclical dependencies
  // class X extends Y
  // class Y extends X this will not compile

  // cyclical dependencies with self-types
  trait X { self: Y => }
  trait Y { self: X => }
  trait Z extends X with Y // this will compile

  def main(args: Array[String]): Unit = {}

}
