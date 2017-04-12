package data

import data.Predef.{Boolean, NoSuchElementException, Nothing}

/** Simple BinaryTree to illustrate Zipper. */

object BinaryTree {
  abstract class BinaryTree[+A] {
    def value: A
    def empty: Boolean
    def left: BinaryTree[A]
    def right: BinaryTree[A]
  }

  case class Node[+A](value: A, left: BinaryTree[A], right: BinaryTree[A]) extends BinaryTree[A] {
    override def empty: Boolean = false
  }

  case object Leaf extends BinaryTree[Nothing] {
    override def value: Nothing = throw new NoSuchElementException("Leaf.value")
    override def left: BinaryTree[Nothing] = throw new NoSuchElementException("Leaf.left")
    override def right: BinaryTree[Nothing] = throw new NoSuchElementException("Left.right")
    override def empty: Boolean = true
  }

}