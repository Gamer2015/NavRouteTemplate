package at.stefan_kreiner.apps.collection_album_manager.ui.composables

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Returns whether the lazy list is currently scrolling up.
 */
@Composable
fun LazyGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
//
///**
// * Returns whether the lazy list is currently scrolling down.
// */
//@Composable
//fun LazyGridState.scrollUpOffset(): Boolean {
//    var maxScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
//    return remember(this) {
//        derivedStateOf {
//            if (previousIndex != firstVisibleItemIndex) {
//                previousIndex > firstVisibleItemIndex
//            } else {
//                previousScrollOffset >= firstVisibleItemScrollOffset
//            }.also {
//                previousIndex = firstVisibleItemIndex
//                previousScrollOffset = firstVisibleItemScrollOffset
//            }
//        }
//    }.value
//}