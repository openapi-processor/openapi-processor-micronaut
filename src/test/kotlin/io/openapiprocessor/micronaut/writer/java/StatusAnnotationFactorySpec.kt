/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.openapiprocessor.core.model.Endpoint
import io.openapiprocessor.core.model.Response
import io.openapiprocessor.core.model.datatypes.NoneDataType
import io.openapiprocessor.core.openapi.HttpMethod
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations

class StatusAnnotationFactorySpec: StringSpec({
    val factory = StatusAnnotationFactory(MicronautFrameworkAnnotations())

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

        val codeSnippets = factory.create(ep, ep.firstSuccessResponse)
        val responseStatus = codeSnippets.first()

        responseStatus.text shouldBeEqual """@Status(HttpStatus.NO_CONTENT)"""
        responseStatus.imports shouldContainExactly setOf(
            "io.micronaut.http.annotation.Status",
            "io.micronaut.http.HttpStatus")
    }
})
