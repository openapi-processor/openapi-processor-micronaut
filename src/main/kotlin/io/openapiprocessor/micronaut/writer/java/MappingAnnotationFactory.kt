/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.model.Endpoint
import io.openapiprocessor.core.model.EndpointResponse
import io.openapiprocessor.core.writer.java.CodeSnippet
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations
import io.openapiprocessor.core.writer.java.MappingAnnotationFactory as CoreFactory

class MappingAnnotationFactory(private val annotations: MicronautFrameworkAnnotations): CoreFactory {

    override fun create(endpoint: Endpoint, endpointResponse: EndpointResponse): List<CodeSnippet> {
        return listOf(createAnnotation(endpoint, endpointResponse))
    }

    private fun createAnnotation(endpoint: Endpoint, endpointResponse: EndpointResponse): CodeSnippet {
        val annotation = annotations.getAnnotation(endpoint.method)

        var snippet = annotation.annotationName
        snippet += "("
        snippet += "uri = " + quote(endpoint.path)

        val consumes = endpoint.getConsumesContentTypes()
        if (consumes.isNotEmpty()) {
            snippet += ", "
            snippet += "consumes = {"
            snippet +=  consumes.map {
                quote(it)
            }.joinToString(", ")
            snippet += '}'
        }

        val contentTypes = endpointResponse.contentTypes
        if (contentTypes.isNotEmpty()) {
            snippet += ", "
            snippet += "produces = {"

            snippet += contentTypes.map {
                quote (it)
            }.joinToString (", ")

            snippet += "}"
        }

        snippet += ")"

        return CodeSnippet(snippet, annotation.imports + annotation.referencedImports)
    }

    private fun quote(content: String): String {
        return '"' + content + '"'
    }
}
