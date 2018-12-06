package com.oneeyedmen.minutest

import com.oneeyedmen.minutest.experimental.*
import com.oneeyedmen.minutest.junit.junitTests
import org.junit.jupiter.api.DynamicNode
import org.junit.jupiter.api.Test
import java.util.stream.Stream


class SkipAndFocusTests {

    private val log = mutableListOf<String>()
    private val noop: Unit.() -> Unit = {}

    @Test fun noop() {
        val tests = junitTests<Unit>(skipAndFocus.then(loggedTo(log))) {

            test("t1", noop)
            test("t2", noop)
        }
        checkLog(tests,
            "com.oneeyedmen.minutest.SkipAndFocusTests",
            "    t1",
            "    t2"
        )
    }

    @Test fun `skip test`() {
        val tests = junitTests<Unit>(skipAndFocus.then(loggedTo(log))) {
            SKIP - test("t1", noop)
            test("t2", noop)
        }
        checkLog(tests,
            "com.oneeyedmen.minutest.SkipAndFocusTests",
            "    t1 skipped",
            "    t2"
        )
    }

    @Test fun `skip context`() {
        val tests = junitTests<Unit>(skipAndFocus.then(loggedTo(log))) {
            SKIP - context("c1") {
                test("c1/t1", noop)
            }
            test("t2", noop)
        }
        checkLog(tests,
            "com.oneeyedmen.minutest.SkipAndFocusTests",
            "    c1 skipped",
            "    t2"
        )
    }

    @Test fun `focus test skips unfocused`() {
        val tests = junitTests<Unit>(skipAndFocus.then(loggedTo(log))) {
            test("t1", noop)
            FOCUS - test("t2", noop)
        }
        checkLog(tests,
            "com.oneeyedmen.minutest.SkipAndFocusTests",
            "    t1 skipped",
            "    t2"
        )
    }

    @Test fun `focus context skips unfocused`() {
        val tests = junitTests<Unit>(skipAndFocus.then(loggedTo(log))) {
            test("t1", noop)
            FOCUS - context("c1") {
                test("c1/t1", noop)
            }
        }
        checkLog(tests,
            "com.oneeyedmen.minutest.SkipAndFocusTests",
            "    t1 skipped",
            "    c1",
            "        c1/t1"
        )
    }

    @Test fun `focus downtree skips unfocused from root`() {
        val tests = junitTests<Unit>(skipAndFocus.then(loggedTo(log))) {
            test("t1", noop)
            context("c1") {
                FOCUS - test("c1/t1", noop)
            }
        }
        checkLog(tests,
            "com.oneeyedmen.minutest.SkipAndFocusTests",
            "    t1 skipped",
            "    c1",
            "        c1/t1"
        )
    }

    @Test fun `deep thing`() {
        val tests = junitTests<Unit>(skipAndFocus.then(loggedTo(log))) {
            test("t1", noop)
            context("c1") {
                FOCUS - test("c1/t1", noop)
                context("c1/c1") {
                    test("c1/c1/t1", noop)
                }
                FOCUS - context("c1/c2") {
                    test("c1/c2/t1", noop)
                    SKIP - test("c1/c2/t2", noop)
                }
            }
        }
        checkLog(tests,
            "com.oneeyedmen.minutest.SkipAndFocusTests",
            "    t1 skipped",
            "    c1",
            "        c1/t1",
            "        c1/c1 skipped",
            "        c1/c2",
            "            c1/c2/t1",
            "            c1/c2/t2 skipped"
        )
    }

    private fun checkLog(tests: Stream<out DynamicNode>, vararg expected: String) {
        executeTests(tests)
        assertLogged(log.withTabsExpanded(4), *expected)
    }
}
