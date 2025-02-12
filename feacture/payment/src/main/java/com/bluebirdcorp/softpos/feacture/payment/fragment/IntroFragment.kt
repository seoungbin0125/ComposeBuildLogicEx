package com.bluebirdcorp.softpos.feacture.payment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.feacture.payment.R
import com.bluebirdcorp.softpos.feacture.payment.view_model.SharedPaymentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class   IntroFragment : Fragment() {

    private val sharedPaymentViewModel: SharedPaymentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        debug("onCreateView")
        return ComposeView(requireContext()).apply {
            setContent {
                IntroScreen(
                    onStartClick = {
//                        sharedPaymentViewModel.requestTapxPhonePayment(
//                            requireContext(), "", 20.22
//                        )
//                         Fragment Navigation으로 이동
                        findNavController().navigate(R.id.action_barcode_scan)
                    },
                    onSettingsClick = {
//                        sharedPaymentViewModel.requestWorldLinePayment(
//                            requireContext(), "", 20.22
//                        )
                        findNavController().navigate(R.id.action_barcode_item_setting)
                    }
                )
            }
        }
    }
}

@Composable
fun IntroScreen(onStartClick: () -> Unit, onSettingsClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // 배경 이미지
            Image(
                painter = painterResource(id = R.drawable.intro_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(15.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_db_setting),
                        contentDescription = "",
                        modifier = Modifier
                            .size(60.dp)
                            .clickable(onClick = onSettingsClick)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.intro_start_button),
                        contentDescription = "시작 버튼",
                        modifier = Modifier
                            .wrapContentSize()
                            .clickable(onClick = onStartClick)
                    )
                }
            }
        }

        val barImagePainter = painterResource(id = R.drawable.intro_bottom_bar)
        val aspectRatio = barImagePainter.intrinsicSize.width / barImagePainter.intrinsicSize.height

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        ) {
            Image(
                painter = barImagePainter,
                contentDescription = "Bottom Bar Image",
                contentScale = ContentScale.FillBounds, // 전체 영역을 채우도록 설정
                modifier = Modifier.matchParentSize()
            )
        }
    }
}
