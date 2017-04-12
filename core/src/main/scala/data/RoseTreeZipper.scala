package data

import data.Predef._
import data.RoseTree.{Forest, Node, RoseTree}


object RoseTreeZipper {

  type Parent[A] = (Forest[A],A,Forest[A])

  /**
    * See: https://hackage.haskell.org/package/rosezipper-0.2/src/Data/Tree/Zipper.hs
    *      By Krasimir Angelov & Iavor S. Diatchki 2008.
    */
  case class Zipper[A](focus: RoseTree[A], before: Forest[A], after: Forest[A], parents: List[Parent[A]]) {

    def fromZipper: RoseTree[A] = toTree
    def moveLeft: Option[Zipper[A]] = prevTree
    def moveRight: Option[Zipper[A]] = nextTree

    /** -- Moving around --------------------------------------------------------------- */
    private[this] def forest(before: Forest[A], n: RoseTree[A], after: Forest[A]): Forest[A]
      = before.foldLeft(n :: after)((y,tree) => tree :: y )

    /* -- | The parent of the given location. */
    def parent: Option[Zipper[A]] =  parents match {
      case (ls,a,rs) :: ps => Some(Zipper(Node(a,forest(before,focus,after)),ls,rs,ps))
      case _ => None
    }

    /* -- | The top-most parent of the given location. */
    @tailrec
    def root: Zipper[A] = parent match {
      case Some(p) => p.root
      case None => this
    }

    /* -- | The tree before this location, if any. */
    def prevTree: Option[Zipper[A]] = before match {
      case t :: ts => Some(Zipper(t,ts, focus :: after,parents))
      case _ => None
    }

    /* -- | The tree after this location, if any. */
    def nextTree: Option[Zipper[A]] = after match {
      case t :: ts => Some(Zipper(t, focus :: before, ts, parents))
      case _ => None
    }

    /* parents after moving down. */
    private[this] def down: List[Parent[A]] = (before,focus.rootLabel,after) :: parents

    /* -- | The first child of the given location. */
    def firstChild: Option[Zipper[A]] = focus.subForest match {
      case t :: ts => Some(Zipper(t,List.empty,ts,down))
      case _ => None
    }

    /* -- | The last child of the given location. */
    def lastChild: Option[Zipper[A]] = focus.subForest match {
      case t :: ts => Some(Zipper(t,ts,List.empty,down))
      case _ => None
    }

    /** -- Conversions ----------------------------------------------------------------- */
    def toTree: RoseTree[A] = root.focus

    /** -- The current tree ------------------------------------------------------------ */

    /* --| Change the current tree. */
    def setTree(a: RoseTree[A]): Zipper[A] = Zipper(a,before,after,parents)

    /* --| Modify the current tree. */
    def modifyTree(f: RoseTree[A] => RoseTree[A]): Zipper[A] = setTree(f(focus))

    /* --| Change the label at the current node. */
    def setLabel(a: A): Zipper[A] = modifyTree(t => Node(a,t.subForest))
  }



}
