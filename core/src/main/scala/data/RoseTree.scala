package data

import data.Predef.{Boolean, List, NoSuchElementException, Nothing}


/** Simple RoseTree, no ordering requirement on children etc. */
object RoseTree {

  type Forest[A] = List[RoseTree[A]]

  abstract class RoseTree[A] {
    def rootLabel: A
    def empty: Boolean
    def subForest: Forest[A]
  }

  case class Node[A](rootLabel: A, subForest: Forest[A]) extends RoseTree[A] {
    override def empty: Boolean = false
  }

  case object Leaf extends RoseTree[Nothing] {
    override def rootLabel: Nothing = throw new NoSuchElementException("Leaf rootLabel is Nothing")
    override def subForest: Forest[Nothing] = throw new NoSuchElementException("Leaf subForest is Nothing.")
    override def empty: Boolean = true
  }

}
