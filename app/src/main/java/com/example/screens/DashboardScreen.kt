package com.example.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.viewmodel.WeatherViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

@Composable
fun DashboardScreen(viewModel: WeatherViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    @SuppressLint("MissingPermission")
    fun fetchLocationAndWeather() {
        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    viewModel.fetchWeatherForLocation(location.latitude, location.longitude)
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                                val address = addresses.firstOrNull()
                                val neighborhood = address?.subLocality
                                val city = address?.locality ?: address?.subAdminArea ?: "Unknown City"
                                val displayCity = if (!neighborhood.isNullOrEmpty()) neighborhood else city
                                val country = address?.countryCode ?: ""
                                viewModel.setLocationName(if (country.isNotEmpty()) "$displayCity, $country" else displayCity)
                            }
                        } else {
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            val address = addresses?.firstOrNull()
                            val neighborhood = address?.subLocality
                            val city = address?.locality ?: address?.subAdminArea ?: "Unknown City"
                            val displayCity = if (!neighborhood.isNullOrEmpty()) neighborhood else city
                            val country = address?.countryCode ?: ""
                            viewModel.setLocationName(if (country.isNotEmpty()) "$displayCity, $country" else displayCity)
                        }
                    } catch (e: Exception) {
                        viewModel.setLocationName("${String.format("%.2f", location.latitude)}, ${String.format("%.2f", location.longitude)}")
                    }
                }
            }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchLocationAndWeather()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchLocationAndWeather()
        } else {
            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant) // Light background
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), CircleShape)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.LocationOn, contentDescription = "Location", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(state.locationName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }
            Icon(Icons.Outlined.CalendarToday, contentDescription = "Calendar", tint = MaterialTheme.colorScheme.tertiary)
        }

        // Hero Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(32.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(32.dp))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp).padding(bottom = 16.dp))
                    Text("Fetching Live Weather...", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    Icon(state.weatherIcon, contentDescription = state.weatherDesc, modifier = Modifier.size(100.dp), tint = MaterialTheme.colorScheme.tertiary)
                    Row(verticalAlignment = Alignment.Top) {
                        Text("${state.temperature.toInt()}°", fontSize = 100.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, lineHeight = 100.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier
                                .background(Color(0xFFff9800), RoundedCornerShape(16.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("AQI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("${state.aqi}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    Text(state.weatherDesc, fontSize = 28.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
                    
                    val insight = if (state.aqi > 100) "Poor air quality. Avoid prolonged outdoor exercise today." else "Great weather! Enjoy the outdoors."
                    Text(insight, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp, bottom = 24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        ChipItem(icon = Icons.Outlined.DeviceThermostat, text = "Feels ${state.feelsLike.toInt()}°", tint = MaterialTheme.colorScheme.primary)
                        ChipItem(icon = Icons.Outlined.Air, text = "${state.windSpeed} km/h", tint = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }
        }

        // Smart Insight Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(32.dp))
                .padding(start = 8.dp) // for left border effect
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 8.dp) // thick left border
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp, topStart = 24.dp, bottomStart = 24.dp))
                .padding(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Icon(Icons.Outlined.Psychology, contentDescription = "Insight", tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Smart Insight", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Outlined.AutoAwesome, contentDescription = "AI", tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("AI Recommendation: High UV and 32°C. Wear light cotton, sunglasses, and carry a water bottle. High chance of a sudden shower at 4 PM.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // Commute Window
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(32.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(32.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("COMMUTE WINDOW", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CommuteItem(title = "DEPARTURE", time = "8:00 AM", icon = Icons.Filled.WbSunny, temp = "24°", desc = "Clear", tint = MaterialTheme.colorScheme.tertiary)
                Box(modifier = Modifier.weight(1f).height(4.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)))
                CommuteItem(title = "RETURN", time = "4:00 PM", icon = Icons.Filled.BeachAccess, temp = "28°", desc = "Rainy", tint = MaterialTheme.colorScheme.primary)
            }
        }

        // Live Radar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(32.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(32.dp))
                .padding(24.dp)
        ) {
            Text("LIVE RADAR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, letterSpacing = 2.sp, modifier = Modifier.padding(bottom = 16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida/AP1WRLtGFfzdHb09CDljUks5ICFaljNDDrjA3mLKMI0d8onrar0g7x6oRv8OR8ZUn_ZwusWaJ_V73P97sDwtl8mxKjwrXwA5oB_zwXAzCnfchcYgVpqSjO2HeME-B62hFOM81VLvZINoOJSTmafJLc4Om7LpNqXaDy4X3_ZYFV_rz3g8Q-BnagDTDimDNwnjudVPmSmHNee6gFZuwWDbvX0SoDXIbSPXWew8DfrFqPAk4x-cinOEGDfaqwKEedo",
                    contentDescription = "Live Radar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.Black.copy(alpha = 0.6f)))))
                
                Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("LIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp))
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.White, modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape).padding(4.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape).padding(8.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Rain approaching", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Updated 2m ago", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChipItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, tint: Color) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun CommuteItem(title: String, time: String, icon: androidx.compose.ui.graphics.vector.ImageVector, temp: String, desc: String, tint: Color) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(time, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier.background(tint.copy(alpha = 0.1f), CircleShape).padding(12.dp)
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(28.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(temp, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Text(desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
