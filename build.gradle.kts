plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("tools.aqua:z3-turnkey:4.12.2.1")
}

sourceSets {
    main {
        kotlin { // or 'java' if you were using Java files
            // This line tells Gradle to look for code in your custom 'code' folder
            srcDirs("src")
            // You can also add to the default location like this:
            // srcDir("src/custom")
        }
        // If you had resources in a custom folder, you'd specify them here:
        // resources {
        //     srcDir("custom_resources")
        // }
    }
}