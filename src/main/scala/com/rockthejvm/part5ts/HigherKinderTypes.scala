package com.rockthejvm.part5ts

object HigherKinderTypes {

  // are generic types where the type parameters are themselves generic
  class HigherKindedType[F[_]] // F is a type constructor

  val hkExample = new HigherKindedType[List]

  def main(args: Array[String]): Unit = {}

}
