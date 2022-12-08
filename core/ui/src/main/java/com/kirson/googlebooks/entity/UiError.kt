package com.kirson.googlebooks.entity

data class UiError(
  val message: UiMessage,
  val cause: Throwable? = null,
)
