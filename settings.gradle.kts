rootProject.name = "SbRemake"

include("minestom")

includeBuild("minestom/build-src")

include("minestom:code-generators")
//include("minestom:testing")

include("minestom:jmh-benchmarks")
//include("minestom:jcstress-tests")
