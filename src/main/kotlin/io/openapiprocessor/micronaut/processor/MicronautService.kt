/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */
@file:Suppress("DEPRECATION")

package io.openapiprocessor.micronaut.processor

import io.openapiprocessor.core.writer.DefaultWriterFactory

/**
 *  Entry point of openapi-processor-micronaut loaded via [java.util.ServiceLoader] by the v1 interface
 *  [io.openapiprocessor.api.v1.OpenApiProcessor].
 */
class MicronautService(private val testMode: Boolean = false):
    io.openapiprocessor.api.v1.OpenApiProcessor,
    io.openapiprocessor.api.OpenApiProcessor
{
    override fun getName(): String {
        return "micronaut"
    }

    override fun run(processorOptions: MutableMap<String, *>) {
        try {
            val processor = MicronautProcessor()
            if (testMode) {
                processor.enableTestMode()
            }
            processor.run(processorOptions)

        } catch (ex: Exception) {
            throw ex
        }
    }
}
