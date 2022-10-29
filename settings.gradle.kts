dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            val kotlin = "1.7.20"

            version("gradle-plugin-kotlin", kotlin)
            version("gradle-plugin-ksp", "$kotlin-1.0.7")
            version("gradle-plugin-android", "7.3.1")
            version("gradle-plugin-protobuf", "0.8.19")
            version("gradle-plugin-versions", "0.42.0")

            version("kotlinx-coroutines", "1.6.4")
            version("kotlinx-serialization", "1.4.1")
            version("ktor", "2.1.2")
            version("room", "2.5.0-beta01")
            version("lifecycle", "2.6.0-alpha02")
            version("navigation", "2.6.0-alpha02")
            version("compose", "1.3.0-rc01")
            version("wear-compose", "1.0.0-alpha18")
            version("compose-compiler", "1.3.2")
            version("material3", "1.0.0-alpha15")
            version("accompanist", "0.26.5-rc")

            bundle(
                "kotlinx",
                listOf(
                    "kotlinx-coroutines-android",
                    "kotlinx-coroutines-play-services",

                    "kotlinx-serialization-json",
                    "kotlinx-serialization-protobuf",

                    "kotlinx-datetime"
                )
            )

            bundle(
                "ktor",
                listOf(
                    "ktor-client-core",
                    "ktor-client-android",
                    "ktor-client-logging",
                    "ktor-client-content-negotiation",
                    "ktor-serialization-kotlinx-json"
                )
            )

            bundle(
                "google",
                listOf(
                    "ksp-symbol-processing-api",
                    "protobuf-kotlin-lite"
                )
            )

            bundle(
                "google-app",
                listOf(
                    "accompanist-navigation-animation",
                    "accompanist-navigation-material",
                    "accompanist-swiperefresh",
                    "accompanist-systemuicontroller",
                    "accompanist-placeholder"
                )
            )

            bundle(
                "google-wear",
                listOf(
                    "play-services-wearable",
                )
            )

            bundle(
                "androidx",
                listOf(
                    "core-ktx",
                    "appcompat",
                    "window",
                    "activity-ktx",
                    "activity-compose",
                    "fragment-ktx",
                    "palette-ktx",
                    "work-runtime-ktx",
                    "preference-ktx",
//                    "slice-builders-ktx",
                    "datastore",
                    "benchmark-junit4",

                    "room-runtime",
                    "room-ktx",
                    "room-paging",

                    "lifecycle-runtime-ktx",
                    "lifecycle-viewmodel-ktx",
                    "lifecycle-viewmodel-compose",
                    "lifecycle-viewmodel-savedstate",
                    "lifecycle-livedata-ktx",

                    // compose
                    "ui",
                    "ui-viewbinding",
                    "ui-tooling",
                    "ui-tooling-preview",
                    "ui-util",
                    "material-icons-core",
                    "material-icons-extended",
                    "foundation",
                    "runtime-livedata",
                    "ui-text-google-fonts",
                    "constraintlayout",
                    "metrics-performance",
                    "compose-foundation",
                )
            )

            bundle(
                "androidx-app",
                listOf(
                    "core-google-shortcuts",

                    "navigation-runtime-ktx",
                    "navigation-ui-ktx",
                    "navigation-compose",

                    "material",
                    "material3",
                    "material3-window-size",

                    // compose
                    "compose-material"
                )
            )

            bundle(
                "androidx-wear",
                listOf(
//                    "compose-foundation-wear",
                    "compose-material-wear",
                    "compose-navigation-wear"
                )
            )

            library(
                "kotlinx-coroutines-android",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-android"
            ).versionRef("kotlinx-coroutines")

            library(
                "kotlinx-coroutines-play-services",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-play-services"
            ).versionRef("kotlinx-coroutines")

            library(
                "kotlinx-serialization-json",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-json"
            ).versionRef("kotlinx-serialization")

            library(
                "kotlinx-serialization-protobuf",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-protobuf"
            ).versionRef("kotlinx-serialization")

            library("kotlinx-datetime", "org.jetbrains.kotlinx", "kotlinx-datetime")
                .version("0.4.0")

            library("ktor-client-core", "io.ktor", "ktor-client-core")
                .versionRef("ktor")

            library("ktor-client-android", "io.ktor", "ktor-client-android")
                .versionRef("ktor")

            library("ktor-client-logging", "io.ktor", "ktor-client-logging")
                .versionRef("ktor")

            library("ktor-client-content-negotiation", "io.ktor", "ktor-client-content-negotiation")
                .versionRef("ktor")

            library("ktor-serialization-kotlinx-json", "io.ktor", "ktor-serialization-kotlinx-json")
                .versionRef("ktor")

            library("play-services-wearable", "com.google.android.gms", "play-services-wearable")
                .version("18.0.0")

            library("protobuf-kotlin-lite", "com.google.protobuf", "protobuf-kotlin-lite")
                .version("3.21.7")

            library("ksp-symbol-processing-api", "com.google.devtools.ksp", "symbol-processing-api")
                .versionRef("gradle-plugin-ksp")

            library("datastore", "androidx.datastore", "datastore")
                .version("1.0.0")

            library("room-runtime", "androidx.room", "room-runtime")
                .versionRef("room")

            library("room-ktx", "androidx.room", "room-ktx")
                .versionRef("room")

            library("room-paging", "androidx.room", "room-paging")
                .versionRef("room")

            library("room-compiler", "androidx.room", "room-compiler")
                .versionRef("room")

            library("collection-ktx", "androidx.collection", "collection-ktx")
                .version("1.2.0")

            library("core-ktx", "androidx.core", "core-ktx")
                .version("1.9.0")

            library("core-google-shortcuts", "androidx.core", "core-google-shortcuts")
                .version("1.1.0-rc01")

            library("appcompat", "androidx.appcompat", "appcompat")
                .version("1.7.0-alpha01")

            library("window", "androidx.window", "window")
                .version("1.1.0-alpha03")

            library("activity-ktx", "androidx.activity", "activity-ktx")
                .version("1.7.0-alpha01")

            library("activity-compose", "androidx.activity", "activity-compose")
                .version("1.7.0-alpha01")

            library("fragment-ktx", "androidx.fragment", "fragment-ktx")
                .version("1.6.0-alpha03")

            library("palette-ktx", "androidx.palette", "palette-ktx")
                .version("1.0.0")

            library("work-runtime-ktx", "androidx.work", "work-runtime-ktx")
                .version("2.8.0-beta01")

            library("preference-ktx", "androidx.preference", "preference-ktx")
                .version("1.2.0")

            library("slice-builders-ktx", "androidx.slice", "slice-builders-ktx")
                .version("1.0.0-alpha08")

            library("benchmark-junit4", "androidx.benchmark", "benchmark-junit4")
                .version("1.2.0-alpha05")

            library("lifecycle-runtime-ktx", "androidx.lifecycle", "lifecycle-runtime-ktx")
                .versionRef("lifecycle")

            library("lifecycle-viewmodel-ktx", "androidx.lifecycle", "lifecycle-viewmodel-ktx")
                .versionRef("lifecycle")

            library(
                "lifecycle-viewmodel-compose",
                "androidx.lifecycle",
                "lifecycle-viewmodel-compose"
            ).versionRef("lifecycle")

            library(
                "lifecycle-viewmodel-savedstate",
                "androidx.lifecycle",
                "lifecycle-viewmodel-savedstate"
            ).versionRef("lifecycle")

            library("lifecycle-livedata-ktx", "androidx.lifecycle", "lifecycle-livedata-ktx")
                .versionRef("lifecycle")

            library("navigation-runtime-ktx", "androidx.navigation", "navigation-runtime-ktx")
                .versionRef("navigation")

            library("navigation-ui-ktx", "androidx.navigation", "navigation-ui-ktx")
                .versionRef("navigation")

            library("navigation-compose", "androidx.navigation", "navigation-compose")
                .versionRef("navigation")

            library("material", "com.google.android.material", "material")
                .version("1.8.0-alpha01")

            library("material3", "androidx.compose.material3", "material3")
                .versionRef("material3")

            library(
                // cannot have `class` as alias postfix because gradle fails
                "material3-window-size",
                "androidx.compose.material3",
                "material3-window-size-class"
            ).versionRef("material3")

            library("ui", "androidx.compose.ui", "ui")
                .versionRef("compose")

            library("ui-viewbinding", "androidx.compose.ui", "ui-viewbinding")
                .versionRef("compose")

            library("ui-tooling", "androidx.compose.ui", "ui-tooling")
                .versionRef("compose")

            library("ui-tooling-preview", "androidx.compose.ui", "ui-tooling-preview")
                .versionRef("compose")

            library("ui-util", "androidx.compose.ui", "ui-util")
                .versionRef("compose")

            library("compose-material", "androidx.compose.material", "material")
                .versionRef("compose")

            library("material-icons-core", "androidx.compose.material", "material-icons-core")
                .versionRef("compose")

            library(
                "material-icons-extended",
                "androidx.compose.material",
                "material-icons-extended"
            ).versionRef("compose")

            library("foundation", "androidx.compose.foundation", "foundation")
                .versionRef("compose")

            library("runtime-livedata", "androidx.compose.runtime", "runtime-livedata")
                .versionRef("compose")

            library("ui-text-google-fonts", "androidx.compose.ui", "ui-text-google-fonts")
                .version("1.3.0-rc01")

            library("constraintlayout", "androidx.constraintlayout", "constraintlayout-compose")
                .version("1.1.0-alpha04")

            library("metrics-performance", "androidx.metrics", "metrics-performance")
                .version("1.0.0-alpha03")

            library("compose-foundation", "androidx.wear.compose", "compose-foundation")
                .version("1.0.2")

            library("compose-material-wear", "androidx.wear.compose", "compose-material")
                .version("1.0.2")

            library("compose-navigation-wear", "androidx.wear.compose", "compose-navigation")
                .version("1.0.2")

            library(
                "accompanist-navigation-animation",
                "com.google.accompanist",
                "accompanist-navigation-animation"
            ).versionRef("accompanist")

            library(
                "accompanist-navigation-material",
                "com.google.accompanist",
                "accompanist-navigation-material"
            ).versionRef("accompanist")

            library(
                "accompanist-swiperefresh",
                "com.google.accompanist",
                "accompanist-swiperefresh"
            ).versionRef("accompanist")

            library(
                "accompanist-systemuicontroller",
                "com.google.accompanist",
                "accompanist-systemuicontroller"
            ).versionRef("accompanist")

            library("accompanist-placeholder", "com.google.accompanist", "accompanist-placeholder")
                .versionRef("accompanist")

            library("slf4j-simple", "org.slf4j", "slf4j-simple")
                .version("2.0.3")
        }
    }
}

rootProject.name = "Hacker News"
include(":app")
include(":wear")
include(":shared")
