package com.kirson.googlebooks.components

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kirson.googlebooks.components.buttons.Button
import com.kirson.googlebooks.components.buttons.ButtonDefaults
import com.kirson.googlebooks.components.buttons.ButtonSize
import com.kirson.googlebooks.components.buttons.ButtonSlot
import com.kirson.googlebooks.core.ui.uikit.R
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme

@Composable
fun SecondaryButtonSmall(
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
  text: String,
  icon: @Composable (() -> Unit)? = null,
  slotPosition: ButtonSlot = ButtonSlot.None,
  isProgressBarVisible: Boolean = false,
  enabled: Boolean = true,
) {
  Button(
    modifier = modifier,
    onClick = onClick,
    text = text,
    icon = icon,
    slotPosition = slotPosition,
    isProgressBarVisible = isProgressBarVisible,
    enabled = enabled,
    size = ButtonSize.Small,
    colors = ButtonDefaults.secondaryButtonColors(),
  )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
internal fun SortButtonPreview() {
    GoogleBooksTheme {
        SecondaryButtonSmall(
            onClick = {},
            slotPosition = ButtonSlot.AfterText,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_up_round_cap_16),
                    contentDescription = "sort direction",
                    tint = GoogleBooksTheme.colors.black
                )
            },
      text = "text"
    )
  }
}
