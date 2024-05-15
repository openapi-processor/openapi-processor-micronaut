/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-processor-micronaut
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.micronaut.processor

import io.openapiprocessor.api.v2.Version
import io.openapiprocessor.core.version.GitHubVersionException
import io.openapiprocessor.core.version.GitHubVersionProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 *  Entry point of openapi-processor-micronaut loaded via [java.util.ServiceLoader]. by the v2 interface
 *  [io.openapiprocessor.api.v1.OpenApiProcessor].
 *
 *  the v2 interfaces *must* be implemented by its own lass and not by [MicronautService] to be downward
 *  compatible with gradle/maven plugin versions that do not know the v2 interfaces.
 */
class MicronautServiceV2(
    private val provider: GitHubVersionProvider = GitHubVersionProvider("openapi-processor-micronaut"),
    private val testMode: Boolean = false):
    io.openapiprocessor.api.v2.OpenApiProcessor,
    io.openapiprocessor.api.v2.OpenApiProcessorVersion
{
    private val log: Logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun getName(): String {
        return "spring"
    }

    override fun run(processorOptions: MutableMap<String, *>) {
        try {
            val processor = MicronautProcessor()
            if (testMode) {
                processor.enableTestMode()
            }
            processor.run(processorOptions)

        } catch (ex: Exception) {
            throw ex
        }
    }

    override fun getVersion(): String {
        return io.openapiprocessor.micronaut.Version.version
    }

    override fun getLatestVersion(): Version {
        return provider.getVersion()
    }

    override fun hasNewerVersion(): Boolean {
        try {
            val version = version
            val latest = latestVersion

            if (latest.name > version) {
                log.info("openapi-processor-micronaut version ${latest.name} is available! I'm version ${version}.")
                return true
            }

            return false
        } catch (ex: GitHubVersionException) {
            // just ignore, do not complain
            return false
        }
    }
}
