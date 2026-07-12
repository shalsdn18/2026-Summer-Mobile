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
import kr.hnu.ice.tossapplication.databinding.ActivityConditionalOrderBinding
import kr.hnu.ice.tossapplication.networking.TossConditionRequest
import kr.hnu.ice.tossapplication.networking.TossConditionalOrderCreateRequest
import kr.hnu.ice.tossapplication.viewmodel.ConditionalOrderUiState
import kr.hnu.ice.tossapplication.viewmodel.ConditionalOrderViewModel
import kotlinx.coroutines.launch

class ConditionalOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConditionalOrderBinding
    private val viewModel: ConditionalOrderViewModel by viewModels()
    private lateinit var schedulerAdapter: ConditionalSchedulerAdapter 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConditionalOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        observeConditionalOrderStream()

        // 활성 감시 리스트 우선 수집 가동
        viewModel.loadSchedulers("OPEN")
    }

    private fun initViews() {
        schedulerAdapter = ConditionalSchedulerAdapter(
            onCancelClick = { orderId -> 
                // 특정 활성 감시망 터치 시 실시간 취소 트랜잭션 패치
                viewModel.terminateScheduler(orderId) 
            }
        )
        binding.rvSchedulers.apply {
            layoutManager = LinearLayoutManager(this@ConditionalOrderActivity)
            adapter = schedulerAdapter
        }

        // [가상 가동 인터랙션] OCO(익절/손절 동시 감시) 모형 조립 테스트 생성 단추
        binding.btnCreateOcoSample.setOnClickListener {
            val ocoRequest = TossConditionalOrderCreateRequest(
                symbol = "005930",
                type = "OCO", // One-Cancels-the-Other
                quantity = "50",
                orderType = "LIMIT", // OCO 고정 제약
                expireDate = "2026-12-31",
                first = TossConditionRequest(orderSide = "SELL", triggerPrice = "75000", orderPrice = "75000"), // 익절
                second = TossConditionRequest(orderSide = "SELL", triggerPrice = "65000", orderPrice = "64800") // 손절 가드
            )
            viewModel.registerConditionalOrder(ocoRequest)
        }
    }

    private fun observeConditionalOrderStream() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ConditionalOrderUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is ConditionalOrderUiState.ListSuccess -> {
                            binding.progressBar.visibility = View.GONE
                            // 원격 앱 및 모바일 실시간 동기화 스케줄 바인딩
                            schedulerAdapter.submitList(state.schedulers)
                        }
                        is ConditionalOrderUiState.TxSuccess -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@ConditionalOrderActivity, state.message, Toast.LENGTH_SHORT).show()
                            // 신규 가변 ID 포인터가 포함된 경우 디버그 로그 바인딩 가동
                            state.newOrderId?.let { android.util.Log.d("AUTOMATION_CORE", "New Target ID Binding: $it") }
                        }
                        is ConditionalOrderUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@ConditionalOrderActivity, state.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
