@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.monoid.hackernews.view.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.monoid.hackernews.common.view.R

@Suppress("ComposeUnstableReceiver")
@Composable
fun ThreePaneScaffoldScope.SettingsDetailPane(
    settingsDetailUiState: SettingsDetailUiState?,
    modifier: Modifier = Modifier,
) {
    AnimatedPane(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (settingsDetailUiState) {
                SettingsDetailUiState.Profile -> {
                    ProfileDetail()
                }

                SettingsDetailUiState.Styling -> {
                    PreferencesDetail()
                }

                null -> {
                    Text(
                        text = stringResource(id = R.string.no_setting_selected),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }
    }
}