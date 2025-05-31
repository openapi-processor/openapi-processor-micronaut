/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.processor

import io.openapiprocessor.core.converter.mapping.SimpleParameterValue
import io.openapiprocessor.core.framework.FrameworkAnnotations
import io.openapiprocessor.core.model.Annotation
import io.openapiprocessor.core.model.EndpointResponseStatus
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

    override fun getAnnotation(status: EndpointResponseStatus): Annotation {
        val statusCode = HTTP_STATUS[status.statusCode]
        if (statusCode == null) {
            log.error("unknown http status code: ${status.statusCode}")
            return UNKNOWN_ANNOTATION
        }

        return Annotation(
            getAnnotationName("Status"),
            linkedMapOf(
                "code" to SimpleParameterValue(statusCode, HTTP_STATUS_ENUM)))
    }

    private fun getAnnotation(key: String): Annotation {
        return PARAMETER_ANNOTATIONS.getValue(key)
    }
}

private val REQUEST_MAPPING_ANNOTATION = Annotation(getAnnotationName("Controller"))

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

private val HTTP_STATUS = hashMapOf(
    "100" to getEnum("CONTINUE"),
    "101" to getEnum("SWITCHING_PROTOCOLS"),
    "102" to getEnum("PROCESSING"), // WebDAV

    "200" to getEnum("OK"),
    "201" to getEnum("CREATED"),
    "202" to getEnum("ACCEPTED"),
    "203" to getEnum("NON_AUTHORITATIVE_INFORMATION"),
    "204" to getEnum("NO_CONTENT"),
    "205" to getEnum("RESET_CONTENT"),
    "206" to getEnum("PARTIAL_CONTENT"),
    "207" to getEnum("MULTI_STATUS"), // WebDAV
    "208" to getEnum("ALREADY_REPORTED"), // WebDAV
    "226" to getEnum("IM_USED"),

    "300" to getEnum("MULTIPLE_CHOICES"),
    "301" to getEnum("MOVED_PERMANENTLY"),
    "302" to getEnum("FOUND"), // replaces MOVED_TEMPORARILY
    "303" to getEnum("SEE_OTHER"),
    "304" to getEnum("NOT_MODIFIED"),
    "305" to getEnum("USE_PROXY"),
    "306" to getEnum("SWITCH_PROXY"),
    "307" to getEnum("TEMPORARY_REDIRECT"),
    "308" to getEnum("PERMANENT_REDIRECT"),

    "400" to getEnum("BAD_REQUEST"),
    "401" to getEnum("UNAUTHORIZED"),
    "402" to getEnum("PAYMENT_REQUIRED"),
    "403" to getEnum("FORBIDDEN"),
    "404" to getEnum("NOT_FOUND"),
    "405" to getEnum("METHOD_NOT_ALLOWED"),
    "406" to getEnum("NOT_ACCEPTABLE"),
    "407" to getEnum("PROXY_AUTHENTICATION_REQUIRED"),
    "408" to getEnum("REQUEST_TIMEOUT"),
    "409" to getEnum("CONFLICT"),
    "410" to getEnum("GONE"),
    "411" to getEnum("LENGTH_REQUIRED"),
    "412" to getEnum("PRECONDITION_FAILED"),
    "413" to getEnum("REQUEST_ENTITY_TOO_LARGE"),
    "414" to getEnum("REQUEST_URI_TOO_LONG"),
    "415" to getEnum("UNSUPPORTED_MEDIA_TYPE"),
    "416" to getEnum("REQUESTED_RANGE_NOT_SATISFIABLE"),
    "417" to getEnum("EXPECTATION_FAILED"),
    "418" to getEnum("I_AM_A_TEAPOT"),
    "420" to getEnum("ENHANCE_YOUR_CALM"),
    "422" to getEnum("UNPROCESSABLE_ENTITY"), // WebDAV
    "423" to getEnum("LOCKED"), // WebDAV
    "424" to getEnum("FAILED_DEPENDENCY"), // WebDAV
    "425" to getEnum("UNORDERED_COLLECTION"),
    "426" to getEnum("UPGRADE_REQUIRED"),
    "428" to getEnum("PRECONDITION_REQUIRED"),
    "429" to getEnum("TOO_MANY_REQUESTS"),
    "431" to getEnum("REQUEST_HEADER_FIELDS_TOO_LARGE"),
    "444" to getEnum("NO_RESPONSE"),
    "450" to getEnum("BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS"),
    "451" to getEnum("UNAVAILABLE_FOR_LEGAL_REASONS"),
    "494" to getEnum("REQUEST_HEADER_TOO_LARGE"),

    "500" to getEnum("INTERNAL_SERVER_ERROR"),
    "501" to getEnum("NOT_IMPLEMENTED"),
    "502" to getEnum("BAD_GATEWAY"),
    "503" to getEnum("SERVICE_UNAVAILABLE"),
    "504" to getEnum("GATEWAY_TIMEOUT"),
    "505" to getEnum("HTTP_VERSION_NOT_SUPPORTED"),
    "506" to getEnum("VARIANT_ALSO_NEGOTIATES"),
    "507" to getEnum("INSUFFICIENT_STORAGE"), // WebDAV
    "508" to getEnum("LOOP_DETECTED"), // WebDAV
    "509" to getEnum("BANDWIDTH_LIMIT_EXCEEDED"),
    "510" to getEnum("NOT_EXTENDED"),
    "511" to getEnum("NETWORK_AUTHENTICATION_REQUIRED"),
    "522" to getEnum("CONNECTION_TIMED_OUT")
)

private const val HTTP_STATUS_ENUM = "io.micronaut.http.HttpStatus"

private fun getEnum(name: String): String {
    return "HttpStatus.${name}"
}
