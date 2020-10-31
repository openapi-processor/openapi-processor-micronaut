/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.processor

import io.openapiprocessor.core.parser.ParserType
import com.github.hauner.openapi.test.TestSet
import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * using Junit so IDEA adds a "<Click to see difference>" when using assertEquals().
 */
@RunWith(Parameterized)
class ProcessorJimsFileSystemTest extends EndToEndBase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<TestSet> sources () {
        // the swagger parser does not work with a custom FileSystem so we just run the test with
        // openapi4j

        TestSets.ALL.collect {
           new TestSet (name: it, processor: new MicronautProcessor (), parser: ParserType.OPENAPI4J)
        }
    }

    ProcessorJimsFileSystemTest (TestSet testSet) {
        super (testSet)
    }

    @Test
    void "jimfs - processor creates expected files for api set "() {
        runOnCustomFileSystem (Jimfs.newFileSystem (Configuration.unix ()))
    }

}
