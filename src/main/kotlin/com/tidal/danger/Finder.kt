package com.tidal.danger

import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class Finder(pattern: String) : SimpleFileVisitor<Path>() {
    private val pathMatcher = FileSystems.getDefault().getPathMatcher("glob:$pattern")
    private val matches = arrayListOf<Path>()

    override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
        find(file)
        return FileVisitResult.CONTINUE
    }

    private fun find(path: Path?) {
        if (path?.let { pathMatcher.matches(it) } == true) {
            matches.add(path)
        }
    }

    override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult {
        println("[ReporterPlugin.ERROR: visitFileFailed file: $file]")
        return FileVisitResult.CONTINUE
    }

    fun getFiles(): List<Path> {
        println("[ReporterPlugin.INFO: done matches(${matches.size}): $matches]")
        return matches
    }
}
