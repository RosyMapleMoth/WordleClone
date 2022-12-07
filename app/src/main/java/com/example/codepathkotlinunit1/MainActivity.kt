package com.example.codepathkotlinunit1

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.sql.Time
import java.time.Clock
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    var inputString : Array<String> = arrayOf("","","","","")
    var LetterOpen = 0
    var curGuess = 0
    var word : Array<String> = arrayOf("A","B","C","D","E")
    var inputVisual : Array<TextView?> = Array(5) { null }
    var attemptVisual : Array<TextView?> = Array(5) { null }
    var attemptRows: Array<ViewGroup?> = Array(5) { null }
    var validWords: Array<String> = Array(12972) { "" }
    var wordleWords: Array<String> = Array(2315) { "" }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn = findViewById<Button>(R.id.btnA)




        // You can of course remove the .withCSVParser part if you use the default separator instead of ;
        val assManager = applicationContext.assets
        val ValidWordsInput: InputStream = assManager.open("valid-words.csv")
        val ValidWordsreader = BufferedReader(InputStreamReader(ValidWordsInput))
        var count = 0;

        while (ValidWordsreader.ready())
        {
            validWords[count] = ValidWordsreader.readLine()
            Log.i("loading", "added " + validWords[count])
            count++
        }

        val inputStream = assManager.open("word-bank.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        count = 0;
        while (reader.ready())
        {
            wordleWords[count] = reader.readLine()
            Log.i("loading", "added " + validWords[count])
            count++
        }

        selectWord()
        attemptRows[0] = findViewById<TextView>(R.id.AttemptOne) as ViewGroup
        attemptRows[1] = findViewById<TextView>(R.id.AttemptTwo) as ViewGroup
        attemptRows[2] = findViewById<TextView>(R.id.AttemptThree) as ViewGroup
        attemptRows[3] = findViewById<TextView>(R.id.AttemptFour) as ViewGroup
        attemptRows[4] = findViewById<TextView>(R.id.AttemptFive) as ViewGroup


        var guessRow = findViewById<LinearLayout>(R.id.Guess)


        inputVisual = grabViews(guessRow as ViewGroup) as Array<TextView?>
        attemptVisual = grabViews(attemptRows[0] as ViewGroup) as Array<TextView?>

    }

    fun inputLetter(string: String)
    {
        // escape if we already have a full test word
        if (LetterOpen > 4)
        {
            return
        }
        inputString[LetterOpen] = string
        inputVisual[LetterOpen]?.text = inputString[LetterOpen]
        LetterOpen++
    }

    fun inputLetter(view: View)
    {
        // escape if we already have a full test word
        if (LetterOpen > 4)
        {
            return
        }

        var txtView = findViewById<TextView>(view.id)
        inputString[LetterOpen] = txtView.text.toString()
        inputVisual[LetterOpen]?.text = inputString[LetterOpen]
        LetterOpen++
    }

    fun removeLetter()
    {
        if (LetterOpen < 1)
        {
            return
        }

        LetterOpen--
        inputString[LetterOpen] = ""
        inputVisual[LetterOpen]?.text = inputString[LetterOpen]
    }

    fun removeLetter(view: View)
    {
        if (LetterOpen < 1)
        {
            return
        }

        LetterOpen--
        inputString[LetterOpen] = ""
        inputVisual[LetterOpen]?.text = inputString[LetterOpen]
    }

    fun tryAttempt(view: View)
    {
        if (LetterOpen <= 4 )
        {
            Toast.makeText(this,"only 5 letter words are acceptable",Toast.LENGTH_SHORT)
            return
        }
        if (curGuess > 4)
        {
            Toast.makeText(this,"you only have 5 guesses",Toast.LENGTH_SHORT)
            return
        }
        if (!validWords.contains(inputString.joinToString(separator = "").lowercase()))
        {
            Log.i("attempt", "does not contain " + inputString.joinToString(separator = ""))
            return
        }

        //TODO add animation or feedback
        attmept()
    }

    private fun attmept()
    {
        var thisInput = inputString.clone()
        var thisAttmept = attemptVisual.clone()
        for (i in 0..4 )
        {
            val animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.foldout)
            val animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.foldin)

            if (thisInput[i] == word[i])
            {
                animationFadeIn.setAnimationListener(object : Animation.AnimationListener {

                    override fun onAnimationEnd(p0: Animation?) {
                        thisAttmept[i]?.text = thisInput[i]
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            thisAttmept[i]?.setBackgroundColor(getColor(R.color.correct))
                        }
                        else
                        {
                            thisAttmept[i]?.setBackgroundColor(resources.getColor(R.color.correct))
                        }
                        thisAttmept[i]?.startAnimation(animationFadeOut)
                    }

                    override fun onAnimationStart(p0: Animation?) {

                    }

                    override fun onAnimationRepeat(p0: Animation?) {

                    }
                })
                thisAttmept[i]?.startAnimation(animationFadeIn)
            }
            else if (word.contains(thisInput[i]))
            {
                animationFadeIn.setAnimationListener(object : Animation.AnimationListener {

                    override fun onAnimationEnd(p0: Animation?) {
                        thisAttmept[i]?.text = thisInput[i]
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            thisAttmept[i]?.setBackgroundColor(getColor(R.color.close))
                        }
                        else
                        {
                            thisAttmept[i]?.setBackgroundColor(resources.getColor(R.color.close))
                        }
                        thisAttmept[i]?.startAnimation(animationFadeOut)
                    }

                    override fun onAnimationStart(p0: Animation?) {

                    }

                    override fun onAnimationRepeat(p0: Animation?) {

                    }
                })
                thisAttmept[i]?.startAnimation(animationFadeIn)
            }
            else
            {
                animationFadeIn.setAnimationListener(object : Animation.AnimationListener {

                    override fun onAnimationEnd(p0: Animation?) {
                        thisAttmept[i]?.text = thisInput[i]
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            thisAttmept[i]?.setBackgroundColor(getColor(R.color.incorrect))
                        }
                        else
                        {
                            thisAttmept[i]?.setBackgroundColor(resources.getColor(R.color.incorrect))
                        }
                        thisAttmept[i]?.startAnimation(animationFadeOut)
                    }

                    override fun onAnimationStart(p0: Animation?) {

                    }

                    override fun onAnimationRepeat(p0: Animation?) {

                    }
                })
                thisAttmept[i]?.startAnimation(animationFadeIn)
            }
        }
        curGuess++
        if (curGuess < 5)
        {
            attemptVisual = grabViews(attemptRows[curGuess] as ViewGroup)
        }
        removeLetter()
        removeLetter()
        removeLetter()
        removeLetter()
        removeLetter()
    }




    fun grabViews(viewGroup: ViewGroup): Array<TextView?> {
        var output: Array<TextView?> = Array(5) { null }

        for (i in 0 until viewGroup.childCount)
        {
            output[i] = viewGroup.getChildAt(i) as TextView?
        }

        return output
    }

    fun selectWord()
    {

        val rnds = (0..2315).random(Random(Date().time))

        val selectedWord = wordleWords[rnds]
        Log.i("select", selectedWord.uppercase() + " is now the word to guess")
        word[0] = selectedWord.get(0).toString().uppercase()
        word[1] = selectedWord.get(1).toString().uppercase()
        word[2] = selectedWord.get(2).toString().uppercase()
        word[3] = selectedWord.get(3).toString().uppercase()
        word[4] = selectedWord.get(4).toString().uppercase()
    }
}