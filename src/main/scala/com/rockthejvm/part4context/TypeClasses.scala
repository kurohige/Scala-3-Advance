package com.rockthejvm.part4context

object TypeClasses {

  /*
    Small Library to serialize some data to standard formats (HTML)
   */

  // v1 : the OO way
  trait HTMLWritable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHTML: String = s"<div>$name ($age yo) <a href=$email/></div>"
  }

  val bob2HTML = User("Bob", 43, "bob@rockthejvm.com").toHTML
  // same for other data structures that we want to serialize

  /*
    Drawbacks:
    - only available for the types WE write
    - can only provide ONE implementation
   */

  // v2: pattern matching
  object HTMLSerializerPM {
    def serializeToHTML(value: Any): String = value match {
      case User(name, age, email) =>
        s"<div>$name ($age yo) <a href=$email/></div>"
      case _ => throw new RuntimeException("Value not serializable to HTML")
    }
  }

  /*
    Drawbacks:
    - lost type safety
    - need to modify the code every time we add a new type
    - still only one implementation
   */



  def main(args: Array[String]): Unit = {}

}
