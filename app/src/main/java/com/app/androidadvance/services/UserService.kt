package com.app.androidadvance.services

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.app.androidadvance.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object UserService {
    var currentUser by mutableStateOf<User?>(null)

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    init {
        // Khởi tạo currentUser khi ứng dụng bắt đầu
        initializeCurrentUser()
    }

    // Hàm khởi tạo currentUser
    fun initializeCurrentUser() {
        val email = auth.currentUser?.email
        if (email != null) {
            getUserByEmail(email) { user ->
                currentUser = user
            }
        }
    }

    suspend fun register(email: String, password: String, fullname: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val newUser  = User(
                email = email,
                name = fullname,
            );
            db.collection("users")
                .document(newUser.email) // Dùng email làm Document ID
                .set(newUser)
                .addOnSuccessListener {
                    Log.d("Firestore", "Người dùng đã được thêm thành công!")
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Lỗi khi thêm người dùng", e)
                }
            login(email,password)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            initializeCurrentUser()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun updateUser(curUser: User? = currentUser, onComplete: (Boolean) -> Unit) {
        if(curUser == null) return
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(curUser.email) // Sử dụng email làm ID
            .set(curUser)
            .addOnSuccessListener {
                Log.d("Firestore", "Cập nhật người dùng thành công")
                onComplete(true)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Lỗi khi cập nhật người dùng", e)
                onComplete(false)
            }
    }


    fun getUserByEmail(email: String, onResult: (User?) -> Unit) {
        db.collection("users")
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val user = document.toObject(User::class.java)
                    onResult(user)
                } else {
                    Log.d("Firestore", "Không tìm thấy người dùng.")
                    onResult(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Lỗi khi lấy người dùng", e)
                onResult(null)
            }
    }

    fun isLogin(): Boolean = auth.currentUser != null

    fun logout() = auth.signOut()


}