package com.coffeebliss.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.coffeebliss.app.ui.components.PointUpdateSuccessDialog
import com.coffeebliss.app.ui.theme.CoffeeGreen
import com.coffeebliss.app.ui.theme.CoffeeGreenLight
import com.coffeebliss.app.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel,
    memberName: String,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    uiState.successResult?.let { result ->
        PointUpdateSuccessDialog(
            pointsEarned = result.pointsEarned,
            newTotalPoints = result.newTotalPoints,
            amount = result.transaction.amount,
            onDismiss = {
                viewModel.dismissSuccessDialog()
                onNavigateBack()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaksi") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoffeeGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "Member: $memberName",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CoffeeGreen
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rp 10.000 = 1 Poin",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.amountInput,
                onValueChange = viewModel::onAmountChange,
                label = { Text("Nominal Pembelian") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                prefix = { Text("Rp ") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = if (uiState.amountInput.isEmpty()) "" else uiState.pointsPreview.toString(),
                onValueChange = {},
                label = { Text("Poin Didapat") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                readOnly = true,
                enabled = false
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.saveTransaction() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && uiState.amountInput.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeGreenLight)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Simpan", modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}
