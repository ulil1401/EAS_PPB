package com.coffeebliss.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.coffeebliss.app.data.model.Redemption
import com.coffeebliss.app.data.model.Transaction
import com.coffeebliss.app.ui.components.CoffeeCard
import com.coffeebliss.app.ui.components.CoffeeCardContent
import com.coffeebliss.app.util.Formatters
import com.coffeebliss.app.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    memberId: Long,
    onNavigateToRedeem: () -> Unit,
    onLogout: () -> Unit
) {
    val member by viewModel.member.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val redemptions by viewModel.redemptions.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Coffee Bliss") },
                actions = {
                    androidx.compose.material3.IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Kartu") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.History, contentDescription = null) },
                    label = { Text("Riwayat") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    label = { Text("Transaksi") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { onNavigateToRedeem() },
                    icon = { Icon(Icons.Default.CardGiftcard, contentDescription = null) },
                    label = { Text("Redeem") }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when (selectedTab) {
            0 -> CardTab(
                modifier = Modifier.padding(padding),
                member = member
            )
            1 -> HistoryTab(
                modifier = Modifier.padding(padding),
                transactions = transactions,
                redemptions = redemptions
            )
            2 -> TransactionTab(
                modifier = Modifier.padding(padding),
                uiState = uiState,
                onAmountChange = viewModel::onAmountChange,
                onAddTransaction = { viewModel.addTransaction(memberId) }
            )
        }
    }
}

@Composable
private fun CardTab(
    modifier: Modifier = Modifier,
    member: com.coffeebliss.app.data.model.Member?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (member != null) {
            MembershipCard(member = member)
            Spacer(modifier = Modifier.height(16.dp))
            CoffeeCard {
                CoffeeCardContent {
                    Text(
                        text = "Aturan Poin",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Setiap pembelian Rp10.000 = 1 Poin",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Contoh: Rp150.000 = 15 Poin",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            BoxLoading()
        }
    }
}

@Composable
private fun HistoryTab(
    modifier: Modifier = Modifier,
    transactions: List<Transaction>,
    redemptions: List<Redemption>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Riwayat Transaksi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (transactions.isEmpty()) {
            item {
                Text(
                    text = "Belum ada transaksi",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            items(transactions) { transaction ->
                TransactionItem(transaction)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Riwayat Redeem",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (redemptions.isEmpty()) {
            item {
                Text(
                    text = "Belum ada penukaran poin",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            items(redemptions) { redemption ->
                RedemptionItem(redemption)
            }
        }
    }
}

@Composable
private fun TransactionTab(
    modifier: Modifier = Modifier,
    uiState: com.coffeebliss.app.viewmodel.HomeUiState,
    onAmountChange: (String) -> Unit,
    onAddTransaction: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Tambah Transaksi",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Masukkan nominal pembelian. Poin dihitung otomatis (Rp10.000 = 1 poin).",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.amountInput,
            onValueChange = onAmountChange,
            label = { Text("Nominal Pembelian (Rp)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            prefix = { Text("Rp ") }
        )

        if (uiState.amountInput.isNotEmpty()) {
            val amount = uiState.amountInput.toLongOrNull() ?: 0L
            val points = amount / 10_000
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Poin yang didapat: $points poin",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onAddTransaction,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && uiState.amountInput.isNotEmpty()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Simpan Transaksi")
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    CoffeeCard {
        CoffeeCardContent {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = Formatters.formatCurrency(transaction.amount),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = Formatters.formatDate(transaction.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = "+${transaction.pointsEarned} poin",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun RedemptionItem(redemption: Redemption) {
    CoffeeCard {
        CoffeeCardContent {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = redemption.rewardName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = Formatters.formatDate(redemption.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = "-${redemption.pointsUsed} poin",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BoxLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}
