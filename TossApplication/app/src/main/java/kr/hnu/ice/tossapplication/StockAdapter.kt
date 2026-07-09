package kr.hnu.ice.tossapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kr.hnu.ice.tossapplication.data.StockModel
import kr.hnu.ice.tossapplication.databinding.ItemStockBinding

class StockAdapter : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {
    private var items = listOf<StockModel>()

    fun updateItems(newItems: List<StockModel>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = items.size
            override fun getNewListSize(): Int = newItems.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos].code == newItems[newPos].code
            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean = items[oldPos] == newItems[newPos]
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

    class StockViewHolder(private val binding: ItemStockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StockModel) {
            binding.tvStockName.text = item.name
            binding.tvStockPrice.text = String.format("%,.0f원", item.currentPrice)
        }
    }
}