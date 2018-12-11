package com.oneeyedmen.minutest.internal

import com.oneeyedmen.minutest.NodeBuilder

internal data class TestBuilder<F>(val name: String, val f: F.() -> F) : NodeBuilder<F, F> {

    override val properties: MutableMap<Any, Any> = HashMap()

    override fun buildNode(parent: ParentContext<F>) = PreparedRuntimeTest(name, parent, f, properties)
}