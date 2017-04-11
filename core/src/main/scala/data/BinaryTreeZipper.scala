/*
 * Copyright (c) 2017
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package data

import data.BinaryTree.{BinaryTree, Node}
import data.Predef._



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

object BinaryTreeZipper {

  sealed trait Direction
  case object Left extends Direction
  case object Right extends Direction

  case class ParentContext[A](direction: Direction, value: A, tree: BinaryTree[A])

  case class Zipper[A](focus: A, left: BinaryTree[A], right: BinaryTree[A], above: List[ParentContext[A]]) {

    @tailrec
    final def fromZipper: BinaryTree[A] = if (above.isEmpty) Node(focus,left,right) else moveUp.fromZipper

    /** directions are up, left and right. */

    /** wip. */
    def moveUp: Zipper[A] = above match {
      case ParentContext(d,p,s) :: cs if d == Left   => Zipper(p, Node(focus,left,right), s, cs)
      case ParentContext(d,p,s) :: cs if d == Right  => Zipper(p, s, Node(focus,left,right), cs)
      case _ => throw new NoSuchElementException("up. Already at root.")
    }

    def moveLeft: Zipper[A] = left match {
      case Node(n,l,r) => Zipper(n,l,r, ParentContext(Left,focus,right) :: above)
      case _ => throw new NoSuchElementException("left. At leaf node.")
    }

    def moveRight: Zipper[A] = right match {
      case Node(n,l,r) => Zipper(n,l,r, ParentContext(Right,focus,left) :: above)
      case _ => throw new NoSuchElementException("right. At leaf node.")
    }

    /** Update the focus element. */
    def update(a: A): Zipper[A] = Zipper(a,left,right,above)
    def mapFocus(f: A=>A): Zipper[A] = Zipper(f(focus),left,right,above)
  }


  def apply[A](tree: BinaryTree[A]): Zipper[A]
    = Zipper(tree.value,tree.left,tree.right,List.empty)

}
