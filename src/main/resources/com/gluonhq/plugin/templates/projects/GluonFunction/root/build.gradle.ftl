apply plugin: 'java'

sourceCompatibility = 1.8

task gfBundle (type: Zip, group: 'gluon') {
    baseName = 'gfBundle'
    version = null

    from configurations.runtime
    from jar.outputs
}