package com.tidal.danger

import systems.danger.kotlin.sdk.DangerPlugin
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object ReporterPlugin : DangerPlugin() {
    override val id: String
        get() = this.javaClass.name

    fun report(configBlock: Config.() -> Unit) {
        val config = Config().apply(configBlock)
        val filesInFolder = find(config.pattern ?: "*.xml")
        val reports = filesInFolder?.map { ReporterParser.parse(it.toString()) } ?: return

        reports.forEach { report ->
            report.files.forEach { file ->
                file.issues.forEach { issue ->
                    issue.report(file)
                }
            }
        }
    }

    private fun find(pattern: String): List<Path>? {
        val startDir = Paths.get("")
        val finder = Finder(pattern)

        Files.walkFileTree(startDir, finder)

        val files = finder.getFiles()
        return if (files.isEmpty()) null else files
    }

    private fun Issue.report(file: IssueFile) {
        val message = "$message [rule: $sourceRule] [file: ${file.filePath}] [line: $line]"
        when (severity) {
            SeverityLevel.Warning -> context.warn(message, file.filePath, line)
            SeverityLevel.Error -> context.fail(message, file.filePath, line)
            SeverityLevel.Info -> context.message("INFO: $message", file.filePath, line)
            is SeverityLevel.Unknown -> context.message("${severity.raw}: $message", file.filePath, line)
        }
    }

    class Config {
        var pattern: String? = null
    }
}
