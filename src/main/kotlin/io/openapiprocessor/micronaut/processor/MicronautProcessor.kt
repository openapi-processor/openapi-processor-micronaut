/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.processor

import io.openapiprocessor.api.OpenApiProcessor
import io.openapiprocessor.core.converter.ApiConverter
import io.openapiprocessor.core.converter.ApiOptions
import io.openapiprocessor.core.parser.Parser
import io.openapiprocessor.core.processor.MappingConverter
import io.openapiprocessor.core.processor.MappingReader
import io.openapiprocessor.core.processor.mapping.MappingVersion
import io.openapiprocessor.core.processor.mapping.v2.Mapping as MappingV2
import io.openapiprocessor.core.writer.java.*
import io.openapiprocessor.micronaut.writer.java.HeaderWriter
import io.openapiprocessor.micronaut.writer.java.MappingAnnotationWriter
import io.openapiprocessor.micronaut.writer.java.ParameterAnnotationWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *  Entry point of openapi-processor-micronaut.
 */
class MicronautProcessor: OpenApiProcessor {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

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

            val headerWriter = HeaderWriter()
            val beanValidationFactory = BeanValidationFactory()

            val writer = ApiWriter(
                options,
                InterfaceWriter(
                    options,
                    headerWriter,
                    MethodWriter(
                        options,
                        MappingAnnotationWriter(),
                        ParameterAnnotationWriter(annotations),
                        beanValidationFactory
                    ),
                    annotations,
                    beanValidationFactory,
                    DefaultImportFilter()
                ),
                DataTypeWriter(
                    options,
                    headerWriter,
                    beanValidationFactory),
                StringEnumWriter (headerWriter),
                true
            )

            writer.write (api)
        } catch (e: Exception) {
            log.error("processing failed!", e)
            throw e
        }
    }

    private fun convertOptions(processorOptions: Map<String, *>): ApiOptions {
        val reader = MappingReader()
        val converter = MappingConverter()
        var mapping: MappingVersion? = null

        if (processorOptions.containsKey("mapping")) {
            mapping = reader.read(processorOptions["mapping"].toString())
        }

        val options = ApiOptions()
        options.apiPath = processorOptions["apiPath"].toString()
        options.targetDir = processorOptions["targetDir"].toString()

        if (mapping != null) {
            val packageName: String? = getPackageName(mapping)
            if (packageName != null) {
                options.packageName = packageName
            } else {
                log.warn("no 'options:package-name' set in mapping!")
            }

            val validation: Boolean? = getBeanValidation(mapping)
            if (validation != null) {
                options.beanValidation = validation
            }

            options.typeMappings = converter.convert(mapping)
        } else {
            log.warn("missing 'mapping.yaml' configuration!")
        }

        return options
    }

    private fun getPackageName(mapping: MappingVersion): String? {
        return if (mapping is MappingV2) {
            mapping.options.packageName
        } else {
            null
        }
    }

    private fun getBeanValidation(mapping: MappingVersion): Boolean? {
        return if (mapping is MappingV2) {
            mapping.options.beanValidation
        } else {
            null
        }
    }

}
