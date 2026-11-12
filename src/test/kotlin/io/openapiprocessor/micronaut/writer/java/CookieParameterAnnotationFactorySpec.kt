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
import io.openapiprocessor.core.model.parameters.CookieParameter
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations


class CookieParameterAnnotationFactorySpec: StringSpec({
    val factory = ParameterAnnotationFactory(MicronautFrameworkAnnotations())

    "create simple (required) cookie parameter" {
        val param = CookieParameter("foo", StringDataType(), true)

        val codeSnippets = factory.create(param)

        val cookie = codeSnippets.first()
        cookie.text shouldBe """@CookieValue(value = "foo")"""
        cookie.imports shouldContainExactly setOf("io.micronaut.http.annotation.CookieValue")
    }

    "create simple (optional) cookie parameter" {
        val param = CookieParameter("foo", StringDataType(), false)

        val codeSnippets = factory.create(param)

        val cookie = codeSnippets.first()
        cookie.text shouldBe """@CookieValue(value = "foo")"""
        cookie.imports shouldContainExactly setOf("io.micronaut.http.annotation.CookieValue")
    }

    "create simple (optional with default) cookie parameter" {
        val param = CookieParameter(
            "foo",
            StringDataType("string", DataTypeConstraints("bar")),
            false)

        val codeSnippets = factory.create(param)

        val cookie = codeSnippets.first()
        cookie.text shouldBe """@CookieValue(value = "foo", defaultValue = "bar")"""
        cookie.imports shouldContainExactly setOf("io.micronaut.http.annotation.CookieValue")
    }
})
