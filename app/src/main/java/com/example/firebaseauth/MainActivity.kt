package com.example.firebaseauthdemo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var btnResetPassword: Button
    private lateinit var btnDeleteAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa o Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Mapeia os elementos do layout
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        btnResetPassword = findViewById(R.id.btnResetPassword)
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount)

        // Configura as ações dos botões
        btnLogin.setOnClickListener { loginUser() }
        btnRegister.setOnClickListener { registerUser() }
        btnResetPassword.setOnClickListener { resetPassword() }
        btnDeleteAccount.setOnClickListener { deleteUserAccount() }
    }

    // 1. Logar
    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha o e-mail e a senha", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, "Login efetuado: ${user?.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro no login: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // 2. Criar um usuário usando createUserWithEmailAndPassword
    private fun registerUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha o e-mail e a senha", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro ao criar usuário: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // 3. Enviar e-mail de redefinição de senha
    private fun resetPassword() {
        val email = etEmail.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "Informe o e-mail para redefinição", Toast.LENGTH_SHORT).show()
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "E-mail de redefinição enviado!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro ao enviar e-mail: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // 4. Excluir a própria conta após logar
    private fun deleteUserAccount() {
        val user: FirebaseUser? = auth.currentUser

        if (user != null) {
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Conta excluída com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Erro ao excluir conta: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(this, "Nenhum usuário está logado no momento.", Toast.LENGTH_SHORT).show()
        }
    }
}