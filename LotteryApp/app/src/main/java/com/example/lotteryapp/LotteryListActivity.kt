package com.example.lotteryapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LotteryListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lottery_list_activity)

        val pref = getSharedPreferences("nums", Context.MODE_PRIVATE)
        // 준비물 1 (그냥 앞에꺼 가져오면 됨)
        var lottoNums: String? = pref.getString("lottonums", "") // 키로 저장된 것이 없으면 두번째 인자로 대체함

        var numList = if(lottoNums == ""){
            mutableListOf<String>()  // 빈 문자열이면 빈 리스트 리턴
        }else {
            lottoNums!!.split(",").toMutableList() // 번호가 있으면 문자열로 저장되있던 게 ","으로 연결되어 mutableList로 옴
        }

        //준비물 3
        val layoutManager = LinearLayoutManager(this)
        val lottoList = findViewById<RecyclerView>(R.id.lotto_list)

        //준비물 4,5
        val adapter = LotteryAdapter(numList)
        lottoList.setHasFixedSize(false)
        lottoList.layoutManager = layoutManager
        lottoList.adapter = adapter
    }
}