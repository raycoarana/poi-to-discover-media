package com.raycoarana.poitodiscover.core

/**
 * Append a line with a CR and LF chars (DOS/Windows like line break)
 */
fun Appendable.appendCrLf(): Appendable = this.append("\r\n")

/**
 * Append a line with a CR and LF chars (DOS/Windows like line break)
 */
fun Appendable.appendCrLf(value: CharSequence?): Appendable = this.append(value).append("\r\n")

fun Appendable.appendSection(value: String): Appendable = this.appendCrLf("[$value]")
fun Appendable.appendField(key: String, value: Any): Appendable = this.appendCrLf("$key = \"$value\"")

/**
 * Append a line with a LF char (Unix like line break)
 */
fun Appendable.appendLf(): Appendable = this.append("\n")

/**
 * Append a line with a CR char (Unix like line break)
 */
fun Appendable.appendLf(value: CharSequence?): Appendable = this.append(value).append("\n")

fun Appendable.appendXmlHeader(): Appendable = this.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n")