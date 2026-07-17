package com.b7b.sobriety.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.b7b.sobriety.data.model.CheckIn
import com.b7b.sobriety.ui.dialogs.CheckInDialog
import com.b7b.sobriety.ui.theme.Danger
import com.b7b.sobriety.ui.theme.Primary
import com.b7b.sobriety.ui.theme.Success
import com.b7b.sobriety.util.DateUtils
import com.b7b.sobriety.viewmodel.SobrietyUiState
import com.b7b.sobriety.viewmodel.SobrietyViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import androidx.compose.ui.text.style.TextAlign
import java.util.*

@Composable
fun CalendarScreen(
    viewModel: SobrietyViewModel,
    uiState: SobrietyUiState
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var showCheckIn by remember { mutableStateOf(false) }
    var selectedDateForLog by remember { mutableStateOf<LocalDate?>(null) }
    
    val checkInsByDate = remember(uiState.checkIns) {
        uiState.checkIns.associateBy { it.date }
    }

    if (showCheckIn && selectedDateForLog != null) {
        CheckInDialog(
            onDismiss = { showCheckIn = false },
            viewModel = viewModel,
            isPastLog = true,
            initialDate = selectedDateForLog.toString()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Month Selector Header (Integrated as Top Nav is now static)
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous Month")
            }
            Text(
                "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next Month")
            }
        }

        // Days of Week Header
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(
                    day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Calendar Grid
        val firstDayOfMonth = currentMonth.atDay(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0=Sun, 1=Mon...
        val daysInMonth = currentMonth.lengthOfMonth()
        
        val totalCells = (firstDayOfWeek + daysInMonth + 6) / 7 * 7
        val cells = (0 until totalCells).map { i ->
            val day = i - firstDayOfWeek + 1
            if (day in 1..daysInMonth) currentMonth.atDay(day) else null
        }

        val quitDate = DateUtils.parseDate(uiState.preferences.quitDate)

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(cells) { date ->
                if (date != null) {
                    val dateStr = date.toString()
                    val checkIn = checkInsByDate[dateStr]
                    CalendarDayCell(date, checkIn, quitDate) {
                        selectedDateForLog = date
                        showCheckIn = true
                    }
                } else {
                    Box(Modifier.aspectRatio(1f))
                }
            }
        }

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(Success, "Sober")
            Spacer(Modifier.width(16.dp))
            LegendItem(Danger, "Slip")
        }
    }
}

@Composable
fun CalendarDayCell(date: LocalDate, checkIn: CheckIn?, quitDate: LocalDate?, onDayClick: () -> Unit) {
    val today = LocalDate.now()
    val isFuture = date.isAfter(today)
    val isQuitDateOrAfter = quitDate != null && !date.isBefore(quitDate)
    val isImpliedSober = isQuitDateOrAfter && !isFuture && checkIn == null

    val bgColor = when {
        checkIn?.status == "sober" -> Success.copy(alpha = 0.2f)
        checkIn?.status == "slip" -> Danger.copy(alpha = 0.1f)
        isImpliedSober -> Success.copy(alpha = 0.08f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }
    val contentColor = when {
        checkIn?.status == "sober" -> Success
        checkIn?.status == "slip" -> Danger
        isImpliedSober -> Success.copy(alpha = 0.6f)
        else -> MaterialTheme.colorScheme.onSurface
    }

    val isSelected = checkIn != null || !isFuture

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .alpha(if (isFuture) 0.3f else 1f)
            .clickable(enabled = isSelected) {
                onDayClick()
            },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.4f)),
        shape = MaterialTheme.shapes.small
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "${date.dayOfMonth}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
            if (checkIn?.mood != null) {
                Text(
                    checkIn.mood,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(2.dp)
                )
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, MaterialTheme.shapes.extraSmall)
        )
        Spacer(Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
