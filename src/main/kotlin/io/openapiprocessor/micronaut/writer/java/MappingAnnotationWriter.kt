/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.writer.java.MappingAnnotationWriter as CoreMappingAnnotationWriter
import io.openapiprocessor.core.model.Endpoint
import io.openapiprocessor.core.model.EndpointResponse
import io.openapiprocessor.core.support.capitalizeFirstChar
import java.io.Writer

/**
 * micronaut mapping annotation writer
 */
class MappingAnnotationWriter: CoreMappingAnnotationWriter {

    override fun write(target: Writer, endpoint: Endpoint, endpointResponse: EndpointResponse) {
        target.write(createAnnotation(endpoint, endpointResponse))
    }

    private fun createAnnotation(endpoint: Endpoint, endpointResponse: EndpointResponse): String {
        var mapping = getMappingAnnotation(endpoint)
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


    private fun getMappingAnnotation(endpoint: Endpoint): String {
        return "@${endpoint.method.method.capitalizeFirstChar()}"
    }

    private fun quote(content: String): String {
        return '"' + content + '"'
    }

}
