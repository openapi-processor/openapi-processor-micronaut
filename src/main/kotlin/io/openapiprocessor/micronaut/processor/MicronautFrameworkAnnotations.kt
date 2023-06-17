/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.processor

import io.openapiprocessor.core.framework.FrameworkAnnotation
import io.openapiprocessor.core.framework.FrameworkAnnotations
import io.openapiprocessor.core.model.HttpMethod
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

    override fun getAnnotation(httpMethod: HttpMethod): FrameworkAnnotation {
        return MAPPING_ANNOTATIONS.getValue(httpMethod)
    }

    override fun getAnnotation(parameter: Parameter): FrameworkAnnotation {
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

    private fun getAnnotation(key: String): FrameworkAnnotation {
        return PARAMETER_ANNOTATIONS.getValue(key)
    }

}

private const val ANNOTATION_PKG = "io.micronaut.http.annotation"

private val MAPPING_ANNOTATIONS = hashMapOf(
    HttpMethod.DELETE  to FrameworkAnnotation("Delete", ANNOTATION_PKG),
    HttpMethod.GET     to FrameworkAnnotation("Get", ANNOTATION_PKG),
    HttpMethod.HEAD    to FrameworkAnnotation("Head", ANNOTATION_PKG),
    HttpMethod.OPTIONS to FrameworkAnnotation("Options", ANNOTATION_PKG),
    HttpMethod.PATCH   to FrameworkAnnotation("Patch", ANNOTATION_PKG),
    HttpMethod.POST    to FrameworkAnnotation("Post", ANNOTATION_PKG),
    HttpMethod.PUT     to FrameworkAnnotation("Put", ANNOTATION_PKG),
    HttpMethod.TRACE   to FrameworkAnnotation("Trace", ANNOTATION_PKG)
)

private val PARAMETER_ANNOTATIONS = hashMapOf(
    "query"     to FrameworkAnnotation ("QueryValue", ANNOTATION_PKG),
    "header"    to FrameworkAnnotation ("Header", ANNOTATION_PKG),
    "cookie"    to FrameworkAnnotation ("CookieValue", ANNOTATION_PKG),
    "path"      to FrameworkAnnotation ("PathVariable", ANNOTATION_PKG),
    "multipart" to FrameworkAnnotation ("Part", ANNOTATION_PKG),
    "body"      to FrameworkAnnotation ("Body", ANNOTATION_PKG)
)

private val UNKNOWN_ANNOTATION = FrameworkAnnotation("Unknown", "fix.me")
