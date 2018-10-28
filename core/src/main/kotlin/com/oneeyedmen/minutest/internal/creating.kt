package com.oneeyedmen.minutest.internal

import com.oneeyedmen.minutest.TestContext

object RootContext : ParentContext<Unit> {
    override val name: String = ""
    override fun runTest(test: Unit.() -> Unit) = test(Unit)
}

/**
 * Build a test context out of context.
 */
internal fun <F> topContext(
    name: String,
    fixtureFn: (Unit.() -> F)? = null,
    builder: TestContext<Unit, F>.() -> Unit
) = MiContext(name, RootContext, fixtureFn).apply { builder() }

fun <F> top(
    name: String,
    fixtureFn: (Unit.() -> F)? = null,
    builder: TestContext<Unit, F>.() -> Unit
): TestContext<Unit, F> = MiContext(name, RootContext, fixtureFn).apply { builder() }

inline fun <reified F> top(name: String,
    noinline builder: TestContext<Unit, F>.() -> Unit
) = top(name, deduceFixtureFn(), builder = builder)

inline fun <reified F> deduceFixtureFn(): (Unit.() -> F)? =
    if (F::class == Unit::class) {
        { Unit as F }
    } else null