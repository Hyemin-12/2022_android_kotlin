package com.example.fragmentstudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity(),
    // 얘를 구현해줘야 함(아니면 CurrencyConverterFragment3에서 if문에서 걸림)
    CurrencyConverterFragment3.CurrencyCalculationListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // supportFragmentManager => 프래그먼트에 삽입, 삭제 등을 관리하는 거
        // beginTransaction => 커밋이 호출되기 전까지 프래그먼트를 교체, 추가 등을 할 수 있다.
        // add(프래그먼트를 붙일 장소, 전달할 프래그먼트) => 프래그먼트 추가
        val transaction = supportFragmentManager.beginTransaction()
//        transaction.add(R.id.fragment_container, CurrencyConverterFragment1())
        transaction.add(R.id.fragment_container, CurrencyConverterFragment2.newInstance("USD", "KRW"))
        transaction.add(R.id.fragment_container, CurrencyConverterFragment2.newInstance("KRW", "USD"))
//        transaction.add(R.id.fragment_container, CurrencyConverterFragment3.newInstance("USD", "KRW"))
        transaction.commit()
    }

    override fun onCalculate(result: Double, amount: Double, from: String, to: String) {
        Log.d("my_Tag", result.toString())
        findViewById<TextView>(R.id.result).text = result.toString()
    }
}