/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("com.draeger.medical.version-conventions")
    id("com.draeger.medical.java-conventions")
    id("org.somda.sdc.xjc")
}

dependencies {
    api(enforcedPlatform(libs.com.draeger.medical.sdccc.bom))
    api(libs.org.glassfish.jaxb.jaxb.core)
    api(libs.org.glassfish.jaxb.jaxb.runtime)
    api(libs.jakarta.xml.bind.jakarta.xml.bind.api)
    api(libs.io.github.threeten.jaxb.threeten.jaxb.core)
    api(libs.org.jvnet.jaxb.jaxb.plugins)
    implementation(libs.jakarta.xml.bind.jakarta.xml.bind.api)
}

description = "DPWS model"


description = "Implements the model for Devices Profile for Web-Services (DPWS) 1.1 and referenced standards."

val schemaDir = "src/main/"

xjc {
    schemaLocation = layout.projectDirectory.dir(schemaDir)
    args = listOf(
        "-nv", // <strict>false</strict>
    )
}

val testsJar by tasks.registering(Jar::class) {
    archiveClassifier.set("tests")
    from(sourceSets["test"].output)
}
