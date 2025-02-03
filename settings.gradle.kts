pluginManagement {

    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

rootProject.name = "DemoIbaSoftpos"
include(":app")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":data:datasource")
include(":data:repository")
include(":domain:model")
include(":feacture:payment")
include(":data:model")
include(":common:utils")
include(":domain:interfaces")
include(":domain:usecase")
