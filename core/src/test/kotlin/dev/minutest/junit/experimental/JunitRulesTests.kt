package dev.minutest.junit.experimental

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterAll
import org.junit.rules.TestWatcher
import org.junit.runner.Description

private val log = mutableListOf<String>()

class JunitRulesTests : JUnit5Minutests {
    class TestRule : TestWatcher() {
        var testDescription: String? = null
        
        override fun succeeded(description: Description) {
            testDescription = description.displayName
        }
    }
    
    class Fixture {
        val rule = TestRule()
    }

    fun tests() = rootContext<Fixture>(name = javaClass.canonicalName) {
        fixture {
            Fixture()
        }

        context("outer") {
            context("apply rule fixture class") {
                applyRule { this.rule }

                test("test 1") {
                    log.add("test 1")
                }
            }

            context("apply rule test class") {
                applyRule(Fixture::rule)

                test("test 2") {
                    log.add("test 2")
                }
            }
        }

        after {
            log.add(rule.testDescription.toString())
        }
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun checkTestIsRun() {
            assertEquals(
                listOf(
                    "test 1",
                    "outer.apply rule fixture class.test 1(dev.minutest.junit.experimental.JunitRulesTests)",
                    "test 2",
                    "outer.apply rule test class.test 2(dev.minutest.junit.experimental.JunitRulesTests)"),
                log)
        }
    }
}


