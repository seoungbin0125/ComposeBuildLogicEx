plugins {
    alias(libs.plugins.generic.library)
    alias(libs.plugins.generic.library.compose)
    alias(libs.plugins.generic.hilt)
    alias(libs.plugins.generic.feature)
}

android {
    namespace = "com.bluebirdcorp.softpos.feacture.payment"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.common.utils)
    implementation(projects.domain.usecase)
    implementation(projects.data.repository)
    implementation(projects.domain.interfaces)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.ui.tooling.preview.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
}