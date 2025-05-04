package com.app.androidadvance.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.app.androidadvance.models.Student

@Composable
fun DialogAddStudent(
    onAddStudent: (Student) -> Unit,
    onDismiss: () -> Unit
){
    var id by remember { mutableStateOf("")  }
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val student = Student(
                        name = name,
                        id = id
                    )
                    onAddStudent(student)
                }
            ) {
                Text(
                    text = "Thêm",
                    color = Color(0xFFFE8C00)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Hủy",
                    color = Color(0xFFFE8C00)
                )
            }
        },
        title = { Text("Thêm sinh viên") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("Mã sinh viên") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF9800), // Cam khi focus
                        unfocusedBorderColor = Color(0xFFFF9800) // Cam khi chưa focus
                    )
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF9800), // Cam khi focus
                        unfocusedBorderColor = Color(0xFFFF9800) // Cam khi chưa focus
                    )
                )

            }
        }
    )
}