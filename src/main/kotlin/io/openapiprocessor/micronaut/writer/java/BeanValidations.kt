/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.framework.FrameworkAnnotation
import io.openapiprocessor.core.model.datatypes.ModelDataType
import io.openapiprocessor.core.writer.java.*
import io.openapiprocessor.core.writer.java.Annotation

class BeanValidations(format: BeanValidationFormat = BeanValidationFormat.JAVAX)
    : BeanValidationFactory(format) {

    override fun validate(dataType: ModelDataType): BeanValidationInfo {
        return BeanValidationInfoSimple(
            dataType,
            listOf(Annotation(INTROSPECTED.fullyQualifiedName)))
    }

}

private val INTROSPECTED = FrameworkAnnotation(
    "Introspected", "io.micronaut.core.annotation")
