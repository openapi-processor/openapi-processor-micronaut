/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package com.github.hauner.openapi.micronaut.writer.java

import io.openapiprocessor.core.model.EmptyResponse
import io.openapiprocessor.core.model.Endpoint
import io.openapiprocessor.core.model.HttpMethod
import io.openapiprocessor.core.model.RequestBody
import io.openapiprocessor.core.model.Response
import io.openapiprocessor.core.model.datatypes.NoneDataType
import io.openapiprocessor.core.model.datatypes.StringDataType
import io.openapiprocessor.core.model.parameters.MultipartParameter
import io.openapiprocessor.micronaut.writer.java.MappingAnnotationWriter
import spock.lang.Ignore
import spock.lang.Specification

class MappingAnnotationWriterSpec extends Specification {
    def writer = new MappingAnnotationWriter()
    def target = new StringWriter()

    void "writes http method specific mapping annotation" () {
        def endpoint = createEndpoint (path: path, method: httpMethod, responses: [
            '204' : [new EmptyResponse ()]
        ])

        when:
        writer.write (target, endpoint, endpoint.endpointResponses.first ())

        then:
        target.toString () == expected

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
        def endpoint = createEndpoint (path: '/foo', method: HttpMethod.GET, responses: [
            '204' : [new EmptyResponse ()]
        ], requestBodies: [
            new RequestBody('body', contentType, new StringDataType (), false, false)
        ])

        when:
        writer.write (target, endpoint, endpoint.endpointResponses.first ())

        then:
        target.toString () == expected

        where:
        contentType         | expected
        'plain/text'        | """@Get(uri = "/foo", consumes = {"plain/text"})"""
        'application/json'  | """@Get(uri = "/foo", consumes = {"application/json"})"""
    }

    void "writes 'produces' parameter with response content type" () {
        def endpoint = createEndpoint (path: '/foo', method: HttpMethod.GET, responses: [
            '200' : [
                new Response (contentType, new StringDataType ())
            ],
        ])

        when:
        writer.write (target, endpoint, endpoint.endpointResponses.first ())

        then:
        target.toString () == expected

        where:
        contentType         | expected
        'plain/text'        | """@Get(uri = "/foo", produces = {"plain/text"})"""
        'application/json'  | """@Get(uri = "/foo", produces = {"application/json"})"""
    }

    void "writes 'consumes' & 'produces' parameters" () {
        def endpoint = createEndpoint (path: '/foo', method: HttpMethod.GET, responses: [
            '200' : [
                new Response (responseContentType, new StringDataType ())
            ]
        ], requestBodies: [
            new RequestBody('body', requestContentType, new StringDataType (), false, false)
        ])

        when:
        writer.write (target, endpoint, endpoint.endpointResponses.first ())

        then:
        target.toString () == expected

        where:
        requestContentType | responseContentType | expected
        'foo/in'           | 'foo/out'           | """@Get(uri = "/foo", consumes = {"foo/in"}, produces = {"foo/out"})"""
    }

    void "writes mapping annotation with multiple result content types" () {
        def endpoint = createEndpoint (path: '/foo', method: HttpMethod.GET, responses: [
            '200' : [
                new Response ('application/json', new StringDataType ())
            ],
            'default': [
                new Response ('text/plain', new StringDataType ())
            ]
        ])

        when:
        writer.write (target, endpoint, endpoint.endpointResponses.first ())

        then:
        target.toString () == """@Get(uri = "${endpoint.path}", produces = {"${endpoint.responses.'200'.first ().contentType}", "${endpoint.responses.'default'.first ().contentType}"})"""
    }

    @Ignore // todo
    void "writes 'consumes' of multipart/form-data" () {
        def endpoint = createEndpoint (path: '/foo', method: HttpMethod.GET,
            parameters: [
                new MultipartParameter ('mp1', new StringDataType(), false, false),
                new MultipartParameter ('mp2', new StringDataType(), false, false)
            ],
            responses: [
                '204' : [new EmptyResponse ()]
            ]
        )

        when:
        writer.write (target, endpoint, endpoint.endpointResponses.first ())

        then:
        target.toString () == """@Get(uri = "${endpoint.path}", consumes = {"multipart/form-data"})"""
    }

    @Deprecated
    private Endpoint createEndpoint (Map properties) {
        def ep = new Endpoint(
            properties.path as String ?: '',
            properties.method as HttpMethod ?: HttpMethod.GET,
            properties.operationId as String ?: null,
            properties.deprecated as boolean ?: false
        )
        ep.parameters = properties.parameters ?: []
        ep.responses = properties.responses ?: [:]
        ep.requestBodies = properties.requestBodies ?: []
        ep.initEndpointResponses ()
    }

}
