package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.WaterDrop
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
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import android.location.Geocoder
import java.util.Locale
import android.os.Build

import com.example.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadarScreen(viewModel: WeatherViewModel) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var locationText by remember { mutableStateOf("New Delhi, IN") }
    var searchQuery by remember { mutableStateOf("") }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    @SuppressLint("MissingPermission")
    fun fetchLocation() {
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
                                val newLoc = if (country.isNotEmpty()) "$displayCity, $country" else displayCity
                                locationText = newLoc
                                viewModel.setLocationName(newLoc)
                                Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            val address = addresses?.firstOrNull()
                            val neighborhood = address?.subLocality
                            val city = address?.locality ?: address?.subAdminArea ?: "Unknown City"
                            val displayCity = if (!neighborhood.isNullOrEmpty()) neighborhood else city
                            val country = address?.countryCode ?: ""
                            val newLoc = if (country.isNotEmpty()) "$displayCity, $country" else displayCity
                            locationText = newLoc
                            viewModel.setLocationName(newLoc)
                            Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        val newLoc = "${String.format("%.2f", location.latitude)}, ${String.format("%.2f", location.longitude)}"
                        locationText = newLoc
                        viewModel.setLocationName(newLoc)
                        Toast.makeText(context, "Location updated (Coordinates only)", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Could not find location. Ensure GPS is ON.", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to get location: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchLocation()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0F172A))) { // Dark background behind map
        // Background Map Image
        AsyncImage(
            model = "https://lh3.googleusercontent.com/aida/AP1WRLtGFfzdHb09CDljUks5ICFaljNDDrjA3mLKMI0d8onrar0g7x6oRv8OR8ZUn_ZwusWaJ_V73P97sDwtl8mxKjwrXwA5oB_zwXAzCnfchcYgVpqSjO2HeME-B62hFOM81VLvZINoOJSTmafJLc4Om7LpNqXaDy4X3_ZYFV_rz3g8Q-BnagDTDimDNwnjudVPmSmHNee6gFZuwWDbvX0SoDXIbSPXWew8DfrFqPAk4x-cinOEGDfaqwKEedo",
            contentDescription = "Radar Map",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().padding(bottom = 80.dp),
            alpha = 0.9f
        )
        
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Location", tint = MaterialTheme.colorScheme.primary)
                }
                Text(locationText, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                IconButton(onClick = {}) {
                    Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Search Overlay
            Box(modifier = Modifier.padding(24.dp)) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search locations...", color = MaterialTheme.colorScheme.outline) },
                    leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(32.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha=0.95f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha=0.95f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = {
                        Button(
                            onClick = { 
                                if (searchQuery.isNotBlank()) {
                                    viewModel.setLocationName(searchQuery)
                                    locationText = searchQuery
                                    Toast.makeText(context, "Showing results for $searchQuery", Toast.LENGTH_SHORT).show()
                                    // For real implementation, we'd geocode this to lat/lng and fetch weather
                                    searchQuery = ""
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                        ) {
                            Text("Go", fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Map Action Buttons
            Column(
                modifier = Modifier.align(Alignment.End).padding(end = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FloatingActionButton(onClick = {}, containerColor = MaterialTheme.colorScheme.surfaceContainerLowest, shape = CircleShape, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Filled.Layers, contentDescription = "Layers", tint = MaterialTheme.colorScheme.primary)
                }
                FloatingActionButton(onClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fetchLocation()
                    } else {
                        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                    }
                }, containerColor = MaterialTheme.colorScheme.primary, shape = CircleShape, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.Filled.MyLocation, contentDescription = "My Location", tint = Color.White)
                }
            }

            // Weather Info Floating Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 100.dp) // Leave space for bottom nav
                    .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(24.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Column {
                            Text(state.locationName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                            if (state.isLoading) {
                                Text("Fetching live data...", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.tertiary)
                            } else {
                                Text(state.weatherDesc, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.tertiary)
                            }
                        }
                        Box(modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer, CircleShape).padding(8.dp)) {
                            Icon(Icons.Outlined.Warning, contentDescription = "Warning", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(24.dp))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                        Column {
                            Text(if (state.isLoading) "--°" else "${state.temperature.toInt()}°", fontSize = 36.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                            Text(if (state.isLoading) "FEELS LIKE --°" else "FEELS LIKE ${state.feelsLike.toInt()}°", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 2.sp)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape).padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Outlined.WaterDrop, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (state.isLoading) "--%" else "${state.precipitation}mm", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Row(
                                modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape).padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Outlined.Air, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onTertiaryContainer)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (state.isLoading) "--" else "${state.windSpeed}km/h", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                            }
                        }
                    }
                }
            }
        }
    }
}
