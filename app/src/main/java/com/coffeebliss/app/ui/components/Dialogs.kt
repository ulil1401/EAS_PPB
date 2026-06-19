package com.coffeebliss.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coffeebliss.app.data.model.Reward
import com.coffeebliss.app.ui.theme.CoffeeGreen
import com.coffeebliss.app.ui.theme.CoffeeGreenLight

@Composable
fun ConfirmRedeemDialog(
    reward: Reward,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Konfirmasi Redeem", fontWeight = FontWeight.Bold) },
        text = {
            Text(
                text = "Tukar ${reward.pointsRequired} poin dengan ${reward.name}?\n\n${reward.description}",
                textAlign = TextAlign.Start
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeGreenLight)
            ) {
                Text("Ya, Tukar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun RedeemSuccessDialog(
    rewardName: String,
    pointsUsed: Int,
    remainingPoints: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = CoffeeGreen,
                modifier = Modifier.size(48.dp)
            )
        },
        title = { Text("Redeem Berhasil!", fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$rewardName berhasil ditukar.",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "-$pointsUsed poin",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sisa poin: $remainingPoints",
                    style = MaterialTheme.typography.titleLarge,
                    color = CoffeeGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeGreen)
            ) {
                Text("OK")
            }
        }
    )
}

@Composable
fun PointUpdateSuccessDialog(
    pointsEarned: Int,
    newTotalPoints: Int,
    amount: Long,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = CoffeeGreen,
                modifier = Modifier.size(48.dp)
            )
        },
        title = { Text("Poin Bertambah!", fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Transaksi Rp ${String.format("%,d", amount).replace(',', '.')} berhasil disimpan.",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "+$pointsEarned poin",
                    style = MaterialTheme.typography.headlineSmall,
                    color = CoffeeGreen,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total poin sekarang",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "$newTotalPoints Poin",
                    style = MaterialTheme.typography.displaySmall,
                    color = CoffeeGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeGreen)
            ) {
                Text("OK")
            }
        }
    )
}
