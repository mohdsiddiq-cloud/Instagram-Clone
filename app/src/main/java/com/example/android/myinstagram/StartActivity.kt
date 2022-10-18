package com.example.android.myinstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout

class StartActivity : AppCompatActivity() {
    lateinit var imageIcon:ImageView
    lateinit var linearLayout: LinearLayout
    lateinit var login:Button
    lateinit var register:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        imageIcon=findViewById(R.id.icon_image)
        linearLayout=findViewById(R.id.linearLayout)
        login=findViewById(R.id.login)
        register=findViewById(R.id.register)

        linearLayout.animate().alpha(0f).setDuration(1)
        val animation = TranslateAnimation(0F,0F,0F,-2000F)
        animation.duration=2000
        animation.fillAfter=false
        animation.setAnimationListener(MyAnimationListner())
        imageIcon.animation=animation
    }

    inner class MyAnimationListner: Animation.AnimationListener{
        override fun onAnimationStart(p0: Animation?) {

        }

        override fun onAnimationEnd(p0: Animation?) {
            imageIcon.clearAnimation()
            imageIcon.visibility= View.INVISIBLE
            linearLayout.visibility=View.VISIBLE
            linearLayout.animate().alpha(1f).setDuration(1000)
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }

    }
}