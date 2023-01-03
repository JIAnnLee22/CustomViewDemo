package jiannlee22.customviewdemo

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val countdownView = findViewById<CountdownView>(R.id.countdown)
        val countdownView1 = findViewById<CountdownView>(R.id.countdown1)
        val valueAnimator = ObjectAnimator.ofInt(0, 100)
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener {
            Log.d("TAG", "onCreate: animatedValue is ${it.animatedValue}")
            countdownView.setProgress(it.animatedValue as Int)
            countdownView1.setProgress(it.animatedValue as Int)
        }
        valueAnimator.startDelay = 1000
        valueAnimator.start()
    }
}