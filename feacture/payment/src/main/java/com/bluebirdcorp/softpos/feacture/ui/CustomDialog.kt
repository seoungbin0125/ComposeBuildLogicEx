package com.bluebirdcorp.softpos.feacture.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CommonDialogShape(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .imePadding()
                .wrapContentSize(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                DialogCloseButton(
                    onDismiss, Modifier.align(Alignment.End)
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier
                        .wrapContentSize() // 내용 크기에 맞게 조절
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

