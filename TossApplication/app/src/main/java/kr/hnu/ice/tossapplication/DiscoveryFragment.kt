package kr.hnu.ice.tossapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.hnu.ice.tossapplication.data.StockModel
import kr.hnu.ice.tossapplication.databinding.FragmentDiscoveryBinding
import java.util.Timer
import kotlin.concurrent.timer

class DiscoveryFragment : Fragment() {
    private var _binding: FragmentDiscoveryBinding? = null
    private val binding get() = _binding!!
    private val adapter = StockAdapter()
    private var mockTimer: Timer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        binding.rvDiscovery.adapter = adapter
        startRealtimeUpdates()
        return binding.root
    }

    private fun startRealtimeUpdates() {
        // 백그라운드 스레드 시뮬레이션 인터벌 제어
        mockTimer = timer(period = 2000) {
            val updatedData = listOf(
                StockModel("005930", "삼성전자", 72000.0, (71000..73000).random().toDouble(), 10),
                StockModel("035420", "NAVER", 180000.0, (178000..182000).random().toDouble(), 5)
            )
            activity?.runOnUiThread {
                adapter.updateItems(updatedData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mockTimer?.cancel()
        _binding = null
    }
}