package kr.hnu.ice.tossapplication.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import kr.hnu.ice.tossapplication.databinding.ActivityOrderHistoryBinding
import kr.hnu.ice.tossapplication.viewmodel.OrderHistoryUiState
import kr.hnu.ice.tossapplication.viewmodel.OrderHistoryViewModel

class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderHistoryBinding
    private val viewModel: OrderHistoryViewModel by viewModels()
    private lateinit var historyAdapter: OrderHistoryAdapter 

    private var currentTabStatus = "OPEN"
    private var hasNextPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        observeOrderHistoryStream()

        // 초기 진입 시 진행 중인 주문(OPEN) 우선 버퍼 덤프
        viewModel.loadOrderHistory("OPEN")
    }

    private fun initViews() {
        historyAdapter = OrderHistoryAdapter()
        binding.rvOrderHistory.apply {
            layoutManager = LinearLayoutManager(this@OrderHistoryActivity)
            adapter = historyAdapter

            // [무한 스크롤 제어] 리스트 최하단 도달 감지 시 CLOSED 탭일 때만 추가 페이징 트리거
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    
                    if (currentTabStatus == "CLOSED" && hasNextPage && lastVisibleItem == historyAdapter.itemCount - 1) {
                        viewModel.loadOrderHistory(status = "CLOSED", isLoadMore = true)
                    }
                }
            })
        }

        // 탭 레이아웃 스위칭 가드 연동
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTabStatus = if (tab?.position == 0) "OPEN" else "CLOSED"
                viewModel.loadOrderHistory(currentTabStatus)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeOrderHistoryStream() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is OrderHistoryUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is OrderHistoryUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            hasNextPage = state.hasNext
                            
                            // UI 상태 스냅샷을 리사이클러뷰 데이터셋에 주입
                            historyAdapter.submitList(state.orders)
                        }
                        is OrderHistoryUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@OrderHistoryActivity, state.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
