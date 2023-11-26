/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.converter.ApiOptions
import io.openapiprocessor.core.model.Annotation
import io.openapiprocessor.core.model.datatypes.ModelDataType
import io.openapiprocessor.core.writer.java.*

class BeanValidations(options: ApiOptions): BeanValidationFactory(options) {

    override fun validate(dataType: ModelDataType): BeanValidationInfo {
        return BeanValidationInfoSimple(dataType, listOf(INTROSPECTED))
    }
}

private val INTROSPECTED = Annotation("io.micronaut.core.annotation.Introspected")
