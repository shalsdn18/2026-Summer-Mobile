package kr.hnu.ice.tossapplication.view

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kr.hnu.ice.tossapplication.databinding.ItemHoldingStockBinding
import kr.hnu.ice.tossapplication.viewmodel.EnrichedHoldingStock
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

class HoldingStockAdapter(
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<HoldingStockAdapter.StockViewHolder>() {

    private val items = mutableListOf<EnrichedHoldingStock>()

    fun submitList(newItems: List<EnrichedHoldingStock>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = ItemHoldingStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class StockViewHolder(private val binding: ItemHoldingStockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EnrichedHoldingStock) {
            binding.tvStockName.text = item.name
            binding.tvStockCount.text = "${item.quantity}주"
            
            val exchangeRate = 1504.80
            val rawPrice = item.evaluationAmountKrw.replace(",", "").toDoubleOrNull() ?: 0.0
            val rawProfit = item.profitLossAmount.replace(",", "").toDoubleOrNull() ?: 0.0

            // [개선] 해외주식도 원화 환산 처리하여 실화면과 토글 정합성 매핑
            val finalPriceKrw = if (item.marketCountry == "US") rawPrice * exchangeRate else rawPrice
            val finalProfitKrw = if (item.marketCountry == "US") rawProfit * exchangeRate else rawProfit

            binding.tvStockPrice.text = "${formatCurrency(finalPriceKrw.toLong().toString())}원"

            // 오픈 파이낸셜 Public CDN 주소 우회 로드 기조 유지
            val targetUrl = if (item.marketCountry == "KR") {
                "https://ssl.pstatic.net/imgfinance/chart/item/img/thumb/ico_${item.symbol}.png"
            } else {
                "https://financialmodelingprep.com/image-holding/${item.symbol}.png"
            }

            binding.tvLogoInitial.visibility = View.GONE
            binding.ivStockLogo.visibility = View.VISIBLE
            binding.ivStockLogo.load(targetUrl) {
                crossfade(true)
                listener(onError = { _, _ ->
                    binding.ivStockLogo.visibility = View.GONE
                    binding.tvLogoInitial.visibility = View.VISIBLE
                    setupInitialPlaceholder(item.name)
                })
            }

            // 개별 종목 수익률 소수점 스케일 가드 정정 및 부호 누락 방지
            val parsedRate = item.profitLossRate.toDoubleOrNull() ?: 0.0
            val formattedRate = String.format(Locale.KOREA, "%.1f%%", parsedRate * 100)
            val prefix = if (parsedRate > 0.0) "+" else ""
            
            binding.tvStockProfit.text = "${prefix}${formatCurrency(finalProfitKrw.toLong().toString())}원 ($formattedRate)"
            
            val color = if (parsedRate < 0.0) {
                Color.parseColor("#3182F6")
            } else if (parsedRate > 0.0) {
                Color.parseColor("#F04452")
            } else {
                Color.parseColor("#8B95A1")
            }
            binding.tvStockProfit.setTextColor(color)

            binding.root.setOnClickListener { onItemClick(item.symbol) }
        }

        private fun setupInitialPlaceholder(stockName: String) {
            val initialText = if (stockName.isNotEmpty()) stockName.substring(0, 1) else ""
            binding.tvLogoInitial.text = initialText

            val pastelColors = listOf("#1A3182F6", "#1A00C853", "#1A4E5968", "#1A8E24AA", "#1A00B0FF", "#1AFF6D00")
            val colorIndex = abs(stockName.hashCode()) % pastelColors.size
            val textColors = listOf("#3182F6", "#00C853", "#4E5968", "#8E24AA", "#00B0FF", "#FF6D00")

            val circleShape = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.parseColor(pastelColors[colorIndex]))
            }
            binding.tvLogoInitial.background = circleShape
            binding.tvLogoInitial.setTextColor(Color.parseColor(textColors[colorIndex]))
        }

        private fun formatCurrency(value: String): String {
            return try {
                val cleaned = value.replace(",", "")
                val parsed = cleaned.toDouble()
                if (parsed % 1 == 0.0) {
                    NumberFormat.getInstance(Locale.KOREA).format(parsed.toLong())
                } else {
                    String.format("%.2f", parsed)
                }
            } catch (e: Exception) {
                value
            }
        }
    }
}
