package com.example.lesson3.presentation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lesson3.App
import com.example.lesson3.data.models.WeatherSuccessResponse
import com.example.lesson3.data.net.retrofit.RetrofitService
import com.example.lesson3.databinding.ActivityMainBinding
import com.example.lesson3.other.extensions.doOnEditorAction
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.lang.Exception
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import io.reactivex.subjects.ReplaySubject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            owner = this,
            factory = defaultViewModelProviderFactory
        )[MainViewModel::class.java]
        viewModel.initRouter(
            fm = supportFragmentManager,
            containerId = binding.fragmentContainer.id,
            finish = ::finish
        )
        viewModel.startWeatherScreen()
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}