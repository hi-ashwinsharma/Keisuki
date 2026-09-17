package com.hiashwinsharma.keisuki.feature.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hiashwinsharma.keisuki.core.designsystem.LocalAppCornerRadius
import com.hiashwinsharma.keisuki.core.designsystem.calculateExpressiveShapes
import com.hiashwinsharma.keisuki.data.auth.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthBottomSheet(
    authState: AuthState,
    isSyncPending: Boolean,
    onDismiss: () -> Unit,
    onSignInWithGoogle: () -> Unit,
    onSignOut: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    val appRadius = LocalAppCornerRadius.current
    val shapes = calculateExpressiveShapes(appRadius)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = appRadius, topEnd = appRadius),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (authState.isAuthenticated) {
                AuthenticatedProfileContent(
                    authState = authState,
                    isSyncPending = isSyncPending,
                    shapes = shapes,
                    onSignOut = {
                        onSignOut()
                        onDismiss()
                    }
                )
            } else {
                GuestSignInContent(
                    isLoading = authState.isLoading,
                    shapes = shapes,
                    onSignIn = {
                        onSignInWithGoogle()
                        onDismiss()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
