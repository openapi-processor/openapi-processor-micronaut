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

/**
 * run integration tests.
 */
class ProcessorEndToEndTest extends EndToEndBase {

    static Collection<TestSet> sources () {
        def swagger = TestSets.ALL.collect {
           new TestSet (name: it, processor: new MicronautProcessor (), parser: ParserType.SWAGGER)
        }

        def openapi4j = TestSets.ALL.collect {
           new TestSet (name: it, processor: new MicronautProcessor (), parser: ParserType.OPENAPI4J)
        }

        swagger + openapi4j
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
