plugins {
    alias(libs.plugins.generic.application)
    alias(libs.plugins.generic.hilt)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.bluebirdcorp.iba.softpos"
    compileSdk = 34
}

dependencies {
    implementation(projects.common.utils)
    implementation(projects.feacture.payment)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}