package com.example.lotteryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LotteryAdapter(private val dataList: List<String>):
    RecyclerView.Adapter<LotteryAdapter.LottoItemViewHolder>() {
    class LottoItemViewHolder(val view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LottoItemViewHolder {
        // 한 항복을 표시할 레이아웃 관련 뷰를 만들어준다
        // (viewType 값이 바로 getItemViewType에서 반환한 레이아웃 리소스 식별자)
        val view = LayoutInflater.from(parent.context) // parent 뷰그룹으로 RecyclerView이다
            .inflate(viewType, parent, false) // viewType는 XML이며, parent를 RecyclerView에 붙일 것이다

        return LottoItemViewHolder(view)
    }

    //onCreate의 내용이 바뀔 때 이게 실행됨
    override fun onBindViewHolder(holder: LottoItemViewHolder, position: Int) {
        val numbers = dataList[position]
        holder.view.findViewById<TextView>(R.id.lotto_nums).text = numbers // 내용을 바꿔줌
    }

    override fun getItemCount() = dataList.size

    // 앞에서 LottoItemViewHolder 클래스에 아무것도 없으니까 여기다가 getItemViewType에서 레이아웃 식별자를 돌려줌
    override fun getItemViewType(position: Int) = R.layout.list_item // 항목 하나하나 보여줌
}