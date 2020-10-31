/*
 * Copyright 2019-2020 the original authors
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

package io.openapiprocessor.micronaut.writer.java

import io.openapiprocessor.core.framework.FrameworkAnnotations
import io.openapiprocessor.core.model.RequestBody
import io.openapiprocessor.core.model.parameters.Parameter
import io.openapiprocessor.core.writer.java.ParameterAnnotationWriter as CoreParameterAnnotationWriter
import java.io.Writer

/**
 * micronaut parameter annotation writer
 *
 * @author Martin Hauner
 */
class ParameterAnnotationWriter(
    private val annotations: FrameworkAnnotations

): CoreParameterAnnotationWriter {

    override fun write(target: Writer, parameter: Parameter) {
//        if (parameter.deprecated) {
//            target.write ("@Deprecated ")
//        }

        if (parameter is RequestBody) {
            target.write(createAnnotation(parameter))
        } else {
            target.write(createAnnotation(parameter))
        }
    }

    private fun createAnnotation(requestBody: RequestBody): String {
        return getAnnotationName(requestBody)
    }

    private fun createAnnotation(parameter: Parameter): String {
        if (! parameter.withAnnotation) {
            return ""
        }

        var annotation = getAnnotationName (parameter)

        if (! parameter.withParameters) {
            return annotation
        }

        annotation += "("
        annotation += "value = " + quote (parameter.name)

        if (hasDefault (parameter)) {
            annotation += ", "
            annotation += "defaultValue = ${getDefault(parameter)}"
        }

        annotation += ")"
        return annotation
    }

    private fun getAnnotationName(parameter: Parameter): String {
        return annotations.getAnnotation (parameter).annotationName
    }

    private fun hasDefault(parameter: Parameter): Boolean {
        return parameter.constraints.hasDefault()
    }

    private fun getDefault(parameter: Parameter): String {
        return quote(parameter.constraints.default.toString())
    }

    private fun quote(content: String): String {
        return '"' + content + '"'
    }

}
