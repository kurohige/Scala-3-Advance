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

  def main(args: Array[String]): Unit = {}

}
