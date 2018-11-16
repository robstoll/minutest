package com.oneeyedmen.minutest.examples

import com.oneeyedmen.minutest.TestContext
import com.oneeyedmen.minutest.junit.JupiterTests
import com.oneeyedmen.minutest.junit.context
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.*


// To run the same tests against different implementations, first define a TestContext extension function
// that defines the tests you want run.
private fun TestContext<MutableCollection<String>>.behavesAsMutableCollection(
    collectionName: String
) {

    context("$collectionName behaves as MutableCollection") {

        test("is empty") {
            assertTrue(isEmpty())
        }

        test("can add") {
            add("item")
            assertEquals("item", first())
        }
    }
}

// Now tests can supply the fixture and invoke the function to verify the contract.

class ArrayListTests : JupiterTests {

    override val tests = context<MutableCollection<String>> {
        fixture {
            ArrayList()
        }

        behavesAsMutableCollection("ArrayList")
    }
}

// We can reuse the contract for different collections.
class LinkedListTests : JupiterTests {

    override val tests = context<MutableCollection<String>> {
        fixture {
            LinkedList()
        }

        behavesAsMutableCollection("LinkedList")
    }
}