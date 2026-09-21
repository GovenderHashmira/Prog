// Top-level build file — declares plugin versions for all sub-modules.
// `apply false` means: resolve the plugin here, but don't apply it to the root project.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}