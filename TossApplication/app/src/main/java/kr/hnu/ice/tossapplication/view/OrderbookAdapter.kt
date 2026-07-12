package kr.hnu.ice.tossapplication.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.hnu.ice.tossapplication.databinding.ItemOrderbookBinding
import kr.hnu.ice.tossapplication.networking.OrderbookEntry

class OrderbookAdapter : RecyclerView.Adapter<OrderbookAdapter.ViewHolder>() {
    private var asks: List<OrderbookEntry> = emptyList()
    private var bids: List<OrderbookEntry> = emptyList()

    fun updateData(newAsks: List<OrderbookEntry>, newBids: List<OrderbookEntry>) {
        // 매도호가(Asks)는 보통 높은 가격부터 아래로 내려오게 배치 (상단)
        // 매수호가(Bids)는 그 아래에 배치
        this.asks = newAsks.sortedByDescending { it.price.toDoubleOrNull() ?: 0.0 }
        this.bids = newBids.sortedByDescending { it.price.toDoubleOrNull() ?: 0.0 }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = asks.size + bids.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderbookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < asks.size) {
            holder.bind(asks[position], isAsk = true)
        } else {
            holder.bind(bids[position - asks.size], isAsk = false)
        }
    }

    class ViewHolder(private val binding: ItemOrderbookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entry: OrderbookEntry, isAsk: Boolean) {
            binding.tvPrice.text = entry.price
            binding.tvVolume.text = entry.volume
            
            // 토스 스타일: 매도는 파란색 계열, 매수는 빨간색 계열 (혹은 그 반대일 수 있으나 일반적으론 매도/매수 색상 구분)
            val color = if (isAsk) 0xFF3182F6.toInt() else 0xFFF04452.toInt()
            binding.tvPrice.setTextColor(color)
            binding.viewVolumeBar.setBackgroundColor(color)
            
            // 배경색 살짝 투명하게 처리하여 호가 잔량 시각화
            binding.layoutOrderbookItem.setBackgroundColor(if (isAsk) 0x1A3182F6 else 0x1AF04452)
        }
    }
}
