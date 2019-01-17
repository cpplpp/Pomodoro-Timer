import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.wm.*
import java.util.Timer
import javax.swing.*
import kotlin.concurrent.fixedRateTimer

class TimeTracker(toolWindow: ToolWindow) {
    private var contentPanel: JPanel? = null
    private var timeLabel: JLabel? = null
    private var startButton: JButton? = null
    private var pauseButton: JButton? = null
    private var resetButton: JButton? = null

    private var timeElapsed: Long = 0
    private val period: Long = 250
    private var timer: Timer? = null

    init {
        initTimer()
        startButton!!.isVisible = false

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

        resetButton!!.addActionListener {
            timer!!.cancel()
            startButton!!.isVisible = true
            pauseButton!!.isVisible = false
            timeElapsed = 0
            updateTracker()
        }
    }

    private fun initTimer() {
        timer = fixedRateTimer(name = "TimeTrackerTimer", period = period) {
            updateTracker()
        }
    }

    private fun updateTracker() {
        timeElapsed += period
        val timeInSec = timeElapsed / 1000
        val hours = timeInSec / 3600
        val minutes = timeInSec / 60 % 60
        val seconds = timeInSec % 60
        val elapsedString = when (hours) {
            0.toLong() -> "Time elapsed: %02d:%02d".format(minutes, seconds)
            else -> "Time elapsed: %d:%02d:%02d".format(hours, minutes, seconds)
        }
        if (timeElapsed % (30 * 60 * 1000) == 0.toLong())
            Notifications.Bus.notify(
                Notification(
                    "TimeTracker",
                    "Time tracker",
                    "You have spent another half of an hour. Please relax for a moment :)",
                    NotificationType.INFORMATION
                )
            )
        timeLabel!!.text = elapsedString
    }

    fun getContent(): JPanel? {
        return contentPanel
    }
}