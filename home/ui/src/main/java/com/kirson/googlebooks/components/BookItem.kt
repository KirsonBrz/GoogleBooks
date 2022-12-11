package com.kirson.googlebooks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kirson.googlebooks.core.utils.toHttpsPrefix
import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(
    book: BookDomainModel, onBookDetails: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(166.dp)
            .padding(vertical = 3.dp),
        onClick = {
            onBookDetails(book.title)
        },
        colors = CardDefaults.cardColors(containerColor = GoogleBooksTheme.colors.contendPrimary),
        elevation = CardDefaults.elevatedCardElevation(),
        shape = CardDefaults.elevatedShape


    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),

            ) {

            if (book.largeImage != null) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(book.largeImage!!.toHttpsPrefix()).crossfade(true).build(),
                    contentDescription = "",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 100.dp, height = 150.dp)


                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = GoogleBooksTheme.colors.contendAccentTertiary,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 100.dp, height = 150.dp)
                        .background(color = Color.Transparent, shape = CircleShape)
                )

            }
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = book.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = GoogleBooksTheme.colors.contendAccentTertiary
            )

        }
    }
}
