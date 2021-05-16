/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.model.datatypes.DataTypeConstraints
import io.openapiprocessor.core.model.datatypes.StringDataType
import io.openapiprocessor.core.model.parameters.HeaderParameter
import io.openapiprocessor.micronaut.MicronautFrameworkAnnotations
import spock.lang.Specification

class HeaderParameterAnnotationWriterSpec extends Specification {
    def writer = new ParameterAnnotationWriter(new MicronautFrameworkAnnotations ())
    def target = new StringWriter()

    void "write simple (required) header parameter" () {
        def param = new HeaderParameter(
            'foo', new StringDataType(), false, false, null)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@Header(value = "foo")'
    }

    void "write simple (optional, with default value) header parameter" () {
        def param = new HeaderParameter('foo',
            new StringDataType(new DataTypeConstraints(defaultValue: 'bar'),
                false, null),
            false, false, null)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@Header(value = "foo", defaultValue = "bar")'
    }

}
