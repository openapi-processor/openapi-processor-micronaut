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

//@Ignore
@RunWith(Parameterized)
class ProcessorPendingTest extends EndToEndBase {

    @Parameterized.Parameters(name = "{0}")
    static Collection<TestSet> sources () {
        return [
            new TestSet(name: 'params-request-body-multipart-mapping', processor: new MicronautProcessor (), parser: ParserType.SWAGGER),
            new TestSet(name: 'params-request-body-multipart-mapping', processor: new MicronautProcessor (), parser: ParserType.OPENAPI4J),
        ]
    }

    ProcessorPendingTest (TestSet testSet) {
        super (testSet)
    }

    @Test
    void "native - processor creates expected files for api set "() {
        runOnNativeFileSystem ()
    }

}
