/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.writer.java

import spock.lang.Specification

class HeaderWriterSpec extends Specification {

    void "writes generated header"() {
        def headerWriter = new HeaderWriter()
        def target = new StringWriter()

        when:
        headerWriter.write (target)

        then:
        target.toString () == HeaderWriterKt.HEADER
    }

}
