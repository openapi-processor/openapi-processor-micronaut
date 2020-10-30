/*
 * Copyright 2020 the original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.openapiprocessor.micronaut.model.parameters

import io.openapiprocessor.core.model.datatypes.DataType
import io.openapiprocessor.core.model.datatypes.ObjectDataType
import io.openapiprocessor.core.model.parameters.ParameterBase

/**
 * OpenAPI query parameter.
 *
 * @author Martin Hauner
 */
class QueryParameter(name: String, dataType: DataType, required: Boolean, deprecated: Boolean)
    : ParameterBase(name, dataType, required, deprecated) {

    /**
     * controls if a {@code @RequestParam} should have any parameters.
     */
    override val withParameters: Boolean
        get() {
            // Pojo should NOT be annotated
            if (dataType is ObjectDataType) {
                return false
            }

            return true
        }

}
