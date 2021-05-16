/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut

import io.openapiprocessor.core.parser.ParserType
import io.openapiprocessor.test.TestSet
import io.openapiprocessor.test.TestSetRunner
import spock.lang.TempDir
import spock.lang.Unroll

//@Ignore
class ProcessorPendingTest extends EndToEndBase {

    static Collection<TestSet> sources () {
        return [
            new TestSet(name: 'params-request-body-multipart-mapping', processor: new MicronautProcessor (), parser: ParserType.SWAGGER),
            new TestSet(name: 'params-request-body-multipart-mapping', processor: new MicronautProcessor (), parser: ParserType.OPENAPI4J),
        ]
    }

    @TempDir
    public File folder

    @Unroll
    void "native - #testSet"() {
        def runner = new TestSetRunner (testSet)
        def success = runner.runOnNativeFileSystem (folder)

        expect:
        assert success: "** found differences! **"

        where:
        testSet << sources ()
    }

}
