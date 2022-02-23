package com.mariusgreve.danger

import systems.danger.kotlin.sdk.DangerPlugin
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

abstract class ReporterPlugin : DangerPlugin() {

    fun report(configBlock: Config.() -> Unit) {
        val config = Config().apply(configBlock)
        find(config.pattern ?: "*.xml")?.let { report(it) }
    }

    private fun find(pattern: String): List<Path>? {
        val startDir = Paths.get("")
        val finder = Finder(pattern)

        Files.walkFileTree(startDir, finder)

        val files = finder.getFiles()
        return files.ifEmpty { null }
    }

    protected abstract fun report(filesInFolder: List<Path>)

    class Config {
        var pattern: String? = null
    }
}
