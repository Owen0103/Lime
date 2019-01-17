package com.moi.lime

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.moi.lime.api.MoiService
import com.moi.lime.util.Logger
import com.moi.lime.worker.PullWorker
import com.moi.lime.worker.PullWorker.Companion.KEY_RESULT
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import android.view.WindowManager
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.os.Build


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var service: MoiService
//    private val navigationController by lazy(LazyThreadSafetyMode.NONE) {
//        Navigation.findNavController(this, R.id.frag_nav_simple)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(window) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                statusBarColor = Color.TRANSPARENT
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
        setContentView(R.layout.activity_main)


//        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss z", Locale.US);
//try{
//   Date logDate1 = sdf.parse(logDate);
//}catch(Exception e){
//    System.out.println(e.getMessage());
//}
//        setSupportActionBar(toolbar)
//        NavigationUI.setupActionBarWithNavController(this, navigationController)
        testWork()
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navigationController.navigateUp()
//    }

    private fun testWork() {
        val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        val pullRequest = OneTimeWorkRequestBuilder<PullWorker>()
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance().enqueue(pullRequest)

        Logger.INS.d(pullRequest.id.toString())

        WorkManager.getInstance().getWorkInfoByIdLiveData(pullRequest.id)
                .observe(this, Observer { status ->
                    status?.let {
                        Logger.INS.d(it.state.name)
                        Logger.INS.d(it.outputData.getString(KEY_RESULT) ?: "")
                    }
                })
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

}
