package com.bluebirdcorp.softpos.feacture.payment.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bluebirdcorp.softpos.common.utils.debug
import com.bluebirdcorp.softpos.domain.usecase.BarcodeDatabaseUsecase
import com.bluebirdcorp.softpos.feacture.payment.R
import com.bluebirdcorp.softpos.feacture.payment.model.BarcodeUiModel
import com.bluebirdcorp.softpos.feacture.payment.view_model.BarcodeItemDBViewModel
import com.bluebirdcorp.softpos.feacture.ui.CommonDialogShape
import com.bluebirdcorp.softpos.feacture.ui.IconItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BarcodeItemSettingsFragment : Fragment() {

    private val viewModel: BarcodeItemDBViewModel by viewModels()

    @Inject
    lateinit var barcodeDatabaseUsecase: BarcodeDatabaseUsecase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        debug("onCreateView")
        return ComposeView(requireContext()).apply {
            setContent {
                BarcodeItemSettingScreen(viewModel, barcodeDatabaseUsecase)
            }
        }
    }

    @Composable
    fun IconRow() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFF5F5F5),
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconItem(iconId = R.drawable.icon_arrow, {
                findNavController().popBackStack()
            })
            Spacer(modifier = Modifier.weight(1f))
            IconItem(iconId = R.drawable.icon_home, {
                findNavController().navigate(R.id.action_intro)
            })
        }
    }

    @Composable
    fun BarcodeItemSettingScreen(
        viewModel: BarcodeItemDBViewModel,
        barcodeDatabaseUsecase: BarcodeDatabaseUsecase
    ) {
        val context = LocalContext.current

        val barcodeList by viewModel.barcodeList.observeAsState(emptyList())

        var showDialog by remember { mutableStateOf(false) }
        var selectedBarcode by remember { mutableStateOf<BarcodeUiModel?>(null) }

        if (showDialog && selectedBarcode != null) {
            CommonDialogShape(
                onDismiss = {
                    showDialog = false
                    selectedBarcode = null
                }
            ) {
                ProductCard(
                    barcode = selectedBarcode!!,
                    onEdit = null,
                    onDismiss = {
                        showDialog = false // showDialog 닫기 로직 추가
                    },
                    isEditing = true
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFEDEDED),
                )
        ) {
            IconRow()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            ) {
                AddButton(onClick = {
                    showDialog = true
                    selectedBarcode = BarcodeUiModel(
                        id = 0,
                        name = "",
                        dollarPrice = 0.0,
                        euroPrice = 0.0,
                    )
                }, "ADD")

                // 바코드 리스트 표시
                BarcodeListScreen(
                    barcodeList = barcodeList,
                    onEdit = { barcode ->
                        selectedBarcode = barcode
                        showDialog = true
                    }
                )
            }
        }
    }

    @Composable
    fun BarcodeListScreen(
        barcodeList: List<BarcodeUiModel>,
        onEdit: (BarcodeUiModel) -> Unit
    ) {
        LazyColumn {
            itemsIndexed(barcodeList) { index, barcode ->
                barcode.index = index
                ProductCard(
                    barcode = barcode,
                    onEdit,
                    null,
                    isEditing = false
                )
            }
        }
    }

    @Composable
    fun AddButton(
        onClick: () -> Unit,
        text: String
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = Color(0xFF035ADE),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 30.sp,
                color = Color.White
            )
        }
    }

    @Composable
    fun ProductCard(
        barcode: BarcodeUiModel,
        onEdit: ((BarcodeUiModel) -> Unit)?,
        onDismiss: (() -> Unit)?,
        isEditing: Boolean
    ) {
        debug("ProductCard barcode info : $barcode")

        var isEditing by remember { mutableStateOf(isEditing) }

        val observedBarcodeId by viewModel.barcodeScanId.observeAsState(initial = 0L)
        var displayedBarcodeId by remember { mutableStateOf(barcode.id) }
        var productName by remember { mutableStateOf(barcode.name) }
        var dollarPrice by remember { mutableStateOf(barcode.dollarPrice) }
        var euroPrice by remember { mutableStateOf(barcode.euroPrice) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "No. ${barcode.index + 1}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        modifier = Modifier.weight(1f)
                    )

                    if (!isEditing) {
                        IconButton(onClick = {
                            onEdit?.let {
                                it(barcode)
                            }
                        }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Tag ID", fontSize = 20.sp, color = Color.Gray)
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color.LightGray
                            )


                            LaunchedEffect(observedBarcodeId) {
                                if (isEditing) {
                                    displayedBarcodeId = observedBarcodeId
                                }
                            }

                            Text(
                                text = displayedBarcodeId.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (isEditing) {
                            Button(
                                onClick = { isEditing = true },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF1E88E5)) // 파란색
                            ) {
                                Text(text = "Write", color = Color.White)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Product Name", color = Color.Gray, fontSize = 18.sp)
                debug("productName : $productName" )
                debug("displayedBarcodeId : $displayedBarcodeId" )

                if (isEditing) {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        readOnly = !isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    )
                } else {
                    HorizontalDivider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text = productName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }


                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "$ Dollar", fontSize = 20.sp, color = Color.Gray)

                        if (isEditing) {
                            OutlinedTextField(
                                value = dollarPrice.toString(),
                                onValueChange = { dollarPrice = it.toDouble() },
                                readOnly = !isEditing,
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                HorizontalDivider(
                                    color = Color.LightGray,
                                    thickness = 1.dp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = dollarPrice.toString(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "€ Euro", fontSize = 20.sp, color = Color.Gray)

                        if (isEditing) {
                            OutlinedTextField(
                                value = euroPrice.toString(),
                                onValueChange = { euroPrice = it.toDouble() },
                                readOnly = !isEditing,
                                textStyle = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                HorizontalDivider(
                                    color = Color.LightGray,
                                    thickness = 1.dp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    text = euroPrice.toString(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                if (isEditing) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            if (displayedBarcodeId == 0L && productName.isBlank()) {
                                Toast.makeText(
                                    context,
                                    context?.getString(R.string.barcode_add_empty_message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.addBarcodeItem(
                                    barcode.copy(
                                        id = displayedBarcodeId,
                                        name = productName,
                                        dollarPrice = dollarPrice,
                                        euroPrice = euroPrice
                                    )
                                )
                                onDismiss?.let { it() }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF035ADE)) // 파란색
                    ) {
                        Text(text = "Save", fontSize = 22.sp, color = Color.White)
                    }
                }
            }
        }
    }
}