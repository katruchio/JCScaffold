package com.example.navigation

import androidx.compose.runtime.Stable

@Stable
interface NavigationState {
    val isRoot : Boolean
    val currentRoute : Route
}