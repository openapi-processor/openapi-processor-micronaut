/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.model.parameters.CookieParameter
import io.openapiprocessor.core.model.datatypes.DataTypeConstraints
import io.openapiprocessor.core.model.datatypes.StringDataType
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations
import spock.lang.Specification

class CookieParameterAnnotationWriterSpec extends Specification {
    def writer = new ParameterAnnotationWriter(new MicronautFrameworkAnnotations ())
    def target = new StringWriter()

    void "write simple (required) cookie parameter" () {
        def param = new CookieParameter(
            'foo', new StringDataType(), true, false, null)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@CookieValue(value = "foo")'
    }

    void "write simple (optional, with default value) cookie parameter" () {
        def param = new CookieParameter('foo',
            new StringDataType('string',
                new DataTypeConstraints(defaultValue: 'bar'),
                false, null),
            false, false, null)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@CookieValue(value = "foo", defaultValue = "bar")'
    }

}
