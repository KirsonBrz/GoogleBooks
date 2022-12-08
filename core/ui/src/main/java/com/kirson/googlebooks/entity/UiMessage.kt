package com.kirson.googlebooks.entity

import com.kirson.googlebooks.util.TextRef

data class UiMessage(
  val title: TextRef,
  val description: TextRef? = null,
  val imageResource: Int? = null,
  val primaryAction: Action? = null,
) {
  data class Action(
    val name: TextRef,
    val listener: (() -> Unit),
  )
}
