package kr.hnu.ice.julyfirstapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kr.hnu.ice.julyfirstapplication.databinding.ActivityMainBinding
import kr.hnu.ice.viewbindingexam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.restBtn.setOnClickListener {
            binding
        }
    }
}