import net.saliman.gradle.plugin.properties.PropertiesPlugin

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.50")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.0.0")
        classpath("net.saliman:gradle-properties-plugin:1.4.6")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
    }
    apply<PropertiesPlugin>()
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
