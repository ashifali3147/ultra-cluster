package com.tlw.ultracluster.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tlw.ultracluster.ui.RideDashboardScreen
import com.tlw.ultracluster.ui.theme.ClusterTheme
import com.tlw.ultracluster.ui.viewmodel.DashboardViewModel

@Composable
fun RideDashboardRoute(viewModel: DashboardViewModel = viewModel(factory = DashboardViewModel.Factory)) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ClusterTheme(isNight = state.isNight) {
        RideDashboardScreen(
            state = state,
            onEvent = viewModel::onEvent,
        )
    }
}