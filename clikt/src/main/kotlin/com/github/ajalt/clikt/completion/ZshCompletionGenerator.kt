package com.github.ajalt.clikt.completion

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.Option

/**
 * [CompletionGenerator] for Zsh.
 *
 * @author Laurent Pireyn
 */
object ZshCompletionGenerator : CompletionGenerator {
    private fun escaped(string: String): String =
        string
            .replace("'", "\\'")
            .replace(":", "\\:")

    override fun generateCompletion(command: CliktCommand): String {
        val commandName = command.commandName
        val options: List<Option> = command._options
        return buildString {
            appendln("""
            |#compdef $commandName
            |
            |# Completion for $commandName
            |# Generated by Clikt
            |
            """.trimMargin())
            if (options.isNotEmpty()) {
                append("_arguments -s")
                for (option in options) {
                    appendln(" \\")
                    val names = option.names + option.secondaryNames
                    if (names.size == 1) {
                        append('\'')
                        append(names.first())
                        append('\'')
                    } else {
                        append(
                            names.joinToString(
                                prefix = "{'",
                                separator = "','",
                                postfix = "'}"
                            )
                        )
                    }
                    append("'[")
                    append(escaped(option.help))
                    append(']')
                    for (i in 1..option.nvalues) {
                        append(':')
                        append(escaped(option.metavar ?: "VALUE"))
                        append(':')
                        append("()")
                    }
                    append('\'')
                }
            }
        }
    }
}