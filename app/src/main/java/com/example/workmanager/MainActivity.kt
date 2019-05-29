package com.example.workmanager

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var oneTimeWork: OneTimeWorkRequest
    lateinit var periodicWork: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //requires chanrging
        /*val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()*/

        val data = Data.Builder()
            .putString(MyWorker.EXTRA_TITLE, "Message from Activity!")
            .putString(MyWorker.EXTRA_TEXT, "Testing from activity.")
            .build()
        //one time request
        //.setConstraints(constraints)
        oneTimeWork = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data)
            .addTag("Simple_Work")
            .build()

        //periodicWork
        periodicWork = PeriodicWorkRequest
            .Builder(MyWorker::class.java, 1, TimeUnit.MINUTES)
            .addTag("periodicWork")
            .build()

        simpleWorkButton.setOnClickListener {
            //single request
//            WorkManager.getInstance().enqueue(oneTimeWork)

            //periodicWork
            WorkManager.getInstance().enqueue(periodicWork)
        }

        cancelWorkButton.setOnClickListener {
            //cancel by id
            //WorkManager.getInstance().cancelWorkById(oneTimeWork.id)

            //cancel by tags
//            WorkManager.getInstance().cancelAllWorkByTag("Simple_Work")
            WorkManager.getInstance().cancelAllWorkByTag("periodicWork")
        }
//        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWork.id)
        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicWork.id)
            .observe(this@MainActivity, Observer {
                it?.let {
                    textView.append("SimpleWorkRequest: " + it.state.name + "\n")
                    //when process completed!
                    if (it.state.isFinished) {
                        val message =
                            it.outputData.getString(MyWorker.EXTRA_OUTPUT_MESSAGE)
                        textView.append("From Worker (Data): $message")
                    }

                }
            })
    }
}
