// explicit name for generated class so that tests are not coupled to the behaviour of the kotlin compiler
@file:JvmName("ExplicitTypedMinutest")
package samples.minutestRunner.a

import dev.minutest.rootContext
import org.junit.jupiter.api.Assertions
import java.util.*

fun `example typed context`() = rootContext<Stack<String>> {
    fixture { Stack() }
    
    test("a typed fixture test") {
        Assertions.assertTrue(isEmpty())
    }
}
