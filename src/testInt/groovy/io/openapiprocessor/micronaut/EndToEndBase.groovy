/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut

import io.openapiprocessor.micronaut.writer.java.HeaderWriterKt
import spock.lang.Specification

class EndToEndBase extends Specification {

    void setupSpec () {
        // set a "fixed" header, we don't want moving version/date/time parts

        HeaderWriterKt.HEADER = """\
/*
 * DO NOT MODIFY - this class was auto generated by openapi-processor-micronaut
 *
 * test
 * time
 * https://docs.openapiprocessor.io/micronaut 
 */

"""
    }

}