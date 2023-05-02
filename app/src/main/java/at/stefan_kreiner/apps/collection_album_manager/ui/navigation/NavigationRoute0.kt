package at.stefan_kreiner.apps.collection_album_manager.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import com.example.collection_album_manager.ui.navigation.NavigationRoute



class NavigationRoute0(
    path: UniversalResourceIdentifierPath,
) : NavigationRoute(
    path = path,
    arguments = listOf()
) {
    fun toDestination(): String = super.toDestination(mapOf())
}

fun NavigationRoute0.navigateWith(
    navController: NavController,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    this.navigateWith(
        navController = navController, navOptions = navOptions(builder)
    )
}

fun NavigationRoute0.navigateWith(
    navController: NavController,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navController.navigate(
        route = this.toDestination(),
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )
}