/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.processor

import io.openapiprocessor.core.framework.AnnotationType
import io.openapiprocessor.core.framework.FrameworkAnnotations
import io.openapiprocessor.core.model.Annotation
import io.openapiprocessor.core.parser.HttpMethod
import io.openapiprocessor.core.model.RequestBody
import io.openapiprocessor.core.model.parameters.*
import io.openapiprocessor.micronaut.model.parameters.QueryParameter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * provides Micronaut annotation details.
 */
class MicronautFrameworkAnnotations: FrameworkAnnotations {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun getAnnotation(httpMethod: HttpMethod): Annotation {
        return MAPPING_ANNOTATIONS.getValue(httpMethod)
    }

    override fun getAnnotation(parameter: Parameter): Annotation {
        return when(parameter) {
            is RequestBody -> getAnnotation("body")
            is PathParameter -> getAnnotation("path")
            is QueryParameter -> getAnnotation("query")
            is HeaderParameter -> getAnnotation("header")
            is CookieParameter -> getAnnotation("cookie")
            is MultipartParameter -> getAnnotation("multipart")
            else -> {
                log.error("unknown parameter type: ${parameter::class.java.name}")
                UNKNOWN_ANNOTATION
            }
        }
    }

    override fun getAnnotation(type: AnnotationType): Annotation {
        return when (type) {
            AnnotationType.INTERFACE_PATH_PREFIX -> REQUEST_MAPPING_ANNOTATION
            else -> throw NotImplementedError()
        }
    }

    private fun getAnnotation(key: String): Annotation {
        return PARAMETER_ANNOTATIONS.getValue(key)
    }
}

private val REQUEST_MAPPING_ANNOTATION = Annotation(getAnnotationName("UriMapping"))

private val MAPPING_ANNOTATIONS = hashMapOf(
    HttpMethod.DELETE  to Annotation(getAnnotationName("Delete")),
    HttpMethod.GET     to Annotation(getAnnotationName("Get")),
    HttpMethod.HEAD    to Annotation(getAnnotationName("Head")),
    HttpMethod.OPTIONS to Annotation(getAnnotationName("Options")),
    HttpMethod.PATCH   to Annotation(getAnnotationName("Patch")),
    HttpMethod.POST    to Annotation(getAnnotationName("Post")),
    HttpMethod.PUT     to Annotation(getAnnotationName("Put")),
    HttpMethod.TRACE   to Annotation(getAnnotationName("Trace"))
)

private val PARAMETER_ANNOTATIONS = hashMapOf(
    "query"     to Annotation (getAnnotationName("QueryValue")),
    "header"    to Annotation (getAnnotationName("Header")),
    "cookie"    to Annotation (getAnnotationName("CookieValue")),
    "path"      to Annotation (getAnnotationName("PathVariable")),
    "multipart" to Annotation (getAnnotationName("Part")),
    "body"      to Annotation (getAnnotationName("Body"))
)

private val UNKNOWN_ANNOTATION = Annotation("Unknown")

private const val ANNOTATION_PKG = "io.micronaut.http.annotation"

private fun getAnnotationName(name: String): String {
    return "${ANNOTATION_PKG}.${name}"
}
