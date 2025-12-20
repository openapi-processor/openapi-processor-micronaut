/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.model.Documentation
import io.openapiprocessor.core.model.EmptyResponse
import io.openapiprocessor.core.model.Endpoint
import io.openapiprocessor.core.parser.HttpMethod
import io.openapiprocessor.core.model.RequestBody
import io.openapiprocessor.core.model.Response
import io.openapiprocessor.core.model.datatypes.StringDataType
import io.openapiprocessor.core.model.parameters.MultipartParameter
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations
import spock.lang.Specification

class MappingAnnotationFactorySpec extends Specification {
    def factory = new MappingAnnotationFactory(new MicronautFrameworkAnnotations())

    void "writes http method specific mapping annotation" () {
        def endpoint = endpoint (path: path, method: httpMethod, responses: [
            '204' : [new EmptyResponse ()]
        ])

        when:
        def annotations = factory.create (endpoint, endpoint.endpointResponses.first ())

        then:
        annotations.first() == expected

        where:
        httpMethod         | path         | expected
        HttpMethod.GET     | "/get-it"     | """@Get(uri = "/get-it")"""
        HttpMethod.PUT     | "/put-it"     | """@Put(uri = "/put-it")"""
        HttpMethod.POST    | "/post-it"    | """@Post(uri = "/post-it")"""
        HttpMethod.DELETE  | "/delete-it"  | """@Delete(uri = "/delete-it")"""
        HttpMethod.OPTIONS | "/options-it" | """@Options(uri = "/options-it")"""
        HttpMethod.HEAD    | "/head-it"    | """@Head(uri = "/head-it")"""
        HttpMethod.PATCH   | "/patch-it"   | """@Patch(uri = "/patch-it")"""
        HttpMethod.TRACE   | "/trace-it"   | """@Trace(uri = "/trace-it")"""
    }

    void "writes 'consumes' parameter with body content type" () {
        def endpoint = endpoint (path: '/foo', method: HttpMethod.GET, responses: [
            '204' : [new EmptyResponse ()]
        ], requestBodies: [
            new RequestBody('body', contentType, new StringDataType (), false, false,
                    null)
        ])

        when:
        def annotations = factory.create (endpoint, endpoint.endpointResponses.first ())

        then:
        annotations.first() == expected

        where:
        contentType         | expected
        'plain/text'        | """@Get(uri = "/foo", consumes = {"plain/text"})"""
        'application/json'  | """@Get(uri = "/foo", consumes = {"application/json"})"""
    }

    void "writes 'produces' parameter with response content type" () {
        def endpoint = endpoint (path: '/foo', method: HttpMethod.GET, responses: [
            '200' : [
                new Response (contentType, new StringDataType (), null)
            ],
        ])

        when:
        def annotations = factory.create (endpoint, endpoint.endpointResponses.first ())

        then:
        annotations.first() == expected

        where:
        contentType         | expected
        'plain/text'        | """@Get(uri = "/foo", produces = {"plain/text"})"""
        'application/json'  | """@Get(uri = "/foo", produces = {"application/json"})"""
    }

    void "writes 'consumes' & 'produces' parameters" () {
        def endpoint = endpoint (path: '/foo', method: HttpMethod.GET, responses: [
            '200' : [
                new Response (responseContentType, new StringDataType (), null)
            ]
        ], requestBodies: [
            new RequestBody('body', requestContentType, new StringDataType (), false, false,
                    null)
        ])

        when:
        def annotations = factory.create (endpoint, endpoint.endpointResponses.first ())

        then:
        annotations.first() == expected

        where:
        requestContentType | responseContentType | expected
        'foo/in'           | 'foo/out'           | """@Get(uri = "/foo", consumes = {"foo/in"}, produces = {"foo/out"})"""
    }

    void "writes mapping annotation with multiple result content types" () {
        def endpoint = endpoint (path: '/foo', method: HttpMethod.GET, responses: [
            '200' : [
                new Response ('application/json', new StringDataType (), null)
            ],
            'default': [
                new Response ('text/plain', new StringDataType (), null)
            ]
        ])

        when:
        def annotations = factory.create (endpoint, endpoint.endpointResponses.first ())

        then:
        annotations.first() == """@Get(uri = "${endpoint.path}", produces = {"${endpoint.responses.'200'.first ().contentType}", "${endpoint.responses.'default'.first ().contentType}"})"""
    }

    void "writes 'consumes' of multipart/form-data" () {
        def endpoint = endpoint (path: '/foo', method: HttpMethod.GET,
            parameters: [
                new MultipartParameter ('mp1', new StringDataType(), false, false, null),
                new MultipartParameter ('mp2', new StringDataType(), false, false, null)
            ],
            responses: [
                '204' : [new EmptyResponse ()]
            ]
        )

        when:
        def annotations = factory.create (endpoint, endpoint.endpointResponses.first ())

        then:
        annotations.first() == """@Get(uri = "${endpoint.path}", consumes = {"multipart/form-data"})"""
    }

    void "writes unique 'consumes' parameter" () {
        def endpoint = endpoint (path: '/foo', method: HttpMethod.GET, responses: [
            '204' : [new EmptyResponse ()]
        ], requestBodies: [
            new RequestBody('body', 'foo/in', new StringDataType (), false, false,
                    null),
            new RequestBody('body', 'foo/in', new StringDataType (), false, false,
                    null),
            new RequestBody('body', 'foo/in', new StringDataType (), false, false,
                    null)
        ])

        when:
        def annotations = factory.create (endpoint, endpoint.endpointResponses.first ())

        then:
        annotations.first().contains ('consumes = {"foo/in"}')
    }

    void "writes unique 'produces' parameters" () {
        def endpoint = endpoint (path: '/foo', method: HttpMethod.GET, responses: [
            '200' : [
                new Response ('foo/out', new StringDataType (), null)
            ],
            '400' : [
                new Response ('foo/out', new StringDataType (), null)
            ],
            '401' : [
                new Response ('foo/out', new StringDataType (), null)
            ],
            '403': [
                new Response ('foo/out', new StringDataType (), null)
            ]
        ])

        when:
        def annotations = factory.create (endpoint, endpoint.endpointResponses.first ())

        then:
        annotations.first().contains ('produces = {"foo/out"}')
    }

    private Endpoint endpoint(Map properties) {
        return new Endpoint(
            properties.path as String ?: '',
            properties.method as HttpMethod ?: HttpMethod.GET,
            properties.parameters ?: [],
            properties.requestBodies ?: [],
            properties.responses ?: [:],
            properties.operationId as String ?: null,
            properties.deprecated as boolean ?: false,
            new Documentation(null, properties.description as String),
        )
    }
}
