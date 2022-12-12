package com.kirson.googlebooks.components

import com.kirson.googlebooks.home.ui.R

sealed class CategoryItem(val name: String, val imageId: Int) {

    object Adventure : CategoryItem("Adventure", R.drawable.adventure)
    object Biography : CategoryItem("Biography", R.drawable.biography)
    object Business : CategoryItem("Business", R.drawable.business)
    object Classic : CategoryItem("Classic", R.drawable.classic)
    object Detective : CategoryItem("Detective", R.drawable.detective)
    object Drama : CategoryItem("Drama", R.drawable.drama)
    object Fairytale : CategoryItem("Fairytale", R.drawable.fairytale)
    object Fantasy : CategoryItem("Fantasy", R.drawable.fantasy)
    object Folklore : CategoryItem("Folklore", R.drawable.folklore)
    object Historical : CategoryItem("Historical", R.drawable.historical)
    object Horror : CategoryItem("Horror", R.drawable.horror)
    object Humor : CategoryItem("Humor", R.drawable.humor)
    object Legend : CategoryItem("Legend", R.drawable.legend)
    object Mystery : CategoryItem("Mystery", R.drawable.mystery)
    object Mythology : CategoryItem("Mythology", R.drawable.mythology)
    object NonFiction : CategoryItem("NonFiction", R.drawable.nonfiction)
    object Play : CategoryItem("Play", R.drawable.play)
    object Poetry : CategoryItem("Poetry", R.drawable.poetry)
    object Romance : CategoryItem("Romance", R.drawable.romance)
    object ScienceFiction : CategoryItem("Science Fiction", R.drawable.sciencefiction)


}
