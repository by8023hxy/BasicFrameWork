package com.baiyu.androidx.basicframework.ui.main

import com.baiyu.androidx.basicframework.R
import com.baiyu.androidx.basicframework.adapter.DemoAdapter
import com.baiyu.androidx.basicframework.databinding.ActivityMainBinding
import com.baiyu.androidx.basicmodule.base.BaseBindingActivity
import com.baiyu.androidx.basicmodule.ext.logD
import com.baiyu.androidx.basicmodule.livedata.observeStatus
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModel()

    override fun afterViews() {
        binding {
            lifecycleOwner = this@MainActivity
            adapter = DemoAdapter()
            vm = mainViewModel.apply {
                getBannerList()
            }
        }
    }

    override fun initObserver() {
        mainViewModel.bannerList.observeStatus(this) {
            onLoading = {
                "MainActivity onLoading".logD()
            }
            onSuccess = {
                mainViewModel.banner.set(it)
                "MainActivity onSuccess".logD()
            }
            onError = {
                "MainActivity onError".logD()
            }
        }
    }
}