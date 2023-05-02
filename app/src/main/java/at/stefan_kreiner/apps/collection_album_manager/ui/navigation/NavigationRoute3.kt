package at.stefan_kreiner.apps.collection_album_manager.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import com.example.collection_album_manager.ui.navigation.NavigationRoute


interface NavigationParameters3<T1, T2, T3> {
    fun component1(): T1
    fun component2(): T2
    fun component3(): T3
}


abstract class NavigationRoute3<T1, T2, T3, Parameters : NavigationParameters3<T1, T2, T3>>(
    private val navArgument1: TypedNamedNavArgument<T1>,
    private val navArgument2: TypedNamedNavArgument<T2>,
    private val navArgument3: TypedNamedNavArgument<T3>,
    path: UniversalResourceIdentifierPath
) : NavigationRoute(
    arguments = listOf(
        navArgument1,
        navArgument2,
        navArgument3,
    ).map {
        it.instance
    },
    path = path,
) {
    open fun toDestination(parameters: Parameters): String = super.toDestination(
        mapOf(
            navArgument1.instance to parameters.component1(),
            navArgument2.instance to parameters.component2(),
            navArgument3.instance to parameters.component3(),
        )
    )
}

fun <T1, T2, T3, Parameters : NavigationParameters3<T1, T2, T3>> NavigationRoute3<T1, T2, T3, Parameters>.navigateWith(
    navController: NavController,
    parameters: Parameters,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    this.navigateWith(
        navController = navController, parameters = parameters, navOptions = navOptions(builder)
    )
}

fun <T1, T2, T3, Parameters : NavigationParameters3<T1, T2, T3>> NavigationRoute3<T1, T2, T3, Parameters>.navigateWith(
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

//fun <T1, T2, T3, Parameters : NavigationParameters3<T1, T2, T3>> NavController.navigate(
//    route: NavigationRoute3<T1, T2, T3, Parameters>,
//    parameters: Parameters,
//    builder: NavOptionsBuilder.() -> Unit = {},
//) {
//    navigate(
//        route = route, parameters = parameters, navOptions = navOptions(builder)
//    )
//}
//
//fun <T1, T2, T3, Parameters : NavigationParameters3<T1, T2, T3>> NavController.navigate(
//    route: NavigationRoute3<T1, T2, T3, Parameters>,
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