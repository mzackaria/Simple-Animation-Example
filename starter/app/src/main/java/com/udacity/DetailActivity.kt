package com.udacity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        cancelAllNotifications(this)

        if (intent.hasExtra(EXTRA_CODE_FILE_NAME))
            mFileNameTV.text = intent.getStringExtra(EXTRA_CODE_FILE_NAME)

        if (intent.hasExtra(EXTRA_CODE_STATUS))
            mStatusTV.isSuccess = intent.getBooleanExtra(EXTRA_CODE_STATUS, false)
    }

    fun buttonClick(v: View) {
        finish()
    }

}
