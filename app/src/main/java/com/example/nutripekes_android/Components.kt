package com.example.nutripekes_android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun BirthYearEntryDialog(
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "¡Bienvenido!")
        },
        text = {
            Column {
                Text(text= "Para mejorar tu experiencia, ingresa el año de nacimiento de tu Peke.")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = textInput,
                    onValueChange = {
                        textInput = it
                        isError = false
                    },
                    label = { Text("Año (ej: 2020)") },
                    singleLine = true,
                    isError = isError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if(isError) {
                    Text(
                        text = "Por favor, ingresa un año válido.",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val year = textInput.toIntOrNull()
                    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

                    if( year != null && year > 2000 && year <= currentYear) {
                        onSave(year)
                    } else{
                        isError = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE55B57))
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Omitir")
            }
        }
    )
}


@Composable
fun FoodEditDialog(
    label: String,
    currentCount: Int,
    maxCount: Int,
    color: Color,
    onDismiss: () -> Unit,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = label.replace("\n", " "),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Porciones actuales:", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Botón RESTAR
                    FilledIconButton(
                        onClick = onRemove,
                        enabled = currentCount > 0,
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.LightGray)
                    ) {
                        Text(text = "-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = "$currentCount / $maxCount",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    // Botón SUMAR
                    FilledIconButton(
                        onClick = onAdd,
                        enabled = currentCount < maxCount,
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = color)
                    ) {
                        Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = color)
            ) {
                Text("Listo")
            }
        }
    )
}
