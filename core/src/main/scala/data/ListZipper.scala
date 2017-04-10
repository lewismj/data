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

import data.Predef._


object ListZipper {
  /**
    * Context around the list, elements to the left of the element in focus,
    * and elements to the right of the element in focus.
    *
    * @param left   elements to the left of the element in focus.
    * @param right  elements to the right of the element in focus.
    * @tparam A the element type.
    */
    case class Context[A](left: List[A], right: List[A])

    case class Zipper[A](focus: A, context: Context[A]) {
      def left: List[A]   = context.left
      def right: List[A]  = context.right

      def toList: List[A] = (left :+ focus) ::: right

      /** Will throw NoSuchElementException if try to move beyond start. */

      def forward: Zipper[A] = Zipper(right.head,Context(left :+ focus,right.tail))
      def backward: Zipper[A] = Zipper(left.last,Context(left.init, focus :: right))
    }


    def apply[A](left: List[A], focus: A, right: List[A]): Zipper[A]
      = Zipper(focus,Context(left,right))

    def apply[A](xs: List[A]): Zipper[A]
        = Zipper(xs.head,Context(List.empty[A],xs.tail))

}