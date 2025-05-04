package com.app.androidadvance.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lint.kotlin.metadata.Visibility
import androidx.navigation.NavController
import com.app.androidadvance.services.UserService
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController){
    //region init variable
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullname by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    //endregion

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState)
            .imePadding()
            .padding(WindowInsets.statusBars.asPaddingValues())
        ,horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        //region Tiêu đề
        Text(
            text = "Tạo tài khoản mới",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        //endregion

        //region Mô tả
        Text(
            text = "Tạo tài khoản mới ngay bây giờ",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        //endregion

        //region Email
        Column{
            Text(
                text = "Địa chỉ email"
            )
            // Trường nhập email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // bo góc viền
                singleLine = true
            )
            if(emailError){
                Spacer(modifier = Modifier.height(8.dp))
                Text("Email không hợp lệ", color = Color.Red, fontSize = 12.sp)
            }

        }
        //endregion

        //region Họ tên
        Column{
            Text("Họ và tên")
            OutlinedTextField(
                value = fullname,
                onValueChange = {
                    fullname = it
                },
                label = { Text("Họ và tên") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // bo góc viền
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        //endregion

        //region Password
        Column{
            Text("Mật khẩu")
            // Trường nhập mật khẩu
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text("Mật khẩu") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // bo góc viền
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        //endregion

        //region Xác nhận password
        Column{
            Text(" Xác nhận mật khẩu")
            // Trường nhập mật khẩu
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                },
                label = { Text("Xác nhận mật khẩu") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // bo góc viền
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        //endregion

        //region Nút Đăng ký
        Button(
            onClick = {
                isLoading = true
                if(fullname.trim().isEmpty()){
                    Toast.makeText(context,"Tên không được để trống", Toast.LENGTH_SHORT).show();
                    isLoading = false
                    return@Button
                }

                if (password == confirmPassword && password.length >= 8) {
                    coroutineScope.launch {
                        val result  = UserService.register(email.trim(), password.trim(), fullname.trim())

                        if (result.isSuccess) {
                            // Đăng ký thành công
                            Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                            navController.navigate("home"){
                                popUpTo(0)
                            }
                        } else {
                            // Đăng ký thất bại
                            Toast.makeText(context, "Đăng ký thất bại, email đã được đăng kí!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    if(password.length < 8){
                        Toast.makeText(context,"Mật khẩu tối thiểu 8 ký tự!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                    }
                }
                isLoading = false
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFE8C00) // Màu nền của Button
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
            } else {
                Text(modifier = Modifier.padding(vertical = 8.dp), text = "Đăng ký")
            }
        }
        //endregion
    }


}