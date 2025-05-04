package com.app.androidadvance.data

import android.annotation.SuppressLint
import com.app.androidadvance.models.Student
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object StudentRepository {
    @SuppressLint("StaticFieldLeak")
    private val firestore = FirebaseFirestore.getInstance()
    private val studentCollection = firestore.collection("students")

    suspend fun getStudents(query: String = ""): Result<List<Student>> = try {
        val snapshot = studentCollection.get().await()
        val students = snapshot.toObjects(Student::class.java)
        val filtered = if (query.isNotBlank()) {
            students.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.name.contains(query, ignoreCase = true)
            }
        } else {
            students
        }
        Result.success(filtered)
    }catch (e: Exception){
        Result.failure(e)
    }
    suspend fun addStudent(student: Student): Result<Boolean> = try {
        studentCollection.document(student.id).set(student).await()
//        studentCollection.add(student).await()
        Result.success(true)
    }catch (e: Exception){
        Result.failure(e)
    }
    suspend fun updateStudent(student: Student): Result<Boolean> = try {
        studentCollection.document(student.id).set(student).await()
        Result.success(true)
    }catch (e: Exception) {
        Result.failure(e)
    }
    suspend fun deleteStudent(student: Student): Result<Boolean> = try {
        studentCollection.document(student.id).delete().await()
        Result.success(true)
    }catch (e: Exception) {
        Result.failure(e)
    }
}