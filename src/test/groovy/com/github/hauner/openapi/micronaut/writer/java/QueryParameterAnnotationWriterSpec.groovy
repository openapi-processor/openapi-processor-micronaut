/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package com.github.hauner.openapi.micronaut.writer.java

import io.openapiprocessor.core.model.datatypes.LongDataType
import io.openapiprocessor.core.model.datatypes.ObjectDataType
import io.openapiprocessor.core.model.datatypes.DataTypeConstraints
import io.openapiprocessor.core.model.datatypes.StringDataType
import io.openapiprocessor.micronaut.model.parameters.QueryParameter
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations
import io.openapiprocessor.micronaut.writer.java.ParameterAnnotationWriter
import spock.lang.Specification

class QueryParameterAnnotationWriterSpec extends Specification {
    def writer = new ParameterAnnotationWriter(new MicronautFrameworkAnnotations())
    def target = new StringWriter()

    void "write simple (required, no default value) query parameter" () {
        def param = new QueryParameter('foo', new StringDataType(), true, false)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@QueryValue(value = "foo")'
    }

    void "write simple (optional, with default value) query parameter" () {
        def param = new QueryParameter('foo',
            new StringDataType(new DataTypeConstraints(defaultValue: 'bar'), false),
            false, false)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@QueryValue(value = "foo", defaultValue = "bar")'
    }

    void "writes simple (optional) query parameter with quoted string default value" () {
        def param = new QueryParameter('foo',
            new StringDataType(new DataTypeConstraints (defaultValue: 'bar'), false),
            false, false)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@QueryValue(value = "foo", defaultValue = "bar")'
    }

    void "writes simple (optional) query parameter with quoted number default value" () {
        def param = new QueryParameter('foo',
            new LongDataType (new DataTypeConstraints (defaultValue: 5), false),
            false, false)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@QueryValue(value = "foo", defaultValue = "5")'
    }

    void "writes object query parameter without annotation" () {
        def param = new QueryParameter(
            'foo',
            new ObjectDataType (
                'Foo', '', [
                    foo1: new StringDataType (),
                    foo2: new StringDataType ()
                ], null, false
            ), false, false)

        when:
        writer.write (target, param)

        then:
        target.toString () == ""
    }

}
