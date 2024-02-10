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

  val bob = User("Bob", 43, "bob@rockthejvm.com")
  val bob2HTML = bob.toHTML
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

  // v3: type class
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  // part 2 - type class instances for the supported types
  val userSerializer = new HTMLSerializer[User] {
    override def serialize(value: User): String =
      s"<div>${value.name} (${value.age} yo) <a href=${value.email}/></div>"
  }

  val bob2HTML_V2 = userSerializer.serialize(bob)

  /*
    Benefits:
    - can define serializers for other types OUTSIDE the "library"
    - multiple implementations for a type, pick whichever you want
   */

  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(value: Date): String =
      s"<div>${value.toString}</div>"
  }

  val partialUserSerializer = new HTMLSerializer[User] {
    override def serialize(value: User): String =
      s"<div>${value.name}</div>"
  }

  object SomeOtherSerializerFunctionality { // organize givens properly
    given SomeOtherSerializerFunctionality :HTMLSerializer[User] with {
      override def serialize(value: User): String =
        s"<div>${value.name}</div>"
    }
  }

  // part 3 - using the type class (user-facing API)
  object HTMLSerializer {
    def serialize[T](value: T)(using serializer: HTMLSerializer[T]): String =
    serializer.serialize(value)
  }

  val bob2HTML_V3 = HTMLSerializer.serialize(bob)


  def main(args: Array[String]): Unit = {}

}
