package com.coffeebliss.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffeebliss.app.data.model.Reward
import com.coffeebliss.app.ui.components.ConfirmRedeemDialog
import com.coffeebliss.app.ui.components.RedeemSuccessDialog
import com.coffeebliss.app.ui.theme.CoffeeGreen
import com.coffeebliss.app.ui.theme.CoffeeGreenLight
import com.coffeebliss.app.viewmodel.RedeemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(
    viewModel: RedeemViewModel,
    memberId: Long,
    memberName: String,
    currentPoints: Int,
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

    uiState.pendingReward?.let { reward ->
        if (uiState.showConfirmDialog) {
            ConfirmRedeemDialog(
                reward = reward,
                onConfirm = { viewModel.confirmRedeem(memberId) },
                onDismiss = { viewModel.dismissConfirmDialog() }
            )
        }
    }

    uiState.successResult?.let { result ->
        RedeemSuccessDialog(
            rewardName = result.rewardName,
            pointsUsed = result.pointsUsed,
            remainingPoints = result.remainingPoints,
            onDismiss = { viewModel.dismissSuccessDialog() }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reward") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Member: $memberName",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = CoffeeGreen
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Poin tersedia: $currentPoints",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(uiState.rewards) { reward ->
                RewardItem(
                    reward = reward,
                    currentPoints = currentPoints,
                    isLoading = uiState.isLoading,
                    onRedeem = { viewModel.requestRedeem(reward) }
                )
            }
        }
    }
}

@Composable
private fun RewardItem(
    reward: Reward,
    currentPoints: Int,
    isLoading: Boolean,
    onRedeem: () -> Unit
) {
    val icon = when (reward.name) {
        "Espresso" -> "☕"
        "Cappuccino" -> "🥤"
        else -> "🍵"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 36.sp)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = reward.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${reward.pointsRequired} Poin",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CoffeeGreen
                )
            }
            Button(
                onClick = onRedeem,
                enabled = currentPoints >= reward.pointsRequired && !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeGreenLight)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text("Redeem")
                }
            }
        }
    }
}
