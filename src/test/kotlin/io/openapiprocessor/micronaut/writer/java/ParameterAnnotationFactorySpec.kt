/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-spring
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.openapiprocessor.core.model.RequestBody
import io.openapiprocessor.core.model.datatypes.*
import io.openapiprocessor.core.model.parameters.CookieParameter
import io.openapiprocessor.core.model.parameters.HeaderParameter
import io.openapiprocessor.core.model.parameters.Parameter
import io.openapiprocessor.core.model.parameters.PathParameter
import io.openapiprocessor.micronaut.model.parameters.QueryParameter
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations


class ParameterAnnotationFactoryXSpec: StringSpec({
    val factory = ParameterAnnotationFactory(MicronautFrameworkAnnotations())

    data class ParameterTest(val annotation: String, val create: (dataType: DataType) -> Parameter)

    val parameters = listOf(
        ParameterTest("@QueryValue") { QueryParameter("foo", it) },
        ParameterTest("@PathVariable") { PathParameter("foo", it) },
        ParameterTest("@CookieValue") { CookieParameter("foo", it) },
        ParameterTest("@Header") { HeaderParameter("foo", it) }
    )

    withData(
        nameFn = { "creates simple (optional) query parameter with quoted string default value: ${it.annotation}" },
        parameters
    ) { (annotation, create) ->
        val param = create(
            StringDataType("string", DataTypeConstraints("bar"))
        )

        val codeSnippets = factory.create(param)

        val parameter = codeSnippets.first()
        parameter.text shouldBe """$annotation(value = "foo", defaultValue = "bar")"""
        parameter.imports shouldContainExactly listOf(
            "io.micronaut.http.annotation.${annotation.substring(1)}")
    }

    withData(
        nameFn = { "creates simple (optional) query parameter with quoted number default value: ${it.annotation}" },
        parameters
    ) { (annotation, create) ->
        val param = create(
            LongDataType("integer:int64", DataTypeConstraints(5))
        )

        val codeSnippets = factory.create(param)

        val parameter = codeSnippets.first()
        parameter.text shouldBe """$annotation(value = "foo", defaultValue = "5")"""
        parameter.imports shouldContainExactly listOf(
            "io.micronaut.http.annotation.${annotation.substring(1)}")
    }

    "creates required request body parameter" {
        val body = RequestBody(
            "body",
            "application/json",
            ObjectDataType(
                DataTypeName("FooRequestBody", "FooRequestBody"),
                "",
                linkedMapOf("foo" to propertyDataType(StringDataType()))
            ),
            true
        )

        val codeSnippets = factory.create(body)

        val parameter = codeSnippets.first()
        parameter.text shouldBe "@Body"
        parameter.imports shouldContainExactly listOf("io.micronaut.http.annotation.Body")
    }

    "creates optional request body parameter" {
        val body = RequestBody(
            "body",
            "application/json",
            ObjectDataType(
                DataTypeName("FooRequestBody", "FooRequestBody"),
                "",
                linkedMapOf("foo" to propertyDataType(StringDataType()))
            )
        )

        val codeSnippets = factory.create(body)

        val parameter = codeSnippets.first()
        parameter.text shouldBe "@Body"
        parameter.imports shouldContainExactly listOf("io.micronaut.http.annotation.Body")
    }
})

private fun propertyDataType(dataType: DataType) = PropertyDataType(
    readOnly = false,
    writeOnly = false,
    dataType = dataType,
    documentation = null
)

