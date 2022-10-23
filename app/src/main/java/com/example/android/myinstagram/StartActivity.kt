package com.example.android.myinstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ActionMode
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    private lateinit var imageIcon:ImageView
    private lateinit var linearLayout: LinearLayout
    private lateinit var login:Button
    private lateinit var register:Button
    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        imageIcon=findViewById(R.id.icon_image)
        linearLayout=findViewById(R.id.linearLayout)
        login=findViewById(R.id.login)
        register=findViewById(R.id.register)
        mAuth= FirebaseAuth.getInstance()
        login.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        register.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        linearLayout.animate().alpha(0f).duration = 1
        val animation = TranslateAnimation(0F,0F,0F,-2000F)
        animation.duration=2000
        animation.fillAfter=false
        animation.setAnimationListener(MyAnimationListner())
        imageIcon.animation=animation

    }

    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java));
            finish();
        }
    }


    inner class MyAnimationListner: Animation.AnimationListener{
        override fun onAnimationStart(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
            imageIcon.clearAnimation()
            imageIcon.visibility= View.INVISIBLE
            linearLayout.visibility=View.VISIBLE
            linearLayout.animate().alpha(1f).duration = 1000
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }

    }


}