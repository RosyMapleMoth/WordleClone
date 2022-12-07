package com.example.codepathkotlinunit1

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.widget.Button
import android.view.animation.AnimationUtils
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    var inputString = arrayOf("","","","","")

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn = findViewById<Button>(R.id.btnTest)



        btn.setOnClickListener {
            val animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.foldout)
            val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.foldin)
            val test = findViewById<TextView>(R.id.g1)

            animationFadeIn.setAnimationListener(object : Animation.AnimationListener {

                override fun onAnimationEnd(p0: Animation?) {
                    test.startAnimation(animationFadeOut)
                }

                override fun onAnimationStart(p0: Animation?) {

                }

                override fun onAnimationRepeat(p0: Animation?) {

                }
            })
            test.startAnimation(animationFadeIn)
        }
    }
}