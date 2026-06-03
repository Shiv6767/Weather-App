package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {
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
            Text("New Delhi, IN", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.primary)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida/AP1WRLumRuXmAL9-NZQJaV4dAOCgVJtac6rIXm9CL81_gTcAx2uV4SYtTnIGRB54NMoW_glPVTKvHnBGAG4ZYHTM4iJRh0ICB623TRGmrj0lD4czoyfcYUbxbJekeGBH7Iw1YCb4-lD9udnhTq7ms_lKtouBbCVI2_0cI0ryZjepv3-eGXyRqF3KMYUjU_F1eYeeiQ41vS0CRoyRQCfXdDF2tWmzES1xuxVn5qk9miIK8FP4s6ckCILyNcg2wao",
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(4.dp, MaterialTheme.colorScheme.surfaceContainerLowest, CircleShape)
                )
                FloatingActionButton(
                    onClick = {},
                    modifier = Modifier.size(40.dp).offset(x = 4.dp, y = 4.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Maya Silva", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text("Free Member", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(32.dp))

            // Preferred Locations
            CardItem(icon = Icons.Filled.Favorite, iconTint = MaterialTheme.colorScheme.secondary, iconBg = MaterialTheme.colorScheme.secondaryContainer, title = "Preferred Locations") {
                LocationItem(city = "New Delhi, IN", icon = Icons.Outlined.LocationCity)
                Spacer(modifier = Modifier.height(12.dp))
                LocationItem(city = "Goa, IN", icon = Icons.Outlined.BeachAccess)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.tertiary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Location", color = MaterialTheme.colorScheme.tertiary, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Units
            CardItem(icon = Icons.Outlined.DeviceThermostat, iconTint = MaterialTheme.colorScheme.onTertiaryContainer, iconBg = MaterialTheme.colorScheme.tertiaryContainer, title = "Units") {
                UnitToggleRow(label = "Temperature", optA = "°C", optB = "°F", isASelected = true)
                Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceVariant)
                UnitToggleRow(label = "Distance", optA = "km", optB = "mi", isASelected = true)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Notifications
            CardItem(icon = Icons.Filled.NotificationsActive, iconTint = MaterialTheme.colorScheme.onPrimaryContainer, iconBg = MaterialTheme.colorScheme.primaryContainer, title = "Notifications") {
                ToggleRow(label = "Severe Weather Alerts", checked = true)
                Spacer(modifier = Modifier.height(16.dp))
                ToggleRow(label = "Daily Summary", checked = false)
                Spacer(modifier = Modifier.height(16.dp))
                ToggleRow(label = "Radar Updates", checked = true)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // About App
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("About App", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                        Text("Version 2.4.1 (Candy Build)", fontSize = 14.sp, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { 
                    navController.navigate("auth") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.error),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign Out", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun CardItem(icon: androidx.compose.ui.graphics.vector.ImageVector, iconTint: Color, iconBg: Color, title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 20.dp)) {
            Box(modifier = Modifier.size(40.dp).background(iconBg, CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = iconTint)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        content()
    }
}

@Composable
fun LocationItem(city: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(12.dp))
            Text(city, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
        }
        Icon(Icons.Filled.DragIndicator, contentDescription = "Drag", tint = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun UnitToggleRow(label: String, optA: String, optB: String, isASelected: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer, CircleShape).padding(4.dp),
        ) {
            Box(
                modifier = Modifier
                    .background(if (isASelected) Color.White else Color.Transparent, CircleShape)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(optA, fontWeight = if (isASelected) FontWeight.Bold else FontWeight.Medium, color = if (isASelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline)
            }
            Box(
                modifier = Modifier
                    .background(if (!isASelected) Color.White else Color.Transparent, CircleShape)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(optB, fontWeight = if (!isASelected) FontWeight.Bold else FontWeight.Medium, color = if (!isASelected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
fun ToggleRow(label: String, checked: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Switch(
            checked = checked, 
            onCheckedChange = {},
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}
