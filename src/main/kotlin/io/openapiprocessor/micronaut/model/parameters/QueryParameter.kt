/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.model.parameters

import io.openapiprocessor.core.model.datatypes.DataType
import io.openapiprocessor.core.model.datatypes.ObjectDataType
import io.openapiprocessor.core.model.parameters.ParameterBase

/**
 * OpenAPI query parameter.
 */
class QueryParameter(
    name: String,
    dataType: DataType,
    required: Boolean = false,
    deprecated: Boolean = false,
    description: String? = null
) : ParameterBase(name, dataType, required, deprecated, description) {

    /**
     * controls if a parameter should have a {@code @RequestParam} annotation.
     */
    override val withAnnotation: Boolean
        get() {
            // POJO's should NOT be annotated
            if (dataType is ObjectDataType) {
                return false
            }

            return true
        }

}
