package com.coffeebliss.app.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffeebliss.app.data.model.Member
import com.coffeebliss.app.data.model.Redemption
import com.coffeebliss.app.data.model.Transaction
import com.coffeebliss.app.ui.components.ProfileAvatar
import com.coffeebliss.app.ui.theme.CoffeeCardDark
import com.coffeebliss.app.ui.theme.CoffeeGreen
import com.coffeebliss.app.util.Formatters
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCardScreen(
    member: Member?,
    transactions: List<Transaction>,
    redemptions: List<Redemption>,
    onNavigateBack: () -> Unit,
    onNavigateTransaction: () -> Unit,
    onNavigateReward: () -> Unit,
    onNavigateEditProfile: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kartu Member") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateEditProfile) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profil", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CoffeeGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        if (member == null) return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PosterMemberCard(member = member)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateTransaction,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeGreen)
                ) {
                    Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(" Transaksi", modifier = Modifier.padding(start = 4.dp))
                }
                Button(
                    onClick = onNavigateReward,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeGreen)
                ) {
                    Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(" Reward", modifier = Modifier.padding(start = 4.dp))
                }
            }

            if (transactions.isNotEmpty() || redemptions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.History, contentDescription = null, tint = CoffeeGreen)
                    Text(
                        text = "  Riwayat",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                transactions.forEach { tx ->
                    HistoryItem(
                        title = Formatters.formatCurrency(tx.amount),
                        subtitle = Formatters.formatDate(tx.date),
                        points = "+${tx.pointsEarned} poin",
                        isPositive = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                redemptions.forEach { rd ->
                    HistoryItem(
                        title = rd.rewardName,
                        subtitle = Formatters.formatDate(rd.date),
                        points = "-${rd.pointsUsed} poin",
                        isPositive = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun PosterMemberCard(member: Member) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CoffeeCardDark)
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "COFFEE BLISS\nMEMBER CARD",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileAvatar(photoPath = member.photoPath, size = 80.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = member.name.uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ID: ${member.memberNumber}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF757575)
                )
                Spacer(modifier = Modifier.height(20.dp))

                QrCodeImage(
                    content = member.memberNumber,
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "POINTS",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF757575),
                    letterSpacing = 2.sp
                )
                Text(
                    text = "${member.points}",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = CoffeeGreen
                )
            }
        }
    }
}

@Composable
private fun HistoryItem(
    title: String,
    subtitle: String,
    points: String,
    isPositive: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title, fontWeight = FontWeight.SemiBold)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = points,
                fontWeight = FontWeight.Bold,
                color = if (isPositive) CoffeeGreen else MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun QrCodeImage(content: String, modifier: Modifier = Modifier) {
    var bitmap by remember(content) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(content) {
        bitmap = withContext(Dispatchers.Default) { generateQrCode(content) }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun generateQrCode(content: String, size: Int = 256): Bitmap? {
    return try {
        val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val pixels = IntArray(size * size) { i ->
            if (bitMatrix[i % size, i / size]) android.graphics.Color.BLACK
            else android.graphics.Color.WHITE
        }
        Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).apply {
            setPixels(pixels, 0, size, 0, 0, size, size)
        }
    } catch (e: Exception) {
        null
    }
}
