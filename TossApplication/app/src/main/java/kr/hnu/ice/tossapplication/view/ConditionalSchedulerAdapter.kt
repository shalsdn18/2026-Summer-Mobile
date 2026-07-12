package kr.hnu.ice.tossapplication.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.hnu.ice.tossapplication.databinding.ItemConditionalSchedulerBinding
import kr.hnu.ice.tossapplication.networking.ConditionalOrderHistoryItem

class ConditionalSchedulerAdapter(
    private val onCancelClick: (String) -> Unit
) : ListAdapter<ConditionalOrderHistoryItem, ConditionalSchedulerAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConditionalSchedulerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemConditionalSchedulerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ConditionalOrderHistoryItem) {
            binding.tvSymbol.text = item.symbol
            binding.tvType.text = item.type
            binding.tvStatus.text = when (item.status) {
                "WATCHING" -> "감시 중"
                "COMPLETED" -> "완료됨"
                "CANCELED" -> "해제됨"
                else -> item.status
            }
            
            val firstCond = "1: ${item.first.triggerPrice}원 ${if (item.first.type == "STOP") "이상" else ""} ${item.first.type}"
            val secondCond = item.second?.let {
                "\n2: ${it.triggerPrice}원 ${if (it.type == "STOP") "이상" else ""} ${it.type}"
            } ?: ""
            
            binding.tvConditions.text = "$firstCond$secondCond"
            
            binding.btnCancel.setOnClickListener {
                onCancelClick(item.conditionalOrderId)
            }
            
            // 상태가 감시 중일 때만 해제 버튼 활성화
            binding.btnCancel.isEnabled = item.status == "WATCHING"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ConditionalOrderHistoryItem>() {
        override fun areItemsTheSame(oldItem: ConditionalOrderHistoryItem, newItem: ConditionalOrderHistoryItem): Boolean {
            return oldItem.conditionalOrderId == newItem.conditionalOrderId
        }

        override fun areContentsTheSame(oldItem: ConditionalOrderHistoryItem, newItem: ConditionalOrderHistoryItem): Boolean {
            return oldItem == newItem
        }
    }
}
