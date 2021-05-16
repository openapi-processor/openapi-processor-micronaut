/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.model.RequestBody
import io.openapiprocessor.core.model.datatypes.DataTypeConstraints
import io.openapiprocessor.core.model.datatypes.DataTypeName
import io.openapiprocessor.core.model.datatypes.LongDataType
import io.openapiprocessor.core.model.datatypes.ObjectDataType
import io.openapiprocessor.core.model.datatypes.StringDataType
import io.openapiprocessor.core.model.parameters.CookieParameter
import io.openapiprocessor.core.model.parameters.HeaderParameter
import io.openapiprocessor.core.model.parameters.PathParameter
import io.openapiprocessor.micronaut.model.parameters.QueryParameter
import io.openapiprocessor.micronaut.MicronautFrameworkAnnotations
import spock.lang.Specification
import spock.lang.Unroll

class ParameterAnnotationWriterSpec extends Specification {
    def writer = new ParameterAnnotationWriter(new MicronautFrameworkAnnotations ())
    def target = new StringWriter()

    @Unroll
    void "writes simple (optional) query parameter with quoted string default value" () {
        def param = clazz.newInstance('foo',
            new StringDataType(createConstraints ('bar'), false, null),
            false, false, null)

        when:
        writer.write (target, param)

        then:
        target.toString () == """${annotation}(value = "foo", defaultValue = "bar")"""

        where:
        clazz           | annotation
        QueryParameter  | "@QueryValue"
        PathParameter   | "@PathVariable"
        CookieParameter | "@CookieValue"
        HeaderParameter | "@Header"
    }

    void "writes simple (optional) query parameter with quoted number default value" () {
        def param = clazz.newInstance ('foo',
            new LongDataType (createConstraints (5), false, null),
            false, false, null)

        when:
        writer.write (target, param)

        then:
        target.toString () == """${annotation}(value = "foo", defaultValue = "5")"""

        where:
        clazz           | annotation
        QueryParameter  | "@QueryValue"
        PathParameter   | "@PathVariable"
        CookieParameter | "@CookieValue"
        HeaderParameter | "@Header"
    }

    void "writes request body parameter" () {
        def body = new RequestBody (
            'body', 'application/json',
            new ObjectDataType (new DataTypeName('FooRequestBody', 'FooRequestBody'), '',
                ['foo': new StringDataType ()], null, false, null),
            true, false)

        when:
        writer.write (target, body)

        then:
        target.toString () == "@Body"
    }

    DataTypeConstraints createConstraints(def defaultValue) {
        def c = new DataTypeConstraints()
        c.defaultValue = defaultValue
        c
    }

}
