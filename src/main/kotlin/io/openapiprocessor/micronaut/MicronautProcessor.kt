/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut

import io.openapiprocessor.api.OpenApiProcessor
import io.openapiprocessor.core.converter.ApiConverter
import io.openapiprocessor.core.converter.ApiOptions
import io.openapiprocessor.core.converter.OptionsConverter
import io.openapiprocessor.core.parser.Parser
import io.openapiprocessor.core.writer.java.*
import io.openapiprocessor.micronaut.writer.java.BeanValidations
import io.openapiprocessor.micronaut.writer.java.MappingAnnotationWriter
import io.openapiprocessor.micronaut.writer.java.ParameterAnnotationWriter
import io.openapiprocessor.spring.Version
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime

/**
 *  Entry point of openapi-processor-micronaut.
 */
class MicronautProcessor: OpenApiProcessor, io.openapiprocessor.api.v1.OpenApiProcessor {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass.name)
    private var testMode = false

    override fun getName(): String {
        return "micronaut"
    }

    override fun run(processorOptions: MutableMap<String, *>) {
        try {
            val parser = Parser()
            val openapi = parser.parse(processorOptions)
            if (processorOptions["showWarnings"] != null) {
                openapi.printWarnings()
            }

            val framework = MicronautFramework()
            val annotations = MicronautFrameworkAnnotations()

            val options = convertOptions(processorOptions)
            val cv = ApiConverter(options, framework)
            val api = cv.convert(openapi)

            val generatedInfo = createGeneratedInfo(options)
            val generatedWriter = GeneratedWriterImpl(generatedInfo, options)
            val beanValidations = BeanValidationFactory(getValidationFormat(options))
            val javaDocWriter = JavaDocWriter()

            val writer = ApiWriter(
                options,
                generatedWriter,
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
                DataTypeWriterPojo(
                    options,
                    generatedWriter,
                    beanValidations),
                StringEnumWriter (generatedWriter),
                InterfaceDataTypeWriter(
                    options,
                    generatedWriter,
                    javaDocWriter
                )
            )

            writer.write (api)
        } catch (e: Exception) {
            log.error("processing failed!", e)
            throw e
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

    private fun convertOptions(processorOptions: Map<String, *>): ApiOptions {
        val options = OptionsConverter().convertOptions (processorOptions as Map<String, Any>)
        options.validate ()
        return options
    }

    private fun getValidationFormat(options: ApiOptions): BeanValidationFormat {
        val format = options.beanValidationFormat
        return if (format != null)
            BeanValidationFormat.valueOf(format.uppercase())
        else
            BeanValidationFormat.JAVAX
    }
}
