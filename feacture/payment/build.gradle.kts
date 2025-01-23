plugins {
    alias(libs.plugins.generic.library)
    alias(libs.plugins.generic.library.compose)
    alias(libs.plugins.generic.hilt)
    alias(libs.plugins.generic.feature)
}

android {
    namespace = "com.bluebirdcorp.iba.feacture.payment"
    compileSdk = 34

    buildFeatures {
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}