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
  given userSerializer: HTMLSerializer[User] with {
    override def serialize(value: User): String =
      s"<div>${value.name} (${value.age} yo) <a href=${value.email}/></div>"
  }

  val bob2HTML_V2 = userSerializer.serialize(bob)

  /*
     Benefits:
     - can define serializers for other types OUTSIDE the "library"
     - multiple serializers for the same type, pick whichever you want
    */
  import java.util.Date
  given dateSerializer: HTMLSerializer[Date] with {
    override def serialize(date: Date) = s"<div>${date.toString()}</div>"
  }

  object SomeOtherSerializerFunctionality { // organize givens properly
    given partialUserSerializer: HTMLSerializer[User] with {
      override def serialize(user: User) = s"<div>${user.name}</div>"
    }
  }

  // part 3 - using the type class (user-facing API)
  object HTMLSerializer {
    def serialize[T](value: T)(using serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](using serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer
  }

  val bob2Html_v3 = HTMLSerializer.serialize(bob)
  val bob2Html_v4 = HTMLSerializer[User].serialize(bob)

  // part 4
  object HTMLSyntax {
    extension [T](value: T)
      def toHTML(using serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  import HTMLSyntax.*

  val bob2Html_v5 = bob.toHTML

  /*
    Cool!
    - extend functionality to new types that we want to support
    - flexibility to add TC instances in a different place than the definition of the TC
    - choose implementations (by importing the right givens)
    - super expressive! (via extension methods)
   */


  def main(args: Array[String]): Unit = {
    println(bob2HTML)
    println(bob2HTML_V2)
    println(bob2Html_v3)
    println(bob2Html_v4)
    println(bob2Html_v5)
  }

}
