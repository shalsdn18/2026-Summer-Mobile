package kr.hnu.ice.tossapplication.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.hnu.ice.tossapplication.databinding.ItemOrderHistoryBinding
import kr.hnu.ice.tossapplication.networking.OrderHistoryItem

class OrderHistoryAdapter : ListAdapter<OrderHistoryItem, OrderHistoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderHistoryItem) {
            binding.tvOrderDate.text = item.orderedAt
            binding.tvSymbol.text = item.symbol
            
            val isBuy = item.side == "BUY"
            binding.tvOrderSide.text = if (isBuy) "매수" else "매도"
            binding.tvOrderSide.setTextColor(if (isBuy) 0xFFF04452.toInt() else 0xFF3182F6.toInt())
            
            binding.tvOrderStatus.text = when (item.status) {
                "PENDING" -> "대기 중"
                "FILLED" -> "체결완료"
                "PARTIAL_FILLED" -> "부분체결"
                "CANCELED" -> "취소됨"
                else -> item.status
            }
            
            val price = item.price ?: "시장가"
            binding.tvOrderDetails.text = "$price · ${item.quantity}주"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<OrderHistoryItem>() {
        override fun areItemsTheSame(oldItem: OrderHistoryItem, newItem: OrderHistoryItem): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: OrderHistoryItem, newItem: OrderHistoryItem): Boolean {
            return oldItem == newItem
        }
    }
}
