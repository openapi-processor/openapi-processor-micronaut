/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.framework.FrameworkAnnotations
import io.openapiprocessor.core.model.RequestBody
import io.openapiprocessor.core.model.parameters.Parameter
import io.openapiprocessor.core.writer.java.CodeSnippet
import io.openapiprocessor.core.writer.java.ParameterAnnotationFactory as CoreFactory

class ParameterAnnotationFactory(private val annotations: FrameworkAnnotations): CoreFactory {

    override fun create(parameter: Parameter): List<CodeSnippet> {
        val codeSnippet = if (parameter is RequestBody) {
            createRequestBodyCodeSnippet(parameter)
        } else {
            createParameterCodeSnippet(parameter)
        }

        return listOfNotNull(codeSnippet)
    }

    private fun createRequestBodyCodeSnippet(parameter: Parameter): CodeSnippet? {
        if (! parameter.withAnnotation) {
            return null
        }

        val annotation = annotations.getAnnotation (parameter)
        val snippet = annotation.annotationName

        return CodeSnippet(snippet, annotation.imports)
    }

    private fun createParameterCodeSnippet(parameter: Parameter): CodeSnippet? {
        if (! parameter.withAnnotation) {
            return null
        }

        val annotation = annotations.getAnnotation (parameter)

        var snippet = annotation.annotationName
        if (parameter.withParameters) {
            snippet += "("
            snippet += "value = " + quote (parameter.name)

            if (hasDefault (parameter)) {
                snippet += ", "
                snippet += "defaultValue = ${getDefault(parameter)}"
            }

            snippet += ")"
        }

        return CodeSnippet(snippet, annotation.imports)
    }

    private fun hasDefault(parameter: Parameter): Boolean {
        return parameter.constraints.hasDefault()
    }

    private fun getDefault(parameter: Parameter): String {
        return quote(parameter.constraints.default.toString())
    }

    private fun quote(content: String): String {
        return '"' + content + '"'
    }
}
