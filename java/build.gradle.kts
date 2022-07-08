import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("momento.java-library-conventions")

    kotlin("jvm")
    id("com.google.protobuf") version "0.8.16"
    idea
}

val grpcProtobufVersion = "3.17.3"
val grpcVersion = "1.39.0"

dependencies {
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    protobuf(files("../proto/"))
}

protobuf {
    var systemOverride = ""
    if (System.getProperty("os.name") == "Mac OS X") {
        println("overiding protobuf artifacts classifier to osx-x86_64 so M1 Macs can find lib")
        systemOverride = ":osx-x86_64"
    }
    protoc {
        artifact = "com.google.protobuf:protoc:$grpcProtobufVersion$systemOverride"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion$systemOverride"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                id("grpc")
            }
            task.generateDescriptorSet = true
            task.descriptorSetOptions.path = "$projectDir/generated-sources/descriptors/client_protos.dsc"
            task.descriptorSetOptions.includeImports = true
            task.descriptorSetOptions.includeSourceInfo = true
        }
    }
}

// TODO: figure out a way to put this in a function or something so that we can put it
// into buildSrc and call it from different libs with different github URLs etc.

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "software.momento.java"
//            groupId = "software.momento"
            artifactId = "client-protos"
//            artifactId = "client-protos-java"
//            version = "0.1.0-SNAPSHOT"

//            artifact(tasks.named("javadocJar"))
            with(pom) {
                name.set(rootProject.name)
                url.set("https://github.com/momentohq/client_protos")
                description.set("Protobuf protos for Momento gRPC services")
                scm {
                    url.set("https://github.com/momentohq/client_protos.git")
                }
                developers {
//                    developer {
//                        id.set("cprice404")
//                        name.set("Chris Price")
//                    }
                }
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
            }
        }
    }
//    repositories {
//        maven {
//            url = if (version.toString().endsWith("SNAPSHOT")) {
//                uri(mavenSnapshotUrl)
//            } else {
//                uri(mavenUrl)
//            }
//            credentials {
//                username = sonatypeUsername
//                password = sonatypePassword
//            }
//        }
//    }
}

//
//configure<PublishingExtension> {
//    components.all {
//        publications.withType<MavenPublication> {
//            artifact(tasks.named("javadocJar"))
//            with(pom) {
//                name.set(rootProject.name)
//                url.set("https://github.com/momentohq/client_protos")
//                description.set("Protobuf protos for Momento gRPC services")
//                scm {
//                    url.set("https://github.com/momentohq/client_protos.git")
//                }
//                developers {
////                    developer {
////                        id.set("cprice404")
////                        name.set("Chris Price")
////                    }
//                }
//                licenses {
//                    license {
//                        name.set("The Apache License, Version 2.0")
//                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                    }
//                }
//            }
//        }
//    }
//    repositories {
//        maven {
//            url = if (version.toString().endsWith("SNAPSHOT")) {
//                uri(mavenSnapshotUrl)
//            } else {
//                uri(mavenUrl)
//            }
//            credentials {
//                username = sonatypeUsername
//                password = sonatypePassword
//            }
//        }
//    }
//}
