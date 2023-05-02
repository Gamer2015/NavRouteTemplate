package at.stefan_kreiner.apps.collection_album_manager.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.navOptions
import com.example.collection_album_manager.ui.navigation.NavigationRoute


interface NavigationParameters4<T1, T2, T3, T4> {
    fun component1(): T1
    fun component2(): T2
    fun component3(): T3
    fun component4(): T4
}


abstract class NavigationRoute4<T1, T2, T3, T4, Parameters : NavigationParameters4<T1, T2, T3, T4>>(
    private val navArgument1: TypedNamedNavArgument<T1>,
    private val navArgument2: TypedNamedNavArgument<T2>,
    private val navArgument3: TypedNamedNavArgument<T3>,
    private val navArgument4: TypedNamedNavArgument<T4>,
    path: UniversalResourceIdentifierPath
) : NavigationRoute(
    arguments = listOf(
        navArgument1,
        navArgument2,
        navArgument3,
        navArgument4,
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
            navArgument4.instance to parameters.component4(),
        )
    )
}

fun <T1, T2, T3, T4, Parameters : NavigationParameters4<T1, T2, T3, T4>> NavigationRoute4<T1, T2, T3, T4, Parameters>.navigateWith(
    navController: NavController,
    parameters: Parameters,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    this.navigateWith(
        navController = navController, parameters = parameters, navOptions = navOptions(builder)
    )
}

fun <T1, T2, T3, T4, Parameters : NavigationParameters4<T1, T2, T3, T4>> NavigationRoute4<T1, T2, T3, T4, Parameters>.navigateWith(
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
