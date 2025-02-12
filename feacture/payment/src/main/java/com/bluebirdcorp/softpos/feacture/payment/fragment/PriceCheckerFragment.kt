package com.bluebirdcorp.softpos.feacture.payment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.domain.usecase.BarcodeHandleUsecase
import com.bluebirdcorp.softpos.feacture.payment.R
import com.bluebirdcorp.softpos.feacture.payment.model.BarcodeUiModel
import com.bluebirdcorp.softpos.feacture.payment.view_model.PriceCheckerViewModel
import com.bluebirdcorp.softpos.feacture.payment.view_model.SharedPaymentViewModel
import com.bluebirdcorp.softpos.feacture.ui.CommonDialogShape
import com.bluebirdcorp.softpos.feacture.ui.CustomImageButton
import com.bluebirdcorp.softpos.feacture.ui.HorizontalGradientBrush
import com.bluebirdcorp.softpos.feacture.ui.IconItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PriceCheckerFragment : Fragment() {
    @Inject
    lateinit var barcodeHandleUsecase: BarcodeHandleUsecase

    private val priceCheckerViewModel: PriceCheckerViewModel by viewModels()

    private val sharedPaymentViewModel: SharedPaymentViewModel by activityViewModels()

    lateinit var mSeletedCurrency: String

    companion object {
        const val CURRENCY_DOLLAR = "USD"
        const val CURRENCY_EURO = "EUR"
        const val ARG_CURRENCY = "currency"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mSeletedCurrency = arguments?.getString(ARG_CURRENCY) ?: CURRENCY_EURO
        debug("onCreateView, currency : $mSeletedCurrency")
        barcodeHandleUsecase.openBarcode()
        priceCheckerViewModel.startCollectBarcodeScan()
        return ComposeView(requireContext()).apply {
            setContent {
                BarcodeScanScreen()
            }
        }
    }


    private fun isSeletedDollar(): Boolean = mSeletedCurrency == CURRENCY_DOLLAR

    @Composable
    fun IconRow() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5)) // Fill 색상 (배경)
                .border(1.dp, Color(0xFFEFEFEF)) // Stroke 색상 (테두리)
                .padding(4.dp), // 내부 여백 추가
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconItem(iconId = R.drawable.icon_arrow, {
                findNavController().popBackStack()
            }, 20.dp)
            Spacer(modifier = Modifier.weight(1f))
            IconItem(iconId = R.drawable.icon_home, {
                findNavController().navigate(R.id.action_intro)
            })
            IconItem(iconId = R.drawable.icon_setting, {
                findNavController().navigate(R.id.action_barcode_item_setting)
            })
        }
    }

    @Composable
    fun BarcodeScanScreen() {
        debug("BarcodeScanScreen")
        val barcodeList by priceCheckerViewModel.barcodeList.observeAsState(emptyList())

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                IconRow()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.48f)
                        .background(Color(0xFFF5F5F5))
                ) {
                    if (barcodeList.size == 0) {
                        Text(
                            text = stringResource(id = R.string.hint_barcode_scan),
                            fontSize = 44.sp,
                            color = Color(0xFFAAAAAA),
                            textAlign = TextAlign.Center, // 텍스트 중앙 정렬
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        BarcodeListScreen(
                            barcodeList,
                            onDelete = { item ->
                                priceCheckerViewModel.deleteItem(item)
                            })
                    }
                }


                val subTotalPrice = barcodeList.sumOf {
                    if (isSeletedDollar())
                        it.dollarPrice
                    else
                        it.euroPrice
                }
                val tax = subTotalPrice / 10f

                val totalPrice = subTotalPrice + tax

                var confirmShowDialog by remember { mutableStateOf(false) }
                var paymentShowDialog by remember { mutableStateOf(false) }
                debug("confirmShowDialog : $confirmShowDialog")
                debug("paymentShowDialog : $paymentShowDialog")

                if (confirmShowDialog) {
                    CommonDialogShape(onDismiss = { confirmShowDialog = false }) {
                        ConfirmContent(
                            itemCount = barcodeList.size,
                            onRetry = {
                                confirmShowDialog = false
                            },
                            onConfirm = {
                                // 확인 로직
                                confirmShowDialog = false
                                paymentShowDialog = true
                            },
                            onDismiss = {
                                // 다이얼로그 바깥/닫기 버튼 등으로 닫을 때
                                confirmShowDialog = false
                            }
                        )
                    }
                }
                if (paymentShowDialog) {
                    CommonDialogShape(onDismiss = {
                        paymentShowDialog = false
                    }) {
                        SoftposPaymentContent(
                            subTotal = subTotalPrice,
                            tax = tax,
                            itemsCount = barcodeList.size,
                            onDismiss = { paymentShowDialog = false },
                            onFinalize = {
                                // 결제 완료 처리
                                findNavController().navigate(R.id.action_intro)
                            },
                            onWorldlineTapOnMobile = {
                                // Worldline Tap on Mobile 로직
                                sharedPaymentViewModel.requestWorldLinePayment(
                                    requireContext(), mSeletedCurrency, totalPrice
                                )
                            },
                            onTapXPhone = {
                                sharedPaymentViewModel.requestTapxPhonePayment(
                                    requireContext(), mSeletedCurrency, totalPrice
                                )
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(0.52f),
                ) {
                    Column {
                        PaymentSummaryScreen(
                            barcodeList,
                            subTotalPrice = subTotalPrice,
                            totalPrice = totalPrice,
                            tax = tax,
                            parentModifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(Color.Black)
                        )
                        bottomNextButton(
                            barcodeList,
                            onConfirmShowDialogChange = { confirmShowDialog = it })
                    }
                }
            }
        }
    }

    // 하단 NEXT 버튼 영역
    @Composable
    fun bottomNextButton(
        barcodeList: List<BarcodeUiModel>,
        onConfirmShowDialogChange: (Boolean) -> Unit,
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xCC000000))
                .fillMaxWidth()
                .padding(bottom = 50.dp, top = 14.dp)
        ) {
            Text(
                text = "NEXT",
                modifier = Modifier
                    .clickable(
                        onClick = {
                            if (barcodeList.isEmpty()) {
                                onConfirmShowDialogChange(true)
                                Toast.makeText(
                                    context,
                                    "Please add a product.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                onConfirmShowDialogChange(true)
                            }
                        },
                    )
                    .align(Alignment.Center),
                fontSize = 32.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }

    @Composable
    fun PaymentSummaryScreen(
        barcodeList: List<BarcodeUiModel>,
        subTotalPrice: Double,
        totalPrice: Double,
        tax: Double,
        parentModifier: Modifier,
    ) {
        Box(
            modifier = parentModifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.backgruond_scan_item_summery),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            // 내용(아이템 수, 세금, 총합 등)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // 아이템 수
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val itemsCount =
                        if (barcodeList.isEmpty()) "" else barcodeList.size.toString() + " "

                    Text(
                        text = "${itemsCount}Items",
                        fontSize = 26.sp,
                        color = Color(0xFFECECEC)
                    )
                    Text(
                        text = "€${subTotalPrice}",
                        fontSize = 26.sp,
                        color = Color(0xFFECECEC)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 세금
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "tax",
                        fontSize = 26.sp,
                        color = Color(0xFFECECEC)
                    )
                    Text(
                        text = String.format("€%.2f", tax),
                        fontSize = 26.sp,
                        color = Color(0xFFECECEC)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 구분선
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 4.dp,
                    color = Color(0xFF93D8FF)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 총합
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        fontSize = 28.sp,
                        color = Color(0xFFB4E4FF)
                    )
                    Text(
                        text = "€${totalPrice.toString()}",
                        fontSize = 32.sp,
                        color = Color(0xFFB4E4FF)
                    )
                }
            }
        }
    }


    @Composable
    fun BarcodeListScreen(
        barcodeList: List<BarcodeUiModel>,
        onDelete: (BarcodeUiModel) -> Unit
    ) {
        LazyColumn {
            itemsIndexed(barcodeList) { index, barcode ->
                barcode.index = index
                AddedBarcodeListScreen(barcode, onDelete)
            }
        }
    }

    @Composable
    fun AddedBarcodeListScreen(item: BarcodeUiModel, onDelete: (BarcodeUiModel) -> Unit) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F5F5),
                    )
                    .padding(
                        start = 16.dp, top = 12.dp,
                        bottom = 8.dp, end = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "0${item.index + 1}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF000101),
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.name,
                    fontSize = 24.sp,
                    color = Color(0xFF000101),
                    maxLines = 1, // 한 줄로 제한
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = if (isSeletedDollar()) {
                        "$" + item.dollarPrice.toString()
                    } else {
                        "€" + item.euroPrice.toString()
                    },
                    fontSize = 24.sp,
                    color = Color(0xFF000101),
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconItem(iconId = R.drawable.icon_delete, {
                    onDelete(item)
                }, 22.dp)
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 3.dp,
                color = Color(0xFFEFEFEF)
            )
        }
    }
}

