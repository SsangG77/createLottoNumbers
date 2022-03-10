package com.sangjin.lotto

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.util.*
import kotlin.math.min
import kotlin.random.Random.Default.nextInt


class MainActivity : AppCompatActivity() {

    val TAG: String = "로그"

    private val percentage by lazy {
        findViewById<TextView>(R.id.percentage)
    }

    private val randomBtn by lazy {
        findViewById<Button>(R.id.randomBtn)
    }

    private val addBtn by lazy {
        findViewById<Button>(R.id.addNumber)
    }

    private val numberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private val clearBtn by lazy {
        findViewById<Button>(R.id.clearBtn)
    }

    private val resultButton by lazy {
        findViewById<Button>(R.id.percentageBtn)
    }

    private val webBtn by lazy {
        findViewById<Button>(R.id.webBtn)
    }

    private val pickerNumberList = mutableSetOf<Int>()

    private var didRun = false

    private val numberTextViewList: List<TextView> by lazy {
        listOf(
            findViewById(R.id.firstNumber),
            findViewById(R.id.secondsNumber),
            findViewById(R.id.thirdNumber),
            findViewById(R.id.fourthNumber),
            findViewById(R.id.fifthNumber),
            findViewById(R.id.sixthNumber)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.apply {
            minValue = 1
            maxValue = 45
        }
        initRunButton()
        initAddButton()
        initClearButton()
        percentageNumber()

        webBtn.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://dhlottery.co.kr/gameResult.do?method=byWin"))
            startActivity(intent)
        }
    }

    private fun initRunButton() {
        randomBtn.setOnClickListener {  //6개의 숫자를 생성해서 리스트에 넣는다. 그리고 그 리스트의 값들을 각각의 텍스트뷰에 넣는다.
            val list = getRandomNumber()
            didRun = true
            list.forEachIndexed { index, number ->
                numberTextViewList[index].text = number.toString()
                numberTextViewList[index].isVisible = true
                setBackground(number, numberTextViewList[index])
            }
        }
    }

    private fun initClearButton() {
        clearBtn.setOnClickListener {
            pickerNumberList.clear()
            numberTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
            percentage.isVisible = false
        }
    }

    private fun initAddButton() { //번호추가 버튼을 누르면 리스트에 번호들을 추가하고 그 값들을 텍스트뷰의 텍스트값으로 넣어준다.
        addBtn.setOnClickListener {

            if (didRun) {
                Toast.makeText(this, "초기화 후 실행해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickerNumberList.size >= 7) {
                Toast.makeText(this, "번호를 더이상 추가할수 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickerNumberList.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 추가된 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickerNumberList.size]
            textView.isVisible = true
            val numberPickerValue = numberPicker.value
            textView.text = numberPickerValue.toString()
            setBackground(numberPickerValue, textView)

            pickerNumberList.add(numberPicker.value)
        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>() //빈 수정가능한 리스트를 생성한다.
        for (i in 1..45) { ///1에서 45의 숫자들을 리스트에 넣는다.
            numberList.add(i)
        }
        numberList.shuffle() //리스트의 값들을 무작위로 섞는다.
        val newList = pickerNumberList.toList() + numberList.subList(
            0,
            6 - pickerNumberList.size
        ) //무작위로 섞인 값들에서 앞에서 6번째까지의 값만 새로운 리스트에 넣는다.
        return newList.sorted() //그 새로운 리스트를 반환한다.
    }

    private fun setBackground(number: Int, textView: TextView) {
        when (number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 11..20 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_green)
            in 21..30 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray)
            in 31..40 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 41..45 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow)
        }
    }

    private fun percentageNumber() {
        val random = Random().nextInt(10000).toDouble()
        val randomDouble = random / 100
        resultButton.setOnClickListener {
            percentage.text = "당첨될 확률: $randomDouble %"
            percentage.isVisible = true
        }
    }
}



