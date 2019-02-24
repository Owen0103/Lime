package com.moi.lime

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.lime.testing.OpenForTesting
import com.moi.lime.core.rxjava.RxBus
import com.moi.lime.vo.SignInExpireEvent
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@OpenForTesting
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var disposable :Disposable

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
        initNavGraph()
        disposable = RxBus.INSTANCE.toFlowable<SignInExpireEvent>()
                .subscribe({
                    navController(R.id.frag_nav_simple).navigate(MainNavDirections.actionGlobalSignInFragment())
                },{})
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::disposable.isInitialized){
            disposable.dispose()
        }
    }



    override fun supportFragmentInjector() = dispatchingAndroidInjector

    /**
     * Created to be able to override in tests
     */
    fun navController(@IdRes viewId: Int) = findNavController(viewId)

    /**
     * Created to be able to override in tests
     */
    fun initNavGraph(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frag_nav_simple) as NavHostFragment
        val navMain = navHostFragment.navController.navInflater.inflate(R.navigation.nav_main)
        navHostFragment.navController.graph = navMain

    }
}
