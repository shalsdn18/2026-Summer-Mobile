package kr.hnu.ice.recyclerview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.hnu.ice.recyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 데이터 세팅 (1~50)
        val datas = mutableListOf<String>()
        for (i in 1..50) {
            datas.add("Item $i")
        }

        // 어댑터 객체 생성 및 리사이클러뷰 구조 연결
        val adapter = MyAdapter(datas)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // [데이터 추가] 버튼 액션 제어
        binding.addBtn.setOnClickListener {
            val nextIndex = datas.size + 1
            datas.add("Item $nextIndex")
            adapter.notifyItemInserted(datas.size - 1)
            binding.recyclerView.scrollToPosition(datas.size - 1) // 최하단 스크롤 이동
        }

        // [데이터 삭제] 버튼 액션 제어
        binding.delBtn.setOnClickListener {
            if (datas.isNotEmpty()) {
                val targetIndex = datas.size - 1
                datas.removeAt(targetIndex)
                adapter.notifyItemRemoved(targetIndex)
            }
        }
    }
}