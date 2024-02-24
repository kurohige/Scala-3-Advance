package com.rockthejvm.part5ts

object OpaqueTypes {

  object SocialNetwork {
    // some data structures = "Domain"
    opaque type Name = String
    
    object Name {
      def apply(str: String): Name = str
    }
    
    extension (name: Name) {
      def length: Int = name.length
      def shorterThan(n: Int): Boolean = name.length < n
    }

    // inside, name <-> String
    def addFriend(person1: Name, person2: Name): Boolean ={
      person1.length == person2.length // use the entire String API
    }

  }

  // outside SocialNetwork, Name and String are Not related
  import SocialNetwork.*
  // val name: Name = "Daniel" // not possible and will not compile
  // Why: you don't need (or want) to have access the entire String API for the name type.

  object Graphics {
    opaque type Color = Int // in hex
    opaque type ColorFilter <: Color = Int // in hex

    val red: Color = 0xFF0000
    val green: Color = 0x00FF00
    val blue: Color = 0x0000FF
    val halfTransparency: ColorFilter = 0x80 // 50% transparency
  }

  import Graphics.*
  case class OverlayFilter(c: Color)
  val fadeLayer = OverlayFilter(halfTransparency) // no need to know the exact value of halfTransparency ColorFilter <: Color

  // how can we create instances of opaque types + how to access their APIs
  // 1- companion objects
  val aName = Name("Alice") // companion object
  // name

  def main(args: Array[String]): Unit = {

  }

}
