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
import scala.language.implicitConversions

object ListZipper {

    case class Zipper[A](focus: A, left: List[A], right: List[A]) {

      def fromZipper: List[A] = left ::: List(focus) ::: right

      /** Will throw NoSuchElementException if try to move beyond start. */

      /** directions are forward and backward across the list. */
      def moveForward: Zipper[A] = Zipper(right.head,left :+ focus,right.tail)
      def moveBackward: Zipper[A] = Zipper(left.last,left.init,focus :: right)

      /** Update the focus element. */
      def update(a: A): Zipper[A] = Zipper(a,left,right)
      def mapFocus(f: A => A): Zipper[A] = Zipper(f(focus),left,right)
    }


    def apply[A](left: List[A], focus: A, right: List[A]): Zipper[A]
      = Zipper(focus,left,right)

    def apply[A](xs: List[A]): Zipper[A]
        = Zipper(xs.head,List.empty[A],xs.tail)


}