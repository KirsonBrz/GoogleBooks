package com.kirson.googlebooks.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kirson.googlebooks.core.utils.lerp
import com.kirson.googlebooks.core.utils.toHttpsPrefix
import com.kirson.googlebooks.entity.BookDomainModel
import com.kirson.googlebooks.ui.theme.GoogleBooksTheme
import kotlin.math.max
import kotlin.math.min


private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun BookDetails(
    book: BookDomainModel,
    upPress: () -> Unit,

    ) {
    remember { book }
    Box(Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        Header()
        Body(book = book, scroll = scroll)
        ImageCollapse(book.largeImage) { scroll.value }
        Up(upPress = upPress)
    }
}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(GoogleBooksTheme.colors.tornado1))
    )
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(20.dp)
            .size(24.dp)
            .background(
                color = GoogleBooksTheme.colors.backgroundPrimary,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowLeft,
            tint = GoogleBooksTheme.colors.contendAccentTertiary,
            contentDescription = "back"
        )
    }
}

@Composable
private fun Body(
    book: BookDomainModel,
    scroll: ScrollState
) {
    Column(modifier = Modifier.verticalScroll(scroll)) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(350.dp)
        )
        Surface(
            Modifier.fillMaxWidth(),
            color = GoogleBooksTheme.colors.backgroundPrimary
        ) {
            Column {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.body1,
                    fontSize = 24.sp,
                    color = GoogleBooksTheme.colors.contendAccentTertiary,
                    modifier = HzPadding
                )
                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Author",
                    style = MaterialTheme.typography.overline,
                    fontSize = 14.sp,
                    color = GoogleBooksTheme.colors.contendAccentTertiary,
                    modifier = HzPadding
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = book.authors?.get(0) ?: "No author",
                    style = MaterialTheme.typography.body1,
                    fontSize = 20.sp,
                    color = GoogleBooksTheme.colors.contendAccentTertiary,
                    modifier = HzPadding
                )


                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Published at",
                    style = MaterialTheme.typography.overline,
                    fontSize = 14.sp,
                    color = GoogleBooksTheme.colors.contendAccentTertiary,
                    modifier = HzPadding
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = book.publishedDate ?: "No date",
                    style = MaterialTheme.typography.body2,
                    color = GoogleBooksTheme.colors.contendAccentTertiary,
                    modifier = HzPadding
                )

                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Description",
                    style = MaterialTheme.typography.overline,
                    fontSize = 14.sp,
                    color = GoogleBooksTheme.colors.contendAccentTertiary,
                    modifier = HzPadding
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = book.description ?: "No description",
                    style = MaterialTheme.typography.body1,
                    fontSize = 20.sp,
                    color = GoogleBooksTheme.colors.contendAccentTertiary,
                    modifier = HzPadding
                )
                Spacer(Modifier.height(60.dp))

            }
        }
    }
}


@Composable
private fun ImageCollapse(
    largeImage: String? = "http://books.google.com/books/content?id=g-gCAAAAMBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
    scrollProvider: () -> Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(largeImage?.toHttpsPrefix())
                .crossfade(true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(8.dp)
                .background(color = Color.Transparent, shape = CircleShape)
                .size(width = 200.dp, height = 200.dp)


        )

    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFractionProvider: () -> Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

