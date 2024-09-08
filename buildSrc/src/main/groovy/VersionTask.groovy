import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

/**
 * simple task to create a Version class.
 */
class VersionTask extends DefaultTask {

    /**
     * Target directory for the generated version class.
     *
     * Used by gradle for the up-to-date check.
     */
    @OutputDirectory
    String targetDir

    @Internal
    String version

    /**
     * generate the version class.
     */
    @TaskAction
    void generateVersion () {
        def path = Path.of (targetDir, "version", "io", "openapiprocessor", "micronaut")
        Files.createDirectories(path)

        def target = path.resolve ("Version.java")

        target.text = """\
/*
 * DO NOT MODIFY - this file was auto generated by buildSrc/src/main/groovy/VersionPlugin.groovy
 *
 * ${Instant.now ().toString ()}
 */

package io.openapiprocessor.micronaut;
 
/**
 * Version
 */
public class Version {
    /** current version */
    public static final String version = "${version}"; 
}
 
"""
    }

}
