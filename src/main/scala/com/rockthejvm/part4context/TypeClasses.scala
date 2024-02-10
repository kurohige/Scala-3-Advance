package com.rockthejvm.part4context

object TypeClasses {

  /*
    Small Library to serialize some data to standard formats (HTML)
   */

  // v1 : the OO way
  trait HTMLWritable {
    def toHTML: String
  }

  def main(args: Array[String]): Unit = {}

}
