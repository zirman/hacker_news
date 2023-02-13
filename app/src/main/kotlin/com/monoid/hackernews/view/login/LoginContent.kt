package com.monoid.hackernews.view.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import com.monoid.hackernews.common.api.loginRequest
import com.monoid.hackernews.common.datastore.Authentication
import com.monoid.hackernews.common.datastore.authentication
import com.monoid.hackernews.common.datastore.copy
import com.monoid.hackernews.common.view.R
import com.monoid.hackernews.view.text.PasswordTextField
import com.monoid.hackernews.view.text.UsernameTextField
import io.ktor.client.HttpClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginContent(
    httpClient: HttpClient,
    authentication: DataStore<Authentication>,
    windowSizeClass: WindowSizeClass,
    onLogin: (Authentication) -> Unit,
    onLoginError: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.tertiary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    WindowInsets.safeContent
                        .only(run {
                            var windowInsets = WindowInsetsSides.Bottom
                            if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact) {
                                windowInsets += WindowInsetsSides.Top
                            }
                            if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                                windowInsets += WindowInsetsSides.Horizontal
                            }
                            windowInsets
                        })
                        .asPaddingValues()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val rowModifier: Modifier =
                if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                } else {
                    Modifier.width(320.dp)
                }.padding(vertical = 4.dp)

            val coroutineScope: CoroutineScope =
                rememberCoroutineScope()

            val (username: String, setUsername) =
                rememberSaveable { mutableStateOf("") }

            val (password: String, setPassword) =
                rememberSaveable { mutableStateOf("") }

            fun onSubmit() {
                if (username.isBlank() || password.isEmpty()) return

                coroutineScope.launch {
                    try {
                        onLogin(
                            authentication.updateData { auth ->
                                httpClient.loginRequest(authentication = authentication {
                                    this.username = username
                                    this.password = password
                                })

                                auth.copy {
                                    this.username = username
                                    this.password = password
                                }
                            }
                        )
                    } catch (error: Throwable) {
                        if (error is CancellationException) throw error
                        onLoginError(error)
                    }
                }
            }

            val focusManager: FocusManager =
                LocalFocusManager.current

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(id = R.string.hacker_news_login),
                modifier = rowModifier,
                style = MaterialTheme.typography.headlineMedium
            )

            UsernameTextField(
                username = username,
                onUsernameChange = setUsername,
                modifier = rowModifier,
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                onPrev = { focusManager.moveFocus(FocusDirection.Up) }
            )

            PasswordTextField(
                password = password,
                onChangePassword = setPassword,
                modifier = rowModifier,
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                onPrev = { focusManager.moveFocus(FocusDirection.Up) },
                onDone = ::onSubmit
            )

            Button(
                onClick = ::onSubmit,
                modifier = rowModifier,
                enabled = username.isNotBlank() && password.isNotEmpty()
            ) {
                Text(text = stringResource(id = R.string.submit))
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}
