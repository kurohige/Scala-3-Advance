package com.rockthejvm.part5ts

object OpaqueTypes {

  object SocialNetwork {
    // some data structures = "Domain"
    opaque type Name = String

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
    opaque type color = Int // in hex


  }

  def main(args: Array[String]): Unit = {

  }

}
