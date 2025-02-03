plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.generic.hilt)
    alias(libs.plugins.generic.android.room)
}

android {
    namespace = "com.bluebirdcorp.iba.data.model"
    compileSdk = 34
}

dependencies {
    implementation(projects.domain.interfaces)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}