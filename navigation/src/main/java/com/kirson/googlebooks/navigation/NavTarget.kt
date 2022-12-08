package com.kirson.googlebooks.navigation

sealed class NavTarget(val route: String, val icon: Int) {
    object Explorer :
        NavTarget(ModuleRoutes.ExplorerScreen.route, ModuleRoutes.ExplorerScreen.icon)

    object Details :
        NavTarget(ModuleRoutes.DetailsScreen.route, ModuleRoutes.DetailsScreen.icon)

    object RootModule :
        NavTarget(ModuleRoutes.RootModule.route, ModuleRoutes.RootModule.icon)
}

enum class ModuleRoutes(val route: String, val icon: Int) {
    ExplorerScreen(
        "Explorer",
        R.drawable.main_24
    ),
   DetailsScreen(
        "Product Details",
        R.drawable.main_24
    ),

    RootModule(
        "rootmodule",
        R.drawable.main_24
    ),
}