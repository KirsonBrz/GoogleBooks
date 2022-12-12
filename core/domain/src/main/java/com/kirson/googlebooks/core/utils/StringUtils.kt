package com.kirson.googlebooks.core.utils


fun String.toHttpsPrefix(): String =
    if (isNotEmpty() && !startsWith("https://") && !startsWith("http://")) {
        "https://$this"
    } else if (startsWith("http://")) {
        replace("http://", "https://")
    } else this

fun String.swapToCategoryPrefix(): String =
    if (startsWith("+subject:")) {
        replace("+subject:", "Category: ")
    } else if (startsWith("Category: ")) {
        replace("Category: ", "+subject:")
    } else this

