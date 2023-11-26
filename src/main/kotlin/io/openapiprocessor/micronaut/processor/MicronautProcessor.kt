/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.processor

import io.openapiprocessor.core.converter.ApiConverter
import io.openapiprocessor.core.converter.ApiOptions
import io.openapiprocessor.core.converter.OptionsConverter
import io.openapiprocessor.core.parser.OpenApiParser
import io.openapiprocessor.core.writer.DefaultWriterFactory
import io.openapiprocessor.core.writer.java.*
import io.openapiprocessor.micronaut.Version
import io.openapiprocessor.micronaut.writer.java.BeanValidations
import io.openapiprocessor.micronaut.writer.java.MappingAnnotationWriter
import io.openapiprocessor.micronaut.writer.java.ParameterAnnotationWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime

/**
 *  Entry point of openapi-processor-micronaut.
 */
class MicronautProcessor {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass.name)
    private var testMode = false

    fun run(processorOptions: MutableMap<String, *>) {
        try {
            val parser = OpenApiParser()
            val openapi = parser.parse(processorOptions)
            if (processorOptions["showWarnings"] != null) {
                openapi.printWarnings()
            }

            val framework = MicronautFramework()
            val annotations = MicronautFrameworkAnnotations()

            val options = convertOptions(processorOptions)
            val cv = ApiConverter(options, framework)
            val api = cv.convert(openapi)

            val writerFactory = DefaultWriterFactory(options)
            val generatedInfo = createGeneratedInfo(options)
            val generatedWriter = GeneratedWriterImpl(generatedInfo, options)
            val validationWriter = ValidationWriter(options)
            val beanValidations = BeanValidations(options)
            val javaDocWriter = JavaDocWriter()

            val writer = ApiWriter(
                options,
                generatedWriter,
                validationWriter,
                InterfaceWriter(
                    options,
                    generatedWriter,
                    MethodWriter(
                        options,
                        MappingAnnotationWriter(),
                        ParameterAnnotationWriter(annotations),
                        beanValidations,
                        javaDocWriter
                    ),
                    annotations,
                    beanValidations,
                    DefaultImportFilter()
                ),
                when (options.modelType) {
                    "record" -> DataTypeWriterRecord(
                        options,
                        generatedWriter,
                        beanValidations,
                        javaDocWriter
                    )
                    else -> DataTypeWriterPojo(
                        options,
                        generatedWriter,
                        beanValidations,
                        javaDocWriter
                    )
                },
                StringEnumWriter (options, generatedWriter),
                InterfaceDataTypeWriter(
                    options,
                    generatedWriter,
                    javaDocWriter
                ),
                listOf(),
                GoogleFormatter(),
                writerFactory
            )

            writer.write (api)
        } catch (ex: Exception) {
            log.error("processing failed!", ex)
            throw ProcessingException(ex)
        }
    }

    private fun createGeneratedInfo(options: ApiOptions): GeneratedInfo {
        var version = Version.version
        var date: String? = OffsetDateTime.now().toString()

        if (!options.generatedDate)
            date = null

        if (testMode) {
            version = "test"
            date = null
        }

        return GeneratedInfo(
            "openapi-processor-micronaut",
            version,
            date
        )
    }

    fun enableTestMode () {
        testMode = true
    }

    @Suppress("UNCHECKED_CAST")
    private fun convertOptions(processorOptions: Map<String, *>): ApiOptions {
        val options = OptionsConverter().convertOptions (processorOptions as Map<String, Any>)
        options.validate ()
        return options
    }
}
