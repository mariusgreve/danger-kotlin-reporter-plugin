package com.mariusgreve.danger.checkstyle

import com.mariusgreve.danger.ReporterPlugin
import java.nio.file.Path

object CheckstyleReporterPlugin : ReporterPlugin() {
    override val id: String
        get() = this.javaClass.name

    override fun report(filesInFolder: List<Path>) {
        val reports = filesInFolder.map { ReporterParser.parse(it.toString()) }

        reports.forEach { report ->
            report.files.forEach { file ->
                file.issues.forEach { issue ->
                    issue.report(file)
                }
            }
        }
    }

    private fun Issue.report(file: IssueFile) {
        val message = "$message [rule: $sourceRule] [file: ${file.filePath}] [line: $line]"
        when (severity) {
            SeverityLevel.Warning -> context.warn(message, file.filePath, line)
            SeverityLevel.Error -> context.fail(message, file.filePath, line)
            SeverityLevel.Info -> context.message("INFO: $message", file.filePath, line)
            is SeverityLevel.Unknown -> context.message(
                "${severity.raw}: $message",
                file.filePath,
                line
            )
        }
    }
}
