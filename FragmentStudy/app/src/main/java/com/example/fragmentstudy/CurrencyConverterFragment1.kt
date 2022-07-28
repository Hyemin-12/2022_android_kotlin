package com.example.fragmentstudy

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import java.lang.Math.round

//프래그먼트 만들고 필요한 기능 구현 가능

// Fragment => 작은 액티비티 느낌ㅁ
// Fragment는 주생성자는 만들면 안됨 -> 프래그먼트가 유실될 수 있는데,(생명주기 메서드)
// 그러면 프래그먼트를 다시 만드는 시점에 우리가 생성자를 호출하는게 아니기 때문에 안드로이드에서는 CurrencyConverterFragment1() 얘(=기본 생성자)만 호출
// CurrencyConverterFragment1("USD", "KRW")를 호춯 못함 <= 얘는 우리가 호출하기 때문에 가능
class CurrencyConverterFragment1: Fragment() {
    val currencyExchangeMap = mapOf( // 맵 -> 키, 값으로 구성있음
        "USD" to 1.0, // key to value
        "EUR" to 0.9,
        "JPY" to 110.0,
        "KRW" to 1150.0
    )

    fun calculateCurrency(amount: Double, from: String, to: String) : Double{
        var USDAmount = if(from != "USD") {
            (amount / currencyExchangeMap[from]!!)
        }else{
            amount
        }
        return currencyExchangeMap[to]!! *USDAmount
    }

    override fun onCreateView( // 이게 사실상 액티비티의 onCreate 메서드
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate( // XML 파일을 동적으로 View로 만들어줌
            R.layout.currency_converter_fragment1, // 파일을 들고와 코드로 바꿈(실질적으로 바꿔야할 건 얘 밖에 없음)
            container, // 어디에 붙을 것인 지 결정(그냥 컨테이너(=부모 뷰)하면 됨)
            false // Fragment도 안에서 자체적으로 붙여주기 떄문에 코드 실행 시 붙을 필요없음
        )

        // Fragment는 앞에 view를 붙여줘야 함
        val calculateBtn = view.findViewById<Button>(R.id.calculate)
        val amount = view.findViewById<EditText>(R.id.amount)
        val result = view.findViewById<TextView>(R.id.result)
        val fromCurrentSpinner = view.findViewById<Spinner>(R.id.from_currency)
        val toCurrentSpinner = view.findViewById<Spinner>(R.id.to_currency)


        // spinner를 쓸 때에는 초기화 필요
        val currencySelectionArrayAdapter = ArrayAdapter<String>(
            view.context,
            android.R.layout.simple_spinner_item,
            listOf("USD", "EUR", "JPY", "KRW") // 문자열 배열
        )
        currencySelectionArrayAdapter.setDropDownViewResource(
            // 안드로이드 기본 드롭다운 디자인 사용
            android.R.layout.simple_spinner_dropdown_item
        )

        //스피너에 어댑터 연결(내용이 같으므로 그냥 둘 다 똑같은거 연결)
        fromCurrentSpinner.adapter = currencySelectionArrayAdapter
        toCurrentSpinner.adapter = currencySelectionArrayAdapter

//        amount.doAfterTextChanged {
//            result.text = calculateCurrency(
//                amount.text.toString().toDouble(),
//                fromCurrentSpinner.selectedItem.toString(),
//                toCurrentSpinner.selectedItem.toString()
//            ).toString()
//        }

        //object : => 익명클래스
        val itemSelectedListner = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //calculateCurrency 호출
                result.text = calculateCurrency(
                    amount.text.toString().toDouble(),
                    fromCurrentSpinner.selectedItem.toString(),
                    toCurrentSpinner.selectedItem.toString()
                ).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        fromCurrentSpinner.onItemSelectedListener = itemSelectedListner
        toCurrentSpinner.onItemSelectedListener = itemSelectedListner

        calculateBtn.setOnClickListener{
            result.text = calculateCurrency(
                amount.text.toString().toDouble(),
                fromCurrentSpinner.selectedItem.toString(),
                toCurrentSpinner.selectedItem.toString()
            ).toString()
        }

        return view
    }
}