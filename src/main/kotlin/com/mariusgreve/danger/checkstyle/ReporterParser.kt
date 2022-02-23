package com.mariusgreve.danger.checkstyle

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

object ReporterParser {

    fun parse(filePath: String): Report {
        val elements = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(File(filePath))
            .documentElement
            .getElementsByTagName("file")

        val files = elements.mapElements {
            IssueFile(
                filePath = it.getAttribute("name")
                    .removePrefix(File("").absolutePath)
                    .removePrefix(File.separator),
                issues = it.parseIssues()
            )
        }.distinct()

        return Report(files)
    }

    private fun <T> NodeList.mapElements(mapper: (Element) -> T): List<T> {
        val elements = mutableListOf<T>()
        for (i in 0 until this.length) {
            with(this.item(i) as Element) {
                if (this.nodeType == Node.ELEMENT_NODE) {
                    elements.add(mapper(this))
                }
            }
        }
        return elements
    }

    private fun Element.parseIssues(): List<Issue> {
        return getElementsByTagName("error").mapElements {
            Issue(
                line = it.getAttribute("line").toIntOrNull() ?: 1,
                column = it.getAttribute("column").toIntOrNull() ?: 1,
                severity = when (val severity = it.getAttribute("severity")) {
                    "info" -> SeverityLevel.Info
                    "error" -> SeverityLevel.Error
                    "warn", "warning" -> SeverityLevel.Warning
                    else -> SeverityLevel.Unknown(severity)
                },
                message = it.getAttribute("message"),
                sourceRule = it.getAttribute("source")
            )
        }
    }
}
