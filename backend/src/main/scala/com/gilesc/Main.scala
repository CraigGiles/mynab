package com.gilesc

object Main extends App with StackModule {
  val (emptyStack, noneValue) = pop(List.empty[Int])
  println(s"Stack Without Values: $emptyStack, $noneValue")

  val (stack01, _) = push(emptyStack, 1)
  val (stack02, _) = push(stack01, 2)
  val (stack, _) = push(stack02, 3)
  println("Stack With Values: " + stack)

  val (poppedStack, value) = pop(stack)
  println(s"Popped $value off of the stack. New stack $poppedStack")
}
