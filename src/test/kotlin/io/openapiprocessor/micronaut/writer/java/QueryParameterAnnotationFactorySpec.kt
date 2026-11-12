/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-spring
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.openapiprocessor.core.model.datatypes.*
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations
import io.openapiprocessor.micronaut.model.parameters.QueryParameter

class QueryParameterAnnotationFactorySpec: StringSpec({
    val factory = ParameterAnnotationFactory(MicronautFrameworkAnnotations())

    "write simple (required) query parameter" {
        val param = QueryParameter("foo", StringDataType(), true)

        val codeSnippets = factory.create(param)

        val annotation = codeSnippets.first()
        annotation.text shouldBe """@QueryValue(value = "foo")"""
        annotation.imports shouldContainExactly setOf("io.micronaut.http.annotation.QueryValue")
    }

    "write simple (optional) query parameter" {
        val param = QueryParameter("foo", StringDataType(), false)

        val codeSnippets = factory.create(param)

        val annotation = codeSnippets.first()
        annotation.text shouldBe """@QueryValue(value = "foo")"""
        annotation.imports shouldContainExactly setOf("io.micronaut.http.annotation.QueryValue")
    }

    "write simple (optional with default) query parameter" {
        val param = QueryParameter(
            "foo",
            StringDataType("string", DataTypeConstraints("bar")),
            false)

        val codeSnippets = factory.create(param)

        val annotation = codeSnippets.first()
        annotation.text shouldBe """@QueryValue(value = "foo", defaultValue = "bar")"""
        annotation.imports shouldContainExactly setOf("io.micronaut.http.annotation.QueryValue")
    }

    "writes object query parameter without annotation" {
        val param = QueryParameter(
            "foo",
            ObjectDataType(
                DataTypeName("Foo"),
                "",
                linkedMapOf(
                    "foo1" to propertyDataType(StringDataType()),
                    "foo2" to propertyDataType(StringDataType())
                )
            )
        )

        val codeSnippets = factory.create(param)

        codeSnippets.shouldBeEmpty()
    }
})

private fun propertyDataType(dataType: DataType) = PropertyDataType(
    readOnly = false,
    writeOnly = false,
    dataType = dataType,
    documentation = null
)
