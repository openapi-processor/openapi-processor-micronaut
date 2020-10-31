/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package com.github.hauner.openapi.micronaut.writer.java

import io.openapiprocessor.core.model.parameters.PathParameter
import io.openapiprocessor.core.model.datatypes.DataTypeConstraints
import io.openapiprocessor.core.model.datatypes.StringDataType
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations
import io.openapiprocessor.micronaut.writer.java.ParameterAnnotationWriter
import spock.lang.Specification

class PathParameterAnnotationWriterSpec extends Specification {
    def writer = new ParameterAnnotationWriter(new MicronautFrameworkAnnotations())
    def target = new StringWriter()

    void "write simple (required, no default value) path parameter" () {
        def param = new PathParameter('foo', new StringDataType(), false, false)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@PathVariable(value = "foo")'
    }

    void "write simple (optional, with default value) path parameter" () {
        def param = new PathParameter('foo',
            new StringDataType(new DataTypeConstraints(defaultValue: 'bar'), false),
            false, false)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@PathVariable(value = "foo", defaultValue = "bar")'
    }

}
