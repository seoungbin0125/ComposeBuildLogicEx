package com.bluebirdcorp.softpos.feacture.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bluebirdcorp.softpos.feacture.payment.R

class Component {
    @Composable
    fun AdjustableText(text: String) {
        var textSize by remember { mutableStateOf(24.sp) } // 기본 폰트 크기
        val textMeasurer = rememberTextMeasurer()

        BoxWithConstraints {
            val maxWidth = with(LocalDensity.current) { maxWidth.toPx() }

            Text(
                text = text,
                fontSize = textSize,
                color = Color(0xFF000101),
                maxLines = 1, // 한 줄로 제한
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .onSizeChanged { size ->
                        if (size.width > maxWidth) {
                            textSize = textSize * 0.9f // 폰트 크기를 줄이기
                        }
                    }
            )
        }
    }
}

@Composable
fun VerticalGradientBrush(): Brush {
    return Brush.verticalGradient(
        colors = listOf(Color(0xFF009AF1), Color(0xFF00EBEB)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )
}

@Composable
fun HorizontalGradientBrush(): Brush {
    return Brush.horizontalGradient(
        colors = listOf(Color(0xFF009AF1), Color(0xFF00EBEB)),
        startX = 0f,
        endX = Float.POSITIVE_INFINITY
    )
}

@Composable
fun DialogCloseButton(onDismiss: () -> Unit, modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.icon_close),
        contentDescription = null,
        modifier = modifier
            .padding(end = 20.dp, bottom = 20.dp)
            .size(20.dp)
            .clickable {
                onDismiss()
            }
    )
}

@Composable
fun IconItem(
    iconId: Int,
    onClick: () -> Unit,
    defaultSize: Dp = 40.dp
) {
    Image(
        painter = painterResource(id = iconId),
        contentDescription = null,
        modifier = Modifier
            .size(defaultSize)
            .clickable {
                onClick()
            }
    )
}

@Composable
fun HomeNavBar(navController: NavController, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.5.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_navi_home), // 이미지 파일을 리소스에서 로드
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter) // Box 상단 중간에 위치
                .offset(y = (-30).dp)
                .clickable {
                    navController.navigate(R.id.action_intro)
                }
        )
    }
}

@Composable
fun CustomImageButton(
    imageRes: Int,
    text: String,
    onClick: () -> Unit,
    parentModifier: Modifier = Modifier,
    iconModifier: Modifier
) {
    Box(
        modifier = parentModifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
            .height(110.dp)
            .padding(top = 3.dp, bottom = 20.dp)
            .clickable {
                onClick()
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = iconModifier.weight(0.5f),
                contentScale = ContentScale.Fit
            )
            Text(
                text = text, color = Color.Black, fontSize = 20.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .wrapContentHeight(Alignment.CenterVertically)
            )
        }
    }
}