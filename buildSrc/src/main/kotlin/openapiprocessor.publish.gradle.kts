plugins {
    `maven-publish`
    signing
   id("io.openapiprocessor.build.plugin.publish")
}

publishing {
    publications {
        create<MavenPublication>("openapiprocessor") {
            from(components["java"])
        }
    }
}

publishingCentral {
    deploymentName = "micronaut"
    waitFor = "VALIDATED"
}
