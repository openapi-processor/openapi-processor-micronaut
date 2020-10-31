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

package com.github.hauner.openapi.micronaut.writer.java

import io.openapiprocessor.core.model.datatypes.DataTypeConstraints
import io.openapiprocessor.core.model.datatypes.StringDataType
import io.openapiprocessor.core.model.parameters.HeaderParameter
import io.openapiprocessor.micronaut.processor.MicronautFrameworkAnnotations
import io.openapiprocessor.micronaut.writer.java.ParameterAnnotationWriter
import spock.lang.Specification

class HeaderParameterAnnotationWriterSpec extends Specification {
    def writer = new ParameterAnnotationWriter(new MicronautFrameworkAnnotations ())
    def target = new StringWriter()

    void "write simple (required) header parameter" () {
        def param = new HeaderParameter('foo', new StringDataType(), false, false)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@Header(value = "foo")'
    }

    void "write simple (optional, with default value) header parameter" () {
        def param = new HeaderParameter('foo',
            new StringDataType(new DataTypeConstraints(defaultValue: 'bar'), false),
            false, false)

        when:
        writer.write (target, param)

        then:
        target.toString () == '@Header(value = "foo", defaultValue = "bar")'
    }

}
