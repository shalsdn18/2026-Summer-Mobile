package kr.hnu.ice.july09application

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kr.hnu.ice.july09application.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // 이미지 기준 영문 표기로 변경 및 변수 할당 유지
        val menuItem1: MenuItem? = menu?.add(0, 1, 0, "Menu Item 1")
        val menuItem2: MenuItem? = menu?.add(0, 2, 1, "Menu Item 2")
        val menuItem3: MenuItem? = menu?.add(0, 3, 2, "Menu Item 3")

        val searchItem = menu?.add(0, 4, 3, "Search")
        searchItem?.setIcon(android.R.drawable.ic_menu_search)
        searchItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        val searchView = SearchView(this)
        searchView.queryHint = "Search here"
        searchItem?.actionView = searchView

        // 익명 객체 중괄호 기호 및 누락된 기능 보완
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "Search submitted: $query", Toast.LENGTH_SHORT).show()
                binding.textView.text = "검색 제출: $query"
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(this@MainActivity, "Search text changed: $newText", Toast.LENGTH_SHORT).show()
                binding.textView.text = "검색 텍스트 변경: $newText"
                return false
            }
        }) // 인터페이스 및 setOnQueryTextListener 정상 종료

        // 핵심: 생성한 searchView를 searchItem의 액션 뷰로 지정
        searchItem?.actionView = searchView

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            1 -> {
                Toast.makeText(this, "Menu Item 1 selected", Toast.LENGTH_SHORT).show()
                binding.textView.text = "메뉴 아이템 1이 선택되었습니다."
                return true
            }
            2 -> {
                Toast.makeText(this, "Menu Item 2 selected", Toast.LENGTH_SHORT).show()
                binding.textView.text = "메뉴 아이템 2이 선택되었습니다."
                return true
            }
            3 -> {
                Toast.makeText(this, "Menu Item 3 selected", Toast.LENGTH_SHORT).show()
                binding.textView.text = "메뉴 아이템 3이 선택되었습니다."
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}