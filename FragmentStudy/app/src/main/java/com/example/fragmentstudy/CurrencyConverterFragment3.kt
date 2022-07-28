package com.example.fragmentstudy

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import java.lang.Math.round
// 프래그먼트에서 값을 반환 가능
class CurrencyConverterFragment3: Fragment() {

    interface CurrencyCalculationListener{
        fun onCalculate(result: Double,
                        amount: Double,
                        from: String,
                        to: String)
    }

    lateinit var listener: CurrencyCalculationListener

    override fun onAttach(context: Context) { // onAttach => 액티비티에 붙을 때 호출됨
        super.onAttach(context)

        if(activity is CurrencyCalculationListener){ // 액티비티가 CurrencyCalculationListener를 구현하고 있느냐
            listener = activity as CurrencyCalculationListener // 액티비티의 타입을 as 뒤에 있는 걸로 바꾸겠다
        }else{
            throw Exception("CurrencyCalculationListener 미구현")
        }
    }

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

    lateinit var fromCurrency: String
    lateinit var toCurrency: String

    override fun onCreateView( // 이게 사실상 액티비티의 onCreate 메서드
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate( // XML 파일을 동적으로 View로 만들어줌
            R.layout.currency_converter_fragment3, // 파일을 들고와 코드로 바꿈(실질적으로 바꿔야할 건 얘 밖에 없음)
            container, // 어디에 붙을 것인 지 결정(그냥 컨테이너(=부모 뷰)하면 됨)
            false // Fragment도 안에서 자체적으로 붙여주기 떄문에 코드 실행 시 붙을 필요없음
        )

        // Fragment는 앞에 view를 붙여줘야 함
        val calculateBtn = view.findViewById<Button>(R.id.calculate)
        val amount = view.findViewById<EditText>(R.id.amount)
        val exchangeType = view.findViewById<TextView>(R.id.exchange_type)

        fromCurrency = arguments?.getString("from", "USD")!!
        toCurrency = arguments?.getString("to", "USD")!!

        exchangeType.text = "${fromCurrency} ➪${toCurrency}︎"

        calculateBtn.setOnClickListener {
            if(amount.text.toString().isBlank()) {
                Toast.makeText(activity, "값을 입력하세요.", Toast.LENGTH_LONG).show()
            } else {
                var result = calculateCurrency(
                    amount.text.toString().toDouble(),
                    fromCurrency,
                    toCurrency
                )

                // TODO : result 값을 액티비티로 전달
                // 이걸 메인 액티비티에 정의 -> 값이 전달됨
                listener.onCalculate(
                    result,
                    amount.text.toString().toDouble(),
                    fromCurrency,
                    toCurrency
                )
            }

        }

        return view
    }

    companion object{ // 정적(=static, class) 메서드, 속성 (객체 없이 호출 가능)
        // 암묵적인 룰로 newInstance로 함
        fun newInstance(from: String, to: String): CurrencyConverterFragment3{
            val fragment = CurrencyConverterFragment3()

            // 번들 객체를 만들고 필요한 데이터 저장
            // 프래그먼트를 죽었다 다시 만들 때 번들에 있는 걸 쓸 수 있음(번들은 안 없어짐)
            val args = Bundle()
            //키, 값으로 저장
            args.putString("from", from)
            args.putString("to", to)
            fragment.arguments = args

            return fragment
        }
    }
}