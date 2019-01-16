import com.intellij.openapi.wm.*
import java.util.*
import java.util.Timer
import javax.swing.*
import kotlin.concurrent.fixedRateTimer

class TimeTracker(toolWindow: ToolWindow) {
    private var contentPanel: JPanel? = null
    private var timeLabel: JLabel? = null
    private var startButton: JButton? = null
    private var pauseButton: JButton? = null
    private var stopButton: JButton? = null

    private var startTime: Long = Date().time
    private var timer: Timer? = null

    init {
        initTimer()
        startButton!!.addActionListener {
            initTimer()
            pauseButton!!.isVisible = true
            startButton!!.isVisible = false
        }
        pauseButton!!.addActionListener {
            timer!!.cancel()
            startButton!!.isVisible = true
            pauseButton!!.isVisible = false
        }
        stopButton!!.addActionListener {  }
    }

    private fun initTimer() {
        timer = fixedRateTimer(name = "TimeTrackerTimer", period = 500) {
            updateTracker()
        }
    }

    private fun updateTracker() {
        val timeElapsed = (Date().time - startTime) / 1000
        val hours = timeElapsed / 3600
        val minutes = timeElapsed / 60 % 60
        val seconds = timeElapsed % 60
        val elapsedString = when (hours) {
            0.toLong() -> "Time elapsed: %02d:%02d".format(minutes, seconds)
            else -> "Time elapsed: %d:%02d:%02d".format(hours, minutes, seconds)
        }
        timeLabel!!.text = elapsedString
    }

    fun getContent(): JPanel? {
        return contentPanel
    }
}