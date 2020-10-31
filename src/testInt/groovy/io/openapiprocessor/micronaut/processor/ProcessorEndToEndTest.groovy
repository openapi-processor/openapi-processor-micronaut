/*
 * Copyright © 2020 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.processor

import io.openapiprocessor.core.parser.ParserType
import com.github.hauner.openapi.test.TestSet
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * using Junit so IDEA adds a "<Click to see difference>" when using assertEquals().
 */
@RunWith(Parameterized)
class ProcessorEndToEndTest extends EndToEndBase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<TestSet> sources () {
        def swagger = TestSets.ALL.collect {
           new TestSet (name: it, processor: new MicronautProcessor (), parser: ParserType.SWAGGER)
        }

        def openapi4j = TestSets.ALL.collect {
           new TestSet (name: it, processor: new MicronautProcessor (), parser: ParserType.OPENAPI4J)
        }

        swagger + openapi4j
    }

    ProcessorEndToEndTest (TestSet testSet) {
        super (testSet)
    }

    @Test
    void "native - processor creates expected files for api set "() {
        runOnNativeFileSystem ()
    }

}
