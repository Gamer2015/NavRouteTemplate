package at.stefan_kreiner.apps.collection_album_manager.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import com.example.collection_album_manager.ui.navigation.NavigationRoute


interface NavigationParameters1<T1> {
    fun argument1(): T1
}


class NavigationRoute1<T1, Parameters : NavigationParameters1<T1>>(
    private val navArgument1: TypedNamedNavArgument<T1>,
    path: UniversalResourceIdentifierPath,
) : NavigationRoute(
    arguments = listOf(
        navArgument1,
    ).map {
        it.instance
    },
    path = path,
) {
    fun toDestination(parameters: Parameters): String = super.toDestination(
        mapOf(
            navArgument1.instance to parameters.argument1(),
        )
    )
}

fun <T1, Parameters : NavigationParameters1<T1>> NavigationRoute1<T1, Parameters>.navigateWith(
    navController: NavController,
    parameters: Parameters,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    this.navigateWith(
        navController = navController, parameters = parameters, navOptions = navOptions(builder)
    )
}

fun <T1, Parameters : NavigationParameters1<T1>> NavigationRoute1<T1, Parameters>.navigateWith(
    navController: NavController,
    parameters: Parameters,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    navController.navigate(
        route = this.toDestination(
            parameters = parameters
        ),
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
    )
}

//fun <T1, Parameters : NavigationParameters1<T1>> NavController.navigate(
//    route: NavigationRoute1<T1, Parameters>,
//    parameters: Parameters,
//    builder: NavOptionsBuilder.() -> Unit = {},
//) {
//    navigate(
//        route = route, parameters = parameters, navOptions = navOptions(builder)
//    )
//}
//
//fun <T1, Parameters : NavigationParameters1<T1>> NavController.navigate(
//    route: NavigationRoute1<T1, Parameters>,
//    parameters: Parameters,
//    navOptions: NavOptions? = null,
//    navigatorExtras: Navigator.Extras? = null,
//) {
//    navigate(
//        route = route.toDestination(
//            parameters = parameters
//        ),
//        navOptions = navOptions,
//        navigatorExtras = navigatorExtras,
//    )
//}