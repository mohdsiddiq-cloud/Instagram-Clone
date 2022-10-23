package com.example.android.myinstagram

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var username:EditText
    private lateinit var name:EditText
    private lateinit var email: EditText
    private lateinit var password:EditText
    private lateinit var register:Button
    private lateinit var login:TextView
    private lateinit var pd:ProgressDialog
    private lateinit var auth:FirebaseAuth
    private lateinit var mrootRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        username=findViewById(R.id.username)
        name=findViewById(R.id.name)
        email=findViewById(R.id.email)
        password=findViewById(R.id.password)
        login=findViewById(R.id.login_user)
        register=findViewById(R.id.register1)
        pd= ProgressDialog(this)
        login.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        register.setOnClickListener {
            var txtusername:String= username.text.toString()
            var txtname:String=name.text.toString()
            var txtemail:String=email.text.toString()
            var txtpassword:String=password.text.toString()
            mrootRef= FirebaseDatabase.getInstance().getReference()
            auth= FirebaseAuth.getInstance()

            if(TextUtils.isEmpty(txtemail) || TextUtils.isEmpty(txtpassword) || TextUtils.isEmpty(txtname) || TextUtils.isEmpty(txtusername))
                Toast.makeText(this,"Empty credentials!",Toast.LENGTH_LONG).show()
            else if(txtpassword.length<6)
                Toast.makeText(this,"password too short!",Toast.LENGTH_LONG).show()
            else{
                registerUser(txtusername,txtname,txtemail,txtpassword);
            }
        }
    }

     fun registerUser(txtusername: String, txtname: String, txtemail: String, txtpassword: String) {
         pd.setMessage("please wait..")
         pd.show()
        auth.createUserWithEmailAndPassword(txtemail,txtpassword).addOnSuccessListener {
            var hm:HashMap<String,String> = HashMap<String,String>()
            hm.put("name",txtname)
            hm.put("email",txtemail)
            hm.put("username",txtusername)
            auth.currentUser?.let { it1 -> hm.put("id", it1.uid) }
            hm.put("bio","");
            hm.put("imageurl","default");
            auth.currentUser?.let { it1 -> mrootRef.child("Users").child(it1.uid).setValue(hm).addOnCompleteListener {
                if(it.isSuccessful) {
                    pd.dismiss()
                    Toast.makeText(
                        this,
                        "update the profile for better Experience!",
                        Toast.LENGTH_LONG
                    ).show()

                startActivity(Intent(this,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
                }
            }.addOnFailureListener {
                pd.dismiss()
                Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show()
            } }
        }
    }
}