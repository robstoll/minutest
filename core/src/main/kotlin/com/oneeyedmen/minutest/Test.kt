package com.oneeyedmen.minutest

/**
 * A test that can be invoked on a fixture.
 */
typealias Test<F> = (F, TestDescriptor) -> F

typealias TestTransform<F> = (Test<F>) -> Test<F>
