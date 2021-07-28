package com.example.awsvmsguild.resultActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.awsvmsguild.R
import com.example.awsvmsguild.data.ResultContent
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    private val model by viewModels<ResultViewModel>()
    private var userId: String? = null
    private var data: ArrayList<ResultContent> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        userId = intent?.getStringExtra("userId")
        with(rv_result) {
            adapter = ResultAdapter(data)
            layoutManager = LinearLayoutManager(this@ResultActivity)
        }

        userId?.let {
            model.getResult(it)
        }

        srl_result.setOnRefreshListener {
            model.getResult(userId!!)
        }
        observerData()
        observerView()

    }

    private fun observerData() {
        model.matchingResult.observe(this) {
            data.clear()
            data.addAll(it)
            rv_result.adapter?.notifyDataSetChanged()
        }
    }

    private fun observerView() {
        model.loadingState.observe(this) {
            srl_result.isRefreshing = it
        }
    }
}