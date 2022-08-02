package com.example.lotteryapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var currentNums: String // 로또 번호 만들 때마다 여기에 저장(클래스의 속성 -> 나중에 접근하기 위함)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        //SharedPreferences 객체를 가져오기
        val pref = getSharedPreferences("nums", Context.MODE_PRIVATE)

        val lottoNumView = findViewById<TextView>(R.id.lotto_num)

        currentNums = generateRandomLottoNum(6, "-")
        lottoNumView.text = currentNums

        val generateButton = findViewById<Button>(R.id.generate)
        generateButton.setOnClickListener{
            currentNums = generateRandomLottoNum(6, "-") //로또 번호를 현재 로또 번호에 저장함(나중에 접근하기 위해서 저장)
            lottoNumView.text = currentNums
        }

        val saveButton = findViewById<Button>(R.id.save)
        saveButton.setOnClickListener{
            // 처음에는 프리퍼런스에서 "lottonums" 키를 통해서 문자열 값을 가져옴
            // 맨 처음에는 저장된 게 없으므로 두 번째 인자값인 빈 문자열을 가져옴
            // 그 이후 if, else 표현식을 통해 빈 문자열인 경우에는 빈 리스트를 하나 생성하도록 하고, 그 리스트에 현재 로또 번호 저장
            // 이 다음으로는 저장된 번호가 있으므로 로또 번호 리턴 -> else에서 로또 번호 리스트 반환
            // 저장할 때마다 joinToString을 통해 문자열로 저장함 -> 실제 저장되는 내용 "1-2-3-4-5-6,2-4-6-8-10-12"

            var lottoNums: String? = pref.getString("lottonums", "") // 키로 저장된 것이 없으면 두번째 인자로 대체함

            var numList = if(lottoNums == ""){
                mutableListOf<String>()  // 빈 문자열이면 빈 리스트 리턴
            }else {
                lottoNums!!.split(",").toMutableList() // 번호가 있으면 문자열로 저장되있던 게 ","으로 연결되어 mutableList로 옴
            }
            numList.add(currentNums) // 현재 로또 번호 추가

            Log.d("my_tag", numList.toString()) // 저장 확인

            val editor = pref.edit()
            editor.putString("lottonums", numList.joinToString(",")) // numList를 문자열로 바꿔서 저장(프리퍼런스에 리스트를 저장 못하기 떄문)
            editor.apply()
        }

        val listButton = findViewById<Button>(R.id.list)
        listButton.setOnClickListener{
            val intent = Intent(this, com.example.lotteryapp.LotteryListActivity::class.java)

            startActivity(intent)
        }

        val checkButton = findViewById<TextView>(R.id.check)
        checkButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://m.dhlottery.co.kr/gameResult.do?method=byWin&wiselog=M_A_1_8"))
            startActivity(intent)
        }
    }

    // 수를 몇 개 넘길 것인지 -> cnt, 어떻게 연결할 것인지 -> sep, 중복을 허용할 것인지 -> bool(나중에 구현해보기)
    fun generateRandomLottoNum(cnt: Int = 6, sep: String = "-", bool: Boolean = false): String{
        // nums에 아무것도 없어서 타입 추론 불가 -> 타입을 써줌(mutableSetOf<Int>())
        // var nums = mutableSetOf<Int>()
        // val numsList = nums.toMutableList()

        //중복 허용 여부
        return if(bool) {
            val numsList = mutableListOf<Int>()
            // 랜덤 함수(6번 반복)
            while(numsList.size < cnt) {
                numsList.add((1..45).random())
            }
            numsList.sorted().joinToString(sep)
        }else{
            var numsSet = mutableSetOf<Int>()
            while(numsSet.size < cnt) {
                numsSet.add((1..45).random())
            }
            numsSet.sorted().joinToString(sep)
        }
    }
}