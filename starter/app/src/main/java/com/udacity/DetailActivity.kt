package com.udacity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity(), MotionLayout.TransitionListener {

    var toStartTransitionInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        cancelAllNotifications(this)

        if (intent.hasExtra(EXTRA_CODE_FILE_NAME))
            mFileNameTV.text = intent.getStringExtra(EXTRA_CODE_FILE_NAME)

        if (intent.hasExtra(EXTRA_CODE_STATUS))
            mStatusTV.isSuccess = intent.getBooleanExtra(EXTRA_CODE_STATUS, false)

        motionLayout.setTransitionListener(this)

    }

    fun buttonClick(v: View) {
        toStartTransitionInProgress = true
        motionLayout.setTransitionDuration(400)
        motionLayout.transitionToStart()
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        if (toStartTransitionInProgress) finish()
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
    }
}
