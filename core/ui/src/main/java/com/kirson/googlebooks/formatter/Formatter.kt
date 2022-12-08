package com.kirson.googlebooks.formatter

interface Formatter<T> {
  fun format(value: T): String
}
