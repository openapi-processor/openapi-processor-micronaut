/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut

import io.openapiprocessor.test.*
import io.openapiprocessor.test.test30_D_

val ALL_30: List<TestParams> = listOf(
    test30_DR("bean-validation-introspected"),
    test30_D_("endpoint-http-mapping"),
    test30_DR("params-complex-data-types"),
    test30_D_("params-pageable-mapping"),
    test30_D_("params-path-simple-data-types"),
    test30_DR("params-request-body"),
    test30_DR("params-request-body-multipart-mapping"),
    test30_D_("params-simple-data-types"),
    test30_D_("response-status")
)

val ALL_31: List<TestParams> = listOf(
    test31_DR("bean-validation-introspected"),
    test31_D_("endpoint-http-mapping"),
    test31_DR("params-complex-data-types"),
    test31_D_("params-pageable-mapping"),
    test31_D_("params-path-simple-data-types"),
    test31_DR("params-request-body"),
    test31_DR("params-request-body-multipart-mapping"),
    test31_D_("params-simple-data-types"),
    test31_D_("response-status")
)
