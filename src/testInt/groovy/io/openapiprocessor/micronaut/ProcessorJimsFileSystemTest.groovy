/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import io.openapiprocessor.core.parser.ParserType
import io.openapiprocessor.test.FileSupport
import io.openapiprocessor.test.TestSet
import io.openapiprocessor.test.TestSetRunner
import spock.lang.Unroll


/**
 * run integration tests with Jimfs.
 */
class ProcessorJimsFileSystemTest extends EndToEndBase {

    static Collection<TestSet> sources () {
        // the swagger parser does not work with a custom FileSystem so we just run the test with
        // openapi4j

        TestSets.ALL.collect {
           new TestSet (name: it, processor: new MicronautProcessor (), parser: ParserType.OPENAPI4J)
        }
    }

    @Unroll
    void "jimfs - #testSet"() {
        def runner = new TestSetRunner (testSet, new FileSupport(getClass ()))
        def success = runner.runOnCustomFileSystem (Jimfs.newFileSystem (Configuration.unix ()))

        expect:
        assert success: "** found differences! **"

        where:
        testSet << sources ()
    }

}
