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
import io.openapiprocessor.core.writer.SourceFormatter
import io.openapiprocessor.core.writer.java.*
import io.openapiprocessor.micronaut.Versions
import io.openapiprocessor.micronaut.writer.java.BeanValidations
import io.openapiprocessor.micronaut.writer.java.MappingAnnotationFactory
import io.openapiprocessor.micronaut.writer.java.ParameterAnnotationWriter
import io.openapiprocessor.micronaut.writer.java.StatusAnnotationWriter
import io.openapiprocessor.test.api.OpenApiProcessorTest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime

/**
 *  Entry point of openapi-processor-micronaut.
 */
class MicronautProcessor : OpenApiProcessorTest {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass.name)
    private var testMode = false

    private var sourceRoot: String? = null
    private var resourceRoot: String? = null

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
            val identifier = JavaIdentifier(IdentifierOptions(
                options.identifierWordBreakFromDigitToLetter,
                options.identifierPrefixInvalidEnumStart))
            val cv = ApiConverter(options, identifier, framework)
            val api = cv.convert(openapi)

            val writerFactory = DefaultWriterFactory(options)
            val generatedInfo = createGeneratedInfo(options)
            val generatedWriter = GeneratedWriterImpl(generatedInfo, options)
            val validationWriter = ValidationWriter(options, generatedWriter)
            val beanValidations = BeanValidations(options)
            val javaDocWriter = JavaDocFactory(identifier)
            val formatter = getFormatter(options)

            val writer = ApiWriter(
                options,
                generatedWriter,
                validationWriter,
                InterfaceWriter(
                    options,
                    generatedWriter,
                    MethodWriter(
                        options,
                        identifier,
                        StatusAnnotationWriter(annotations),
                        MappingAnnotationFactory(annotations),
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
                        identifier,
                        generatedWriter,
                        beanValidations,
                        javaDocWriter
                    )
                    else -> DataTypeWriterPojo(
                        options,
                        identifier,
                        generatedWriter,
                        beanValidations,
                        javaDocWriter
                    )
                },
                StringEnumWriter (options, identifier, generatedWriter),
                InterfaceDataTypeWriter(
                    options,
                    generatedWriter,
                    javaDocWriter
                ),
                listOf(),
                formatter,
                writerFactory
            )

            writer.write (api)
        } catch (ex: Exception) {
            log.error("processing failed!", ex)
            throw ProcessingException(ex)
        }
    }

    private fun createGeneratedInfo(options: ApiOptions): GeneratedInfo {
        var version = Versions.version
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

        if (options.targetDirOptions.standardLayout) {
            sourceRoot =  "java"
            resourceRoot = "resources"
        }

        return options
    }

    override fun getSourceRoot(): String? {
        return sourceRoot
    }

    override fun getResourceRoot(): String? {
        return resourceRoot
    }

    private fun getFormatter(apiOptions: ApiOptions): SourceFormatter {
        return SourceFormatterFactory().getFormatter(apiOptions)
    }
}