@Composable
fun ConfirmContent(
    itemCount: Int,
    onRetry: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(modifier = Modifier.padding(bottom = 24.dp)) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            TitleBar("Confirm Item Count")

            Spacer(modifier = Modifier.height(64.dp))

            // 아이템 개수 표시
            Text(
                text = "$itemCount Items",
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00CCCC),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(64.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 20.16.dp)
            ) {
                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6E6E6E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Retry Adding Items",
                        fontSize = 28.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, bottom = 6.dp),
                        textAlign = TextAlign.Center,
                        maxLines = 1, // 한 줄로 제한
                    )
                }
                Spacer(modifier = Modifier.height(8.dp)) // 버튼 사이 여백 (필요 시 추가)

                Button(
                    onClick = onConfirm,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0BC9D5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Confirm",
                        fontSize = 28.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, bottom = 6.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun SoftposPaymentContent(
    subTotal: Double = 83.57,
    tax: Double = 8.36,
    itemsCount: Int = 4,
    onDismiss: () -> Unit,
    onFinalize: () -> Unit,
    onWorldlineTapOnMobile: () -> Unit,
    onTapXPhone: () -> Unit
) {
    val total = subTotal + tax
    Box(modifier = Modifier.padding(bottom = 24.dp)) {
        // 실제 내용(타이틀, 금액 정보, 버튼들)
        Column(
        ) {
            // 타이틀 영역 (좌측 컬러 라인 + "Softpos Payment")
            TitleBar("Softpos Payment")

            Box(
                modifier = Modifier.padding(40.dp)
            ) {

                Column {
                    // Subtotal (아이템 수)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Subtotal",
                                fontSize = 22.sp,
                                color = Color(0xFF6E6E6E)
                            )
                            Text(
                                text = "($itemsCount Items)",
                                fontSize = 16.sp,
                                color = Color(0xFF6E6E6E)
                            )
                        }
                        Text(
                            text = String.format("€%.2f", subTotal),
                            fontSize = 22.sp,
                            color = Color(0xFF6E6E6E)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Tax
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tax", fontSize = 22.sp,
                            color = Color(0xFF6E6E6E)
                        )
                        Text(
                            text = String.format("€%.2f", tax),
                            fontSize = 22.sp,
                            color = Color(0xFF6E6E6E)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(),
                        thickness = 4.dp,
                        color = Color(0xFF00AEF0)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    // Total
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00AEF0)
                        )
                        Text(
                            text = String.format("€%.2f", total),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00AEF0)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier =
                Modifier
                    .background(brush = HorizontalGradientBrush())
                    .padding(20.dp)
            ) {
                Column {
                    // Worldline Tap on Mobile / Tap X Phone 버튼
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CustomImageButton(
                            R.drawable.logo_worldline,
                            "Wordline Tab\non Mobile",
                            {
                                onWorldlineTapOnMobile()
                            },
                            parentModifier = Modifier.weight(1f),
                            iconModifier = Modifier.size(48.dp)
                        )
                        CustomImageButton(
                            R.drawable.logo_iba,
                            "Tab X Phone",
                            {
                                onTapXPhone()
                            },
                            parentModifier = Modifier.weight(1f),
                            iconModifier = Modifier.size(96.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onFinalize,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF56B4ED)
                        ),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(
                            text = "Back to Home",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TitleBar(string: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .border(1.dp, Color(0xFFEFEFEF))
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .height(24.dp)
                .width(4.dp)
                .background(Color(0xFF00CCCC))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = string,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

