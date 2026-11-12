/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.openapiprocessor.core.model.datatypes.DataTypeConstraints
import io.openapiprocessor.core.model.datatypes.StringDataType
import io.openapiprocessor.core.model.parameters.HeaderParameter
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations

class HeaderParameterAnnotationFactorySpec: StringSpec({
    val factory = ParameterAnnotationFactory(MicronautFrameworkAnnotations())

    "create simple (required) header parameter" {
        val param = HeaderParameter("foo", StringDataType(), true)

        val codeSnippets = factory.create(param)

        val header = codeSnippets.first()
        header.text shouldBe """@Header(value = "foo")"""
        header.imports shouldContainExactly setOf("io.micronaut.http.annotation.Header")
    }

    "create simple (optional) header parameter" {
        val param = HeaderParameter("foo", StringDataType(), false)

        val codeSnippets = factory.create(param)

        val header = codeSnippets.first()
        header.text shouldBe """@Header(value = "foo")"""
        header.imports shouldContainExactly setOf("io.micronaut.http.annotation.Header")
    }

    "create simple (optional with default) header parameter" {
        val param = HeaderParameter(
            "foo",
            StringDataType("string", DataTypeConstraints("bar")),
            false)

        val codeSnippets = factory.create(param)

        val header = codeSnippets.first()
        header.text shouldBe """@Header(value = "foo", defaultValue = "bar")"""
        header.imports shouldContainExactly setOf("io.micronaut.http.annotation.Header")
    }
})
