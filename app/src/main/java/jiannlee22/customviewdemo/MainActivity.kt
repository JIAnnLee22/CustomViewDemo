package jiannlee22.customviewdemo

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tasksView = findViewById<TasksView>(R.id.tasksview)
        tasksView.setOnClickListener {
            animator(tasksView)
        }
        animator(tasksView)
    }

    private fun animator(tasksView: TasksView) {
        val valueAnimator = ObjectAnimator.ofInt(0, tasksView.getSize())
        valueAnimator.duration = 5000
        valueAnimator.addUpdateListener {
            tasksView.setProgress(it.animatedValue as Int)
        }
        valueAnimator.startDelay = 1000
        valueAnimator.start()
    }
}