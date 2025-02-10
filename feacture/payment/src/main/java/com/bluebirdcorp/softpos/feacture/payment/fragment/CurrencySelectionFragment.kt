package com.bluebirdcorp.softpos.feacture.payment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.feacture.payment.R
import com.bluebirdcorp.softpos.feacture.payment.view_model.SharedPaymentViewModel
import com.bluebirdcorp.softpos.feacture.ui.HomeNavBar
import com.bluebirdcorp.softpos.feacture.ui.HorizontalGradientBrush
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencySelectionFragment : Fragment() {

    private val sharedPaymentViewModel: SharedPaymentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        debug("onCreateView")
        return ComposeView(requireContext()).apply {
            setContent {
                CurrencySelectionScreen(
                    onDollarClick = {
                        val bundle = Bundle().apply {
                            putString(BarcodeScanFragment.ARG_CURRENCY, BarcodeScanFragment.CURRENCY_DOLLAR)
                        }
                        findNavController().navigate(
                            R.id.action_barcode_scan,
                            bundle
                        )
                    },
                    onEuroClick = {
                        val bundle = Bundle().apply {
                            putString(BarcodeScanFragment.ARG_CURRENCY, BarcodeScanFragment.CURRENCY_EURO)
                        }
                        findNavController().navigate(
                            R.id.action_barcode_scan,
                            bundle
                        )
                    },
                )
            }
        }
    }

    @Composable
    fun CurrencySelectionScreen(
        onDollarClick: () -> Unit,
        onEuroClick: () -> Unit,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = HorizontalGradientBrush()
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0x99035ADE), // 투명도 포함된 색상 (#035ADE 60%)
                                Color.Transparent
                            ),
                            startX = 0f,
                            endX = Float.POSITIVE_INFINITY
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 110.dp), // 전체적인 상단 여백 조절
                    verticalArrangement = Arrangement.Top, // Top 정렬로 요소들을 위로 올림
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.currency_select_text),
                        fontSize = 32.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp) // 텍스트 위치 조정
                    )

                    Spacer(modifier = Modifier.height(60.dp)) // 텍스트 아래 여백 조정

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(top = 16.dp) // 버튼 그룹 위치 조정
                    ) {
                        CurrencyButton(R.drawable.icon_dollar, onDollarClick)
                        CurrencyButton(R.drawable.icon_euro, onEuroClick)
                    }
                }
                HomeNavBar(findNavController(),
                    Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}

@Composable
fun CurrencyButton(imageRes: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(140.dp, 120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit, // 비율 유지
            modifier = Modifier
                .size(70.dp) // 이미지 크기 통일
                .padding(8.dp) // 이미지와 박스 사이 패딩 추가
        )
    }
}