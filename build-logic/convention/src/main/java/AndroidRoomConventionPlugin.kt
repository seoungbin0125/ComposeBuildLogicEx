
import androidx.room.gradle.RoomExtension
import com.bluebirdcorp.iba.convention.libs
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")
            apply(plugin = "androidx.room")

            extensions.configure<KspExtension> {
                arg("room.generateKotlin", "true")
            }

            extensions.configure<RoomExtension> {
                // The schemas directory contains a schema file for each version of the Room database.
                // This is required to enable Room auto migrations.
                // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                "implementation"(libs.findLibrary("room.runtime").get())
                "implementation"(libs.findLibrary("room.ktx").get())
                "ksp"(libs.findLibrary("room.compiler").get())
            }

//            dependencies {
//                add("implementation", libs.findLibrary("room.runtime").get())
//                add("implementation", libs.findLibrary("room.ktx").get())
//                add("implementation", libs.findLibrary("room.compiler").get())
//                add("ksp", libs.findLibrary("room.compiler").get())
//            }
        }
    }
}
//
//class AndroidRoomConventionPlugin: Plugin<Project> {
//    override fun apply(target: Project) {
//        with(target) {
//            with(pluginManager) {
//                apply("androidx.room")
//                apply("com.google.devtools.ksp")
//            }
//
//            extensions.configure<RoomExtension> {
//                schemaDirectory("$projectDir/schemas")
//            }
//
//            dependencies {
//                add("implementation", libs.findLibrary("room.runtime").get())
//                add("implementation", libs.findLibrary("room.ktx").get())
//                add("implementation", libs.findLibrary("room.compiler").get())
//            }
//        }
//    }
//}