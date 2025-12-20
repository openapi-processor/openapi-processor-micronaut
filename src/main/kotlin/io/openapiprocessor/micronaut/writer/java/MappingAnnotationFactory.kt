/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.model.Endpoint
import io.openapiprocessor.core.model.EndpointResponse
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations
import io.openapiprocessor.core.writer.java.MappingAnnotationFactory as CoreMappingAnnotationFactory

/**
 * micronaut mapping annotation factory
 */
class MappingAnnotationFactory(private val annotations: MicronautFrameworkAnnotations): CoreMappingAnnotationFactory {

    override fun create(endpoint: Endpoint, endpointResponse: EndpointResponse): List<String> {
        return listOf(createAnnotation(endpoint, endpointResponse))
    }

    private fun createAnnotation(endpoint: Endpoint, endpointResponse: EndpointResponse): String {
        val annotation = annotations.getAnnotation(endpoint.method)

        var mapping = annotation.annotationName
        mapping += "("
        mapping += "uri = " + quote(endpoint.path)

        val consumes = endpoint.getConsumesContentTypes()
        if (consumes.isNotEmpty()) {
            mapping += ", "
            mapping += "consumes = {"
            mapping +=  consumes.map {
                quote(it)
            }.joinToString(", ")
            mapping += '}'
        }

        val contentTypes = endpointResponse.contentTypes
        if (contentTypes.isNotEmpty()) {
            mapping += ", "
            mapping += "produces = {"

            mapping += contentTypes.map {
                quote (it)
            }.joinToString (", ")

            mapping += "}"
        }

        mapping += ")"
        return mapping
    }

    private fun quote(content: String): String {
        return '"' + content + '"'
    }
}
