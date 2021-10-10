/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.framework.FrameworkAnnotation
import io.openapiprocessor.core.model.datatypes.ModelDataType
import io.openapiprocessor.core.writer.java.BeanValidationFactory
import io.openapiprocessor.core.writer.java.BeanValidationInfo
import io.openapiprocessor.core.writer.java.BeanValidationInfoSimple

class BeanValidations: BeanValidationFactory() {

    override fun validate(dataType: ModelDataType): BeanValidationInfo {
        return BeanValidationInfoSimple(
            dataType,
            setOf(INTROSPECTED.fullyQualifiedName),
            listOf(INTROSPECTED.annotationName))
    }

}

private val INTROSPECTED = FrameworkAnnotation(
    "Introspected", "io.micronaut.core.annotation")
