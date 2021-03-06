package io.shiftleft.overflowdb.traversal

import io.shiftleft.overflowdb.traversal.help.Doc
import io.shiftleft.overflowdb.{NodeRef, OdbEdge, PropertyKey, PropertyKeyValue}
import org.apache.tinkerpop.gremlin.structure.Direction

class NodeTraversal[A <: NodeRef[_]](val traversal: Traversal[A]) extends AnyVal {

  @Doc("Traverse to node id")
  def id: Traversal[Long] = traversal.map(_.id)

  @Doc("Traverse to node label")
  def label: Traversal[String] = traversal.map(_.label)

  def property[P](name: String): Traversal[P] =
    traversal.map(_.value[P](name))

  def property[P](propertyKey: PropertyKey[P]): Traversal[P] =
    traversal.map(_.value[P](propertyKey.name))

  def hasProperty(name: String): Traversal[A] =
    traversal.filter(_.property(name).isPresent)

  def hasProperty(key: PropertyKey[_]): Traversal[A] = hasProperty(key.name)

  def hasProperty[P](keyValue: PropertyKeyValue[P]): Traversal[A] =
    hasProperty[P](keyValue.key, keyValue.value)

  def hasProperty[P](key: PropertyKey[P], value: P): Traversal[A] =
    traversal.filter { node =>
      val property = node.property[P](key.name)
      property.isPresent && property.value == value
    }

  /** Note: do not use as the first step in a traversal, e.g. `traversalSource.all.id(value)`.
   * Use `traversalSource.withId` instead, it is much faster */
  def id(value: Long): Traversal[A] = traversal.filter(_.id == value)

  /** Note: do not use as the first step in a traversal, e.g. `traversalSource.all.label(value)`.
   * Use `traversalSource.withLabel` instead, it is much faster */
  def label(value: String): Traversal[A] = traversal.filter(_.label == value)

  /** follow outgoing edges to adjacent nodes */
  def out: Traversal[NodeRef[_]] =
    traversal.flatMap(_.vertices(Direction.OUT).toScalaAs)

  /** follow outgoing edges of given label to adjacent nodes */
  def out(label: String): Traversal[NodeRef[_]] =
    traversal.flatMap(_.nodesOut(label).toScalaAs)

  /** follow incoming edges to adjacent nodes */
  def in: Traversal[NodeRef[_]] =
    traversal.flatMap(_.vertices(Direction.IN).toScalaAs)

  /** follow incoming edges of given label to adjacent nodes */
  def in(label: String): Traversal[NodeRef[_]] =
    traversal.flatMap(_.nodesIn(label).toScalaAs)

  /** follow incoming and outgoing edges to adjacent nodes */
  def both: Traversal[NodeRef[_]] =
    traversal.flatMap(_.nodes(Direction.BOTH).toScalaAs)

  /** follow incoming and outgoing edges of given label to adjacent nodes */
  def both(label: String): Traversal[NodeRef[_]] =
    traversal.flatMap(_.nodes(Direction.BOTH, label).toScalaAs)

  /** follow outgoing edges */
  def outE: Traversal[OdbEdge] =
    traversal.flatMap(_.edges(Direction.OUT).toScalaAs)

  /** follow outgoing edges of given label */
  def outE(label: String): Traversal[OdbEdge] =
    traversal.flatMap(_.edgesOut(label).toScalaAs)

  /** follow incoming edges */
  def inE: Traversal[OdbEdge] =
    traversal.flatMap(_.edges(Direction.IN).toScalaAs)

  /** follow incoming edges of given label */
  def inE(label: String): Traversal[OdbEdge] =
    traversal.flatMap(_.edgesIn(label).toScalaAs)

  /** follow incoming and outgoing edges */
  def bothE: Traversal[OdbEdge] =
    traversal.flatMap(_.edges(Direction.BOTH).toScalaAs)

  /** follow incoming and outgoing edges of given label */
  def bothE(label: String): Traversal[OdbEdge] =
    traversal.flatMap(_.edges(Direction.BOTH, label).toScalaAs)
}
