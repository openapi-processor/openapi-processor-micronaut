/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut

import io.openapiprocessor.core.parser.ParserType
import io.openapiprocessor.micronaut.processor.MicronautProcessor
import io.openapiprocessor.micronaut.processor.MicronautServiceV2
import io.openapiprocessor.test.TestSet


const val API_30 = "openapi30.yaml"
const val API_31 = "openapi31.yaml"


@Suppress("SameParameterValue")
fun testSet(
    name: String,
    parser: ParserType,
    openapi: String = "openapi.yaml",
    inputs: String = "inputs.yaml",
    generated: String = "generated.yaml"): TestSet {

    val processor = MicronautServiceV2(testMode = true)

    val testSet = TestSet()
    testSet.name = name
    testSet.processor = processor
    testSet.parser = parser.name
    testSet.openapi = openapi
    testSet.inputs = inputs
    testSet.outputs = generated
    return testSet
}
