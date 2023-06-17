/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.processor

import io.openapiprocessor.core.framework.FrameworkBase
import io.openapiprocessor.core.model.datatypes.DataType
import io.openapiprocessor.core.model.parameters.Parameter
import io.openapiprocessor.micronaut.model.parameters.QueryParameter
import io.openapiprocessor.core.parser.Parameter as ParserParameter

/**
 * Micronaut model factory.
 */
class MicronautFramework: FrameworkBase() {

    @Override
    override fun createQueryParameter(parameter: ParserParameter, dataType: DataType): Parameter {
        return QueryParameter (
            parameter.getName(),
            dataType,
            parameter.isRequired(),
            parameter.isDeprecated())
    }

}
