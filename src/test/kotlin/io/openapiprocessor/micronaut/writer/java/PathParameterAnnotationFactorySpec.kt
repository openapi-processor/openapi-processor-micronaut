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
import io.openapiprocessor.core.model.parameters.PathParameter
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations

class PathParameterAnnotationFactorySpec: StringSpec({
    val factory = ParameterAnnotationFactory(MicronautFrameworkAnnotations())

    /*
        void "write simple (required, no default value) path parameter" () {
        def param = new PathParameter(
            'foo', new StringDataType(), false, false, null)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@PathVariable(value = "foo")'
    }

    void "write simple (optional, with default value) path parameter" () {
        def param = new PathParameter('foo',
            new StringDataType('string', new DataTypeConstraints(defaultValue: 'bar'),
                false, null),
            false, false, null)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@PathVariable(value = "foo", defaultValue = "bar")'
    }
     */
    "creates simple (required) path parameter" {
        val param = PathParameter("foo", StringDataType(), true)

        val codeSnippets = factory.create(param)

        val path = codeSnippets.first()
        path.text shouldBe """@PathVariable(value = "foo")"""
        path.imports shouldContainExactly setOf("io.micronaut.http.annotation.PathVariable")
    }

    "creates simple (optional) path parameter" {
        val param = PathParameter("foo", StringDataType(), false)

        val codeSnippets = factory.create(param)

        val path = codeSnippets.first()
        path.text shouldBe """@PathVariable(value = "foo")"""
        path.imports shouldContainExactly setOf("io.micronaut.http.annotation.PathVariable")
    }

    "creates simple (optional with default) path parameter" {
        val param = PathParameter(
            "foo",
            StringDataType("string", DataTypeConstraints("bar")),
            false)

        val codeSnippets = factory.create(param)

        val path = codeSnippets.first()
        path.text shouldBe """@PathVariable(value = "foo", defaultValue = "bar")"""
        path.imports shouldContainExactly setOf("io.micronaut.http.annotation.PathVariable")
    }
})
