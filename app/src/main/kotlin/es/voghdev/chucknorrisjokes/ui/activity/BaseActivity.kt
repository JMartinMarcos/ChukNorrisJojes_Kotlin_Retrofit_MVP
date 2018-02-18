package es.voghdev.chucknorrisjokes.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())
    }

    abstract fun getLayoutId(): Int
}
