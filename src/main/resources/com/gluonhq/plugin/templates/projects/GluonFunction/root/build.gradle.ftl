apply plugin: 'java'

sourceCompatibility = 1.8

jar {
   manifest {
      attributes 'Main-Class': '${packageName}.${functionName}'
   }
}