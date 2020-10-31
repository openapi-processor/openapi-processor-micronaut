/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.framework.FrameworkAnnotations
import io.openapiprocessor.core.model.RequestBody
import io.openapiprocessor.core.model.parameters.Parameter
import io.openapiprocessor.core.writer.java.ParameterAnnotationWriter as CoreParameterAnnotationWriter
import java.io.Writer

/**
 * micronaut parameter annotation writer
 */
class ParameterAnnotationWriter(
    private val annotations: FrameworkAnnotations

): CoreParameterAnnotationWriter {

    override fun write(target: Writer, parameter: Parameter) {
//        if (parameter.deprecated) {
//            target.write ("@Deprecated ")
//        }

        if (parameter is RequestBody) {
            target.write(createAnnotation(parameter))
        } else {
            target.write(createAnnotation(parameter))
        }
    }

    private fun createAnnotation(requestBody: RequestBody): String {
        return getAnnotationName(requestBody)
    }

    private fun createAnnotation(parameter: Parameter): String {
        if (! parameter.withAnnotation) {
            return ""
        }

        var annotation = getAnnotationName (parameter)

        if (! parameter.withParameters) {
            return annotation
        }

        annotation += "("
        annotation += "value = " + quote (parameter.name)

        if (hasDefault (parameter)) {
            annotation += ", "
            annotation += "defaultValue = ${getDefault(parameter)}"
        }

        annotation += ")"
        return annotation
    }

    private fun getAnnotationName(parameter: Parameter): String {
        return annotations.getAnnotation (parameter).annotationName
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
