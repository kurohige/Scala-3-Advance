package com.rockthejvm.part5ts

object PathDependentTypes {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def process(arg: Inner): Unit = println(arg)
    def processGeneral(arg: Outer#Inner): Unit = println(arg)
  }

  val outer = new Outer
  val inner =
    new outer.Inner // outer-Inner is a separate TYPE = path-dependent type

  val outerA = new Outer
  val outerB = new Outer
  // val inner2: outerA.Inner = new outerB.Inner // different types because of different outer instances
  val innerA = new outerA.Inner
  val innerB = new outerB.Inner

  // outerA.process(innerB) // same type missmatch
  outer.process(inner)

  // parent-type: Outer#Inner
  outerA.processGeneral(innerA) // Ok
  outerB.processGeneral(
    innerB
  ) // Ok, outerB.Inner is a subtype of Outer#Inner <:

  /*
    Why:
    - type-checking/type inference, e.g. Akka Streams: Flow[Int, Int, NotUsed]#Repr
    - type-level programming
   */

  // methods with dependent types: return a different COMPILE-TIME type depending on the argument
  // no need for generics
  trait Record {
    type Key
    def defaultValue: Key
  }

  class StringRecord extends Record {
    override type Key = String
    override def defaultValue = ""
  }

  class IntRecord extends Record {
    override type Key = Int
    override def defaultValue = 0
  }

  // user-facing API
  def getDefaultIdentifier(record: Record): record.Key = record.defaultValue

  val aString: String = getDefaultIdentifier(new StringRecord) // a string
  val anInt: Int = getDefaultIdentifier(new IntRecord) // an int

  def main(args: Array[String]): Unit = {}

}
