package com.kirson.googlebooks.components.sorting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kirson.googlebooks.components.Checkbox
import com.kirson.googlebooks.components.HorizontalDivider
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme

@Composable
fun OptionSelector(
    modifier: Modifier = Modifier,
    title: String,
    items: List<OptionSelectorItem>,
    selectedItemId: OptionSelectorItem.Id?,
    onSelect: (OptionSelectorItem.Id) -> Unit,
) {
    Column(modifier = modifier.background(GoogleBooksTheme.colors.backgroundSecondary)) {
        SheetHandle()
        Row(modifier = Modifier.heightIn(min = 48.dp)) {
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillLineHeight(32.sp)
                    .weight(1f),
                style = GoogleBooksTheme.typography.title1,
                text = title,
            )
        }
        items.forEachIndexed { index, option ->
            OptionSelectorRow(
                modifier = Modifier.clickable { onSelect(option.id) },
                text = option.name,
                checked = option.id == selectedItemId
            )
            if (index != items.lastIndex) {
                HorizontalDivider(startIndent = 0.dp, endIndent = 0.dp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
internal fun OptionSelectorRow(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
) {
    Row(
        modifier = modifier.heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 8.dp),
            style = GoogleBooksTheme.typography.caption3,
            text = text,
        )
        if (checked) {
            Checkbox(
                modifier = Modifier.padding(end = 16.dp),
                checked = true,
                onCheckedChange = null
            )
        }
    }
}

@Immutable
data class OptionSelectorItem(
    val id: Id,
    val name: String,
) {
    @JvmInline
    value class Id(val value: Any)
}

@Preview(showBackground = true, widthDp = 360)
@Composable
internal fun OptionSelectorPreview() {
    GoogleBooksTheme {
        OptionSelector(
            title = "title",
            items = listOf(
                OptionSelectorItem(id = OptionSelectorItem.Id(1), name = "Hello1"),
                OptionSelectorItem(id = OptionSelectorItem.Id(2), name = "Hello2"),
                OptionSelectorItem(id = OptionSelectorItem.Id(3), name = "Hello3"),
                OptionSelectorItem(id = OptionSelectorItem.Id(4), name = "Hello4"),
                OptionSelectorItem(id = OptionSelectorItem.Id(5), name = "Hello5"),
                OptionSelectorItem(id = OptionSelectorItem.Id(6), name = "Hello6"),
            ),
            selectedItemId = OptionSelectorItem.Id(4),
            onSelect = {},
        )
    }
}
