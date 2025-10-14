/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut

import io.kotest.core.spec.style.StringSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.booleans.shouldBeTrue
import io.openapiprocessor.core.parser.ParserType
import io.openapiprocessor.test.*
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.plus
import kotlin.jvm.java


class CompileExpectedSpec: StringSpec({

    for (testSet in sources()) {
        "compile - $testSet".config(enabled = true) {
            val folder = tempdir()
            val reader = ResourceReader(CompileExpectedSpec::class.java)

            val testFiles = TestFilesNative(folder, reader)

            TestSetCompiler(testSet, testFiles)
                .run(
                    setOf(
                        "src/testInt/resources/compile/Generated.java"
                    )
                )
                .shouldBeTrue()
        }
    }
})

private fun sources(): Collection<TestSet> {
    return buildTestSets()
}
