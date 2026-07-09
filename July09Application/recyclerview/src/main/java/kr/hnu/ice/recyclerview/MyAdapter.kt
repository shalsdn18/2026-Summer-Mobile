package kr.hnu.ice.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kr.hnu.ice.recyclerview.databinding.ItemMainBinding

class MyAdapter(val datas: List<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private val mDatas = datas

    class MyViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // 1. 텍스트 데이터 바인딩
        holder.binding.itemData.text = mDatas[position]

        // 2. 이미지 레이아웃의 itemRoot(레이아웃 최상단 루트) 클릭 이벤트 구현
        holder.binding.itemData.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Clicked: ${mDatas[position]}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }
}