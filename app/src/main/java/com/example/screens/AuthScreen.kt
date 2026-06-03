package com.example.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Decorative Blurs
        Box(
            modifier = Modifier
                .offset(x = (-80).dp, y = (-80).dp)
                .size(150.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f), CircleShape)
                .blur(40.dp)
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .offset(x = 80.dp, y = 80.dp)
                .size(200.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape)
                .blur(50.dp)
                .align(Alignment.BottomEnd)
        )

        // Main Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(32.dp))
                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.BubbleChart, contentDescription = "Logo", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("CloudCandy", fontSize = 32.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
            Text("Join the sweetest weather app!", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { navController.navigate("dashboard") },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest, contentColor = MaterialTheme.colorScheme.onSurface),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text("Continue with Google", fontWeight = FontWeight.Bold)
            }
            
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.surfaceVariant)
                Text("or", modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Medium)
                Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.surfaceVariant)
            }
            
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email address", color = MaterialTheme.colorScheme.outlineVariant) },
                leadingIcon = { Icon(Icons.Outlined.Mail, contentDescription = null, tint = MaterialTheme.colorScheme.outline) },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Create a password", color = MaterialTheme.colorScheme.outlineVariant) },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.outline) },
                trailingIcon = { 
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(Icons.Outlined.Visibility, contentDescription = null, tint = MaterialTheme.colorScheme.outline) 
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { termsAccepted = it },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary, uncheckedColor = MaterialTheme.colorScheme.outline)
                )
                val annotatedString = buildAnnotatedString {
                    append("I agree to the ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
                        append("Terms of Service")
                    }
                    append(" and ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
                        append("Privacy Policy")
                    }
                }
                Text(text = annotatedString, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 8.dp))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { navController.navigate("dashboard") },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Create Account", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Filled.ArrowForward, contentDescription = null)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("Already have an account? ", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Sign In", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { })
            }
        }
    }
}
