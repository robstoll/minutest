package dev.minutest.experimental

import dev.minutest.Node
import dev.minutest.NodeBuilder
import dev.minutest.NodeTransform
import dev.minutest.TestContextBuilder

/**
 * An experimental feature that allows contexts and tests to be annotated in order to change their execution.
 */
interface TestAnnotation<in F> {
    fun <F2: F> getTransform(): NodeTransform<F2> = NodeTransform { it }
}

/**
 * Used (ironically) to *add* an annotation to a context or test block.
 */
operator fun <F, NodeBuilderT: NodeBuilder<F>> TestAnnotation<F>.minus(nodeBuilder: NodeBuilderT): NodeBuilderT =
    nodeBuilder.also {
        it.annotateWith(this)
    }

/**
 * Groups annotations into a list.
 */
operator fun <F, A: TestAnnotation<F>> A.plus(other: A): List<A> = listOf(this, other)


/**
 * Add a list of annotations to a context or test block.
 */
operator fun <F, NodeBuilderT: NodeBuilder<F>> Iterable<TestAnnotation<F>>.minus(nodeBuilder: NodeBuilderT): NodeBuilderT =
    nodeBuilder.also { it.annotateWith(this) }

/**
 * If you are looking at this you are probably trying to add a mismatched set of [TestAnnotation]s to a test or context
 * block. This definition is provided to try to catch this issue by luring the compiler into failure.
 */
operator fun <F> Iterable<*>.minus(@Suppress("UNUSED_PARAMETER") thing: NodeBuilder<*>): Nothing = error("")

/**
 * Adds an annotation to a context block from the inside.
 */
fun <PF, F> TestContextBuilder<PF, F>.annotateWith(annotation: TestAnnotation<PF>) {
    @Suppress("UNCHECKED_CAST")
    (this as NodeBuilder<PF>).annotateWith(annotation)
}

/**
 * For use by annotation transforms to establish if they should.
 */
fun TestAnnotation<*>.appliesTo(node: Node<*>) = node.annotations.contains(this)

fun <F> NodeBuilder<F>.annotateWith(annotations: Iterable<TestAnnotation<F>>) {
    annotations.forEach {
        this.annotateWith(it)
    }
}