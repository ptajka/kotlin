package com.example.memorina

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var gameBoard: Array<Int> = arrayOf()
    private val BOARD_SIZE: Int = 4
    private var lastCard: Int = -1
    private lateinit var lastIm: ImageView
    private var flagAnswer = false
    private var guessedPairs = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL

        createBoard()
        gameBoard.shuffle()

        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.weight = 1.toFloat() // единичный вес

            val catViews = ArrayList<ImageView>()
            for (i in 1..BOARD_SIZE*BOARD_SIZE) {
                catViews.add( // вызываем конструктор для создания нового ImageView
                    ImageView(applicationContext).apply {
                        setImageResource(R.drawable.squarecat)
                        layoutParams = params
                        tag = "p$cat"
                        setOnClickListener(colorListener)
                    })
            }

            val rows = Array(BOARD_SIZE, { LinearLayout(applicationContext)})

        for((rowCount, view) in catViews.withIndex()) {
            val row: Int = rowCount / BOARD_SIZE
            rows[row].addView(view)
        }
        for (row in rows) {
            layout.addView(row)
        }

        setContentView(layout)
    }


    private fun createBoard(){
        for (i in 1.. BOARD_SIZE*BOARD_SIZE/2){
            gameBoard += i
            gameBoard += i
        }
    }


    suspend fun setBackgroundWithDelay(iv: ImageView) {
        val cardTag = iv.tag.toString().filter { it.isDigit() }.toInt()-1
        var resourceId = resources.getIdentifier("cat${gameBoard[cardTag]}", "drawable", packageName)
        Log.e("Tag", "$cardTag")


        if (!flagAnswer ) {
            if (lastCard < 0) {
                iv.setImageResource(resourceId)
                lastCard = gameBoard[cardTag]
                lastIm = iv
                iv.isClickable = false
            } else {
                flagAnswer = true
                iv.setImageResource(resourceId)
                if (lastCard == gameBoard[cardTag]) {
                    delay(200)
                    lastIm.visibility = View.INVISIBLE
                    iv.visibility = View.INVISIBLE
                    iv.isClickable = false
                    lastCard = -1
                    guessedPairs += 1
                } else {
                    delay(500)
                    iv.setImageResource(R.drawable.squarecat)
                    lastIm.setImageResource(R.drawable.squarecat)

                    iv.isClickable = true
                    lastIm.isClickable = true

                    lastCard = -1
                }
            }
            flagAnswer = false
        }

        if (guessedPairs == gameBoard.size/2){
            Toast.makeText(this, "Победа!", Toast.LENGTH_LONG).show()
        }

    }


    // обработчик нажатия на кнопку
    val colorListener = View.OnClickListener() {
        // запуск функции в фоновом потоке
        GlobalScope.launch (Dispatchers.Main)
        { setBackgroundWithDelay(it as ImageView) }
    }
}