plugins {
    id("kotlin-module")
}

dependencies {
    // simplify debugging while testing
    testRuntimeOnly("org.slf4j:slf4j-simple:[1.7,2.0)!!1.7.9")
}
