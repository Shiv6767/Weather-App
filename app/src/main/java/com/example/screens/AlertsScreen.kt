package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Masks
import androidx.compose.material.icons.outlined.Thunderstorm
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AlertsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.LocationOn, contentDescription = "Location", tint = MaterialTheme.colorScheme.primary)
            }
            Text("New Delhi, IN", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.primary)
            }
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .padding(bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Active Alerts", fontSize = 32.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("3 notifications requiring your attention", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
                }
                Box(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, CircleShape).padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("Update: 2m ago", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            
            // Alert 1 (Urgent - Primary)
            AlertCard(
                title = "Severe Heat Warning",
                message = "Temperatures expected to exceed 42°C. Stay hydrated and avoid direct sun exposure between 11 AM and 4 PM.",
                tag = "URGENT",
                color = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                icon = Icons.Filled.WbSunny,
                tagColor = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Alert 2 (Info - Tertiary)
            AlertCard(
                title = "Rain Approaching",
                message = "Light to moderate showers expected in your area. Radar indicates movement from the south-west.",
                tag = "IN 20 MINS",
                color = MaterialTheme.colorScheme.tertiary,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                icon = Icons.Outlined.Thunderstorm,
                tagColor = MaterialTheme.colorScheme.tertiary,
                actionLabel = "View Radar"
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            // Alert 3 (Warning - Secondary)
            AlertCard(
                title = "AQI Update: Poor",
                message = "Air quality has dropped. Sensitive groups should reduce prolonged outdoor exertion.",
                tag = "AQI 215",
                color = MaterialTheme.colorScheme.secondary,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                icon = Icons.Outlined.Masks,
                tagColor = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun AlertCard(
    title: String,
    message: String,
    tag: String,
    color: Color,
    containerColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tagColor: Color,
    actionLabel: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .height(IntrinsicSize.Min)
    ) {
        Box(modifier = Modifier.width(6.dp).fillMaxHeight().background(color, RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)))
        
        Row(modifier = Modifier.padding(20.dp).fillMaxWidth()) {
            Box(
                modifier = Modifier.size(56.dp).background(color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = color, modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.background(containerColor, CircleShape).padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text(tag, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = tagColor)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(message, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 20.sp)
                
                if (actionLabel != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(onClick = {}, contentPadding = PaddingValues(0.dp)) {
                        Text(actionLabel, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                } else if (title.contains("Heat")) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Schedule, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.outline)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Valid until 6:00 PM", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }
    }
}
