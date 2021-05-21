package com.mariusgreve.danger

data class Report(val files: List<IssueFile>)

data class IssueFile(
    val filePath: String,
    val issues: List<Issue>
)

data class Issue(
    val line: Int,
    val column: Int,
    val severity: SeverityLevel,
    val message: String,
    val sourceRule: String
)

sealed class SeverityLevel {
    object Warning : SeverityLevel()
    object Error : SeverityLevel()
    object Info : SeverityLevel()
    data class Unknown(val raw: String) : SeverityLevel()
}
