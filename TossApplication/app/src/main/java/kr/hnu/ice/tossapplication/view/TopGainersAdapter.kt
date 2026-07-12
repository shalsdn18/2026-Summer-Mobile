package kr.hnu.ice.tossapplication.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.hnu.ice.tossapplication.databinding.ItemTopGainerBinding
import kr.hnu.ice.tossapplication.networking.RankingItem

class TopGainersAdapter : ListAdapter<RankingItem, TopGainersAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopGainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemTopGainerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RankingItem) {
            binding.tvRank.text = item.rank.toString()
            binding.tvSymbol.text = item.symbol
            binding.tvPrice.text = "${item.price.lastPrice}원"
            
            val rate = item.price.changeRate.toDoubleOrNull() ?: 0.0
            val prefix = if (rate > 0) "+" else ""
            binding.tvChangeRate.text = "$prefix${item.price.changeRate}%"
            
            val color = if (rate > 0) 0xFFF04452.toInt() else if (rate < 0) 0xFF3182F6.toInt() else 0xFF191F28.toInt()
            binding.tvChangeRate.setTextColor(color)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RankingItem>() {
        override fun areItemsTheSame(oldItem: RankingItem, newItem: RankingItem): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: RankingItem, newItem: RankingItem): Boolean {
            return oldItem == newItem
        }
    }
}
