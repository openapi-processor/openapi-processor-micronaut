/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut

import io.kotest.core.spec.style.StringSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiprocessor.core.parser.ParserType.INTERNAL
import io.openapiprocessor.core.parser.ParserType.SWAGGER
import io.openapiprocessor.test.*

/**
 * helper to run selected integration tests.
 */
class ProcessorPendingSpec: StringSpec({

    for (testSet in sources()) {
        "native - $testSet".config(enabled = true) {
            val folder = tempdir()
            val reader = ResourceReader(ProcessorPendingSpec::class.java)

            val testFiles = TestFilesNative(folder, reader)
            val test = Test(testSet, testFiles)

            TestSetRunner(test, testSet)
                .runOnNativeFileSystem()
                .shouldBeTrue()
        }
    }
})

private fun sources(): Collection<TestSet> {
    return listOf(
        testSet("params-path-simple-data-types", SWAGGER, API_30, model = "default", outputs = "outputs.yaml", expected = "outputs"),
//        testSet("bean-validation-introspected", INTERNAL, API_30, model = "default", outputs = "outputs.yaml", expected = "outputs"),
//        testSet("bean-validation-introspected", INTERNAL, API_31, model = "default", outputs = "outputs.yaml", expected = "outputs")
    )
}
