package riz1.automaticscroll

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import duggu.automaticscroll.RecyclerTouchListener
import riz1.automaticscroll.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvNews.apply {
            val dataArray = resources.getStringArray(R.array.list_array)
            adapter = NewsAdapter(dataArray) {}
            /*adapter = object :
                MyGlobalAdapter<ItemAdapterBinding>(ItemAdapterBinding::inflate, dataArray.size) {
                override fun bind(binder: ItemAdapterBinding, position: Int) {
                    dataArray[position % dataArray.size]?.let { model ->
                        binder.tvNews.setOnClickListener {
                            it?.setTag(R.id.model, dataArray[position % dataArray.size])
                        }
                        binder.tvNews.text = model
                    }
                }
            }*/
            layoutManager = llManager
            setHasFixedSize(true)
            addOnItemTouchListener(
                RecyclerTouchListener(
                    this@MainActivity, object : RecyclerTouchListener.ClickListener {
                        override fun onClick(view: View?, position: Int) {
                            dataArray[position % dataArray.size]?.let { model ->
                                Toast.makeText(this@MainActivity, model, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
            )
        }
        handler.postDelayed(runnable, speedScroll)
    }

    private val llManager = object : GridLayoutManager(this, 3) {
        override fun smoothScrollToPosition(
            recyclerView: RecyclerView, state: RecyclerView.State, position: Int
        ) {
            val smoothScroller = object : LinearSmoothScroller(this@MainActivity) {
                private val SPEED = 400f // Change this value (default=25f)
                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return SPEED
                }
            }
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private val speedScroll = 0L
    private val runnable = object : Runnable {
        var count = 0
        override fun run() {
            val rvNews = binding.rvNews
            if (count == rvNews.adapter?.itemCount) count = 0
            if (count < (rvNews.adapter?.itemCount ?: -1)) {
                rvNews.smoothScrollToPosition(++count)
                handler.postDelayed(this, speedScroll)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, speedScroll)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}
