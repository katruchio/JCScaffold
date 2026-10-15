package com.example.jcscaffold

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.navigation.Route

sealed class AppRoute(@StringRes val titleRes : Int
) : Route {
    object AddItem : AppRoute(R.string.add_item)

    sealed class Tab(
        @StringRes titleRes: Int,
        val icon: ImageVector
    ) : AppRoute(titleRes) {
        object Items : Tab(R.string.items, Icons.Default.List)
        object Settings : Tab(R.string.settings, Icons.Default.Settings)
        object Profile : Tab(R.string.profile , Icons.Default.AccountBox)
    }
}