/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-spring
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.openapiprocessor.core.builder.api.endpoint
import io.openapiprocessor.core.model.datatypes.StringDataType
import io.openapiprocessor.core.openapi.HttpMethod
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations

class MappingAnnotationFactorySpec: StringSpec({
    val factory = MappingAnnotationFactory(MicronautFrameworkAnnotations())

    data class TestHttpMethod(val httpMethod: HttpMethod, val path: String, val expected: String)

    withData(
        nameFn = {"writes http method specific mapping annotation: ${it.httpMethod}"},

        TestHttpMethod(
            HttpMethod.GET,
            "get-it",
            """@Get(uri = "get-it")"""),
        TestHttpMethod(
            HttpMethod.PUT,
            "put-it",
            """@Put(uri = "put-it")"""),
        TestHttpMethod(
            HttpMethod.POST,
            "post-it",
            """@Post(uri = "post-it")"""),
        TestHttpMethod(
            HttpMethod.DELETE,
            "delete-it",
            """@Delete(uri = "delete-it")"""),
        TestHttpMethod(
            HttpMethod.OPTIONS,
            "options-it",
            """@Options(uri = "options-it")"""),
        TestHttpMethod(
            HttpMethod.HEAD,
            "head-it",
            """@Head(uri = "head-it")"""),
        TestHttpMethod(
            HttpMethod.PATCH,
            "patch-it",
            """@Patch(uri = "patch-it")"""),
        TestHttpMethod(
            HttpMethod.TRACE,
            "trace-it",
            """@Trace(uri = "trace-it")""")

    ) { (httpMethod, path, expected) ->
        val endpoint = endpoint(path = path, method = httpMethod) {
            responses {
                status("204") { empty() }
            }
        }

        val codeSnippets = factory.create(endpoint, endpoint.endpointResponses.first())

        codeSnippets.first().text shouldBe expected
    }

    data class TestContentType(val contentType: String, val expected: String)

    withData(
        nameFn = {"writes 'consumes' parameter with body content type ${it.contentType}"},

        TestContentType(
            "plain/text",
            """@Get(uri = "/foo", consumes = {"plain/text"})"""),
        TestContentType(
            "application/json",
            """@Get(uri = "/foo", consumes = {"application/json"})""")

    ) { (contentType, expected) ->
        val endpoint = endpoint(path = "/foo", method = HttpMethod.GET) {
            parameters {
                body("body", contentType, StringDataType())
            }
            responses {
                status("204") { empty() }
            }
        }

        val codeSnippets = factory.create(endpoint, endpoint.endpointResponses.first())

        val mapping = codeSnippets.first()
        mapping.text shouldBe expected
        mapping.imports shouldContainExactly listOf("io.micronaut.http.annotation.Get")
    }

    data class TestProduces(val contentType: String, val expected: String)

    withData(
        nameFn = {"writes 'produces' parameter with response content type ${it.contentType}"},

        TestProduces(
            "plain/text",
            """@Get(uri = "/foo", produces = {"plain/text"})"""),
        TestProduces(
            "application/json",
            """@Get(uri = "/foo", produces = {"application/json"})""")

    ) { (contentType, expected) ->
        val endpoint = endpoint(path = "/foo", method = HttpMethod.GET) {
            responses {
                status("200") {
                    response(contentType, StringDataType())
                }
            }
        }

        val codeSnippets = factory.create(endpoint, endpoint.endpointResponses.first())

        val mapping = codeSnippets.first()
        mapping.text shouldBe expected
        mapping.imports shouldContainExactly listOf("io.micronaut.http.annotation.Get")
    }

    data class TestConsumesProduces(val requestContentType: String, val responseContentType: String, val expected: String)

    withData(
        nameFn = {"writes 'consumes' & 'produces' parameters ${it.requestContentType}/${it.responseContentType}"},

        listOf(
            TestConsumesProduces(
                "foo/in",
                "foo/out",
                """@Get(uri = "/foo", consumes = {"foo/in"}, produces = {"foo/out"})"""
            )
        )
    ) { (requestContentType, responseContentType, expected) ->
        val endpoint = endpoint(path = "/foo", method = HttpMethod.GET) {
            parameters {
                body("body", requestContentType, StringDataType())
            }
            responses {
                status("200") {
                    response(responseContentType, StringDataType())
                }
            }
        }

        val codeSnippets = factory.create(endpoint, endpoint.endpointResponses.first())

        val mapping = codeSnippets.first()
        mapping.text shouldBe expected
        mapping.imports shouldContainExactly listOf("io.micronaut.http.annotation.Get")
    }

    "writes mapping annotation with multiple result content types" {
        val endpoint = endpoint(path = "/foo", method = HttpMethod.GET) {
            responses {
                status("200") {
                    response("application/json", StringDataType())
                }
                status("default") {
                    response("text/plain", StringDataType())
                }
            }
        }

        val codeSnippets = factory.create(endpoint, endpoint.endpointResponses.first())

        val mapping = codeSnippets.first()
        mapping.text shouldBe """@Get(uri = "${endpoint.path}", produces = {"${endpoint.responses["200"]?.first()?.contentType}", "${endpoint.responses["default"]?.first()?.contentType}"})"""
        mapping.imports shouldContainExactly listOf("io.micronaut.http.annotation.Get")
    }

    "writes unique 'consumes' parameter" {
        val endpoint = endpoint(path = "/foo", method = HttpMethod.GET) {
            parameters {
                body("body", "foo/in", StringDataType())
                body("body", "foo/in", StringDataType())
                body("body", "foo/in", StringDataType())
            }
            responses {
                status("204") { empty() }
            }
        }

        val codeSnippets = factory.create(endpoint, endpoint.endpointResponses.first())

        val mapping = codeSnippets.first()
        mapping.text shouldContain """consumes = {"foo/in"}"""
        mapping.imports shouldContainExactly listOf("io.micronaut.http.annotation.Get")
    }

    "writes unique 'produces' parameters" {
        val endpoint = endpoint(path = "/foo", method = HttpMethod.GET) {
            responses {
                status("200") {
                    response("foo/out", StringDataType())
                }
                status("400") {
                    response("foo/out", StringDataType())
                }
                status("401") {
                    response("foo/out", StringDataType())
                }
                status("403") {
                    response("foo/out", StringDataType())
                }
            }
        }

        val codeSnippets = factory.create(endpoint, endpoint.endpointResponses.first())

        val mapping = codeSnippets.first()
        mapping.text shouldContain """produces = {"foo/out"}"""
        mapping.imports shouldContainExactly listOf("io.micronaut.http.annotation.Get")
    }

    "writes 'consumes' of multipart/form-data" {
        val endpoint = endpoint(path = "/foo", method = HttpMethod.GET) {
            parameters {
                multipart("mp1", StringDataType())
                multipart("mp2", StringDataType())
            }
            responses {
                status("204") { empty() }
            }
        }

        val codeSnippets = factory.create(endpoint, endpoint.endpointResponses.first())

        val mapping = codeSnippets.first()
        mapping.text shouldContain """@Get(uri = "${endpoint.path}", consumes = {"multipart/form-data"}"""
        mapping.imports shouldContainExactly listOf("io.micronaut.http.annotation.Get")
    }
})
