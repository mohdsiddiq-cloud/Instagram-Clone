package com.example.android.myinstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var email:EditText
    lateinit var password:EditText
    lateinit var register:TextView
    lateinit var login:Button
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email=findViewById(R.id.email1)
        password=findViewById(R.id.password1);
        register=findViewById(R.id.register_user)
        login=findViewById(R.id.login1)
        auth= FirebaseAuth.getInstance()
        register.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
        login.setOnClickListener {
            var eml_txt:String=email.text.toString()
            var psw_txt:String=password.text.toString()
            if(TextUtils.isEmpty(eml_txt) || TextUtils.isEmpty(psw_txt)){
                Toast.makeText(this,"Empty Credentials!",Toast.LENGTH_LONG).show()
            }
            else{
                loginUser(eml_txt,psw_txt);
            }
        }
    }

    private fun loginUser(emlTxt: String, pswTxt: String) {
        auth.signInWithEmailAndPassword(emlTxt,pswTxt).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(
                    this,
                    "update the profile for better Experience!",
                    Toast.LENGTH_LONG
                ).show()
            startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"check your email and passsword",Toast.LENGTH_LONG).show()
        }
    }
}