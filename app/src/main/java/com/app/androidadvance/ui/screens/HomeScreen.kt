@file:Suppress("NAME_SHADOWING")

package com.app.androidadvance.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.androidadvance.data.StudentRepository
import com.app.androidadvance.models.Student
import com.app.androidadvance.ui.components.DialogAddStudent
import com.app.androidadvance.ui.components.SearchBar
import com.app.androidadvance.ui.components.StudentCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(navController: NavController){
    var query by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var showDialogAddStudent by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val students = remember { mutableStateListOf<Student>() }
    coroutineScope.launch {
        val result = StudentRepository.getStudents(query)
        result.onSuccess {
            students.clear()
            students.addAll(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color(0xFFFE8C00),
                elevation = 0.dp,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = "Quản lý..." + students.count(),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialogAddStudent = true
                },
                containerColor = Color(0xFFFE8C00), // Sử dụng containerColor thay cho backgroundColor
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Add, // Dùng Icons.Filled.Add trong material3
                    contentDescription = "Thêm",
                    tint = Color.White
                )
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ){
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                }
            )

            LazyColumn {
                items(students){
                    StudentCard(
                        student = it,
                        onEdit = {
                        },
                        onDelete = {}
                    )
                }
            }
            //region Dialog Add Student
            if(showDialogAddStudent){
                DialogAddStudent(
                    onAddStudent = {
                        coroutineScope.launch {
                            val result = StudentRepository.addStudent(it)
                            result.onSuccess {
                                showDialogAddStudent = false
                                coroutineScope.launch {
                                    val result = StudentRepository.getStudents(query)
                                    result.onSuccess {
                                        students.clear()
                                        students.addAll(it)
                                    }
                                }
                                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
                            }
                            result.onFailure {
                                Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onDismiss = {showDialogAddStudent = false}

                )
            }
            //endregion

        }

    }
}