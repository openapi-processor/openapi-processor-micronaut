/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.openapiprocessor.core.model.Endpoint
import io.openapiprocessor.core.model.Response
import io.openapiprocessor.core.model.datatypes.NoneDataType
import io.openapiprocessor.core.parser.HttpMethod
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations
import java.io.StringWriter

class StatusAnnotationWriterSpec: StringSpec({
    val writer = StatusAnnotationWriter(MicronautFrameworkAnnotations())
    val target = StringWriter()

    "write response status annotation" {
        val ep = Endpoint(
            "/foo",
            HttpMethod.GET,
            emptyList(),
            emptyList(),
            mapOf(
                "204" to listOf(Response("?", NoneDataType()))
            )
        )

        writer.write(target, ep, ep.endpointResponses.first())

        target.toString() shouldBeEqual """@Status(HttpStatus.NO_CONTENT)"""
    }
})
