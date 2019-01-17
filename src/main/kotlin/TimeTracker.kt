import com.intellij.icons.AllIcons
import com.intellij.notification.Notification
import com.intellij.notification.Notifications
import com.intellij.notification.NotificationType
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

    private val period: Long = 250

    private val workingPeriod: Long = 25 * 60 * 1000 + period
    private val relaxPeriod: Long = 5 * 60 * 1000 + period
    private var isRelaxing = false

    private var timeRemained: Long = workingPeriod
    private var timer: Timer? = null

    init {
        startButton!!.icon = AllIcons.Actions.Execute
        pauseButton!!.icon = AllIcons.Actions.Pause
        resetButton!!.icon = AllIcons.Actions.Restart

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
            timeRemained = workingPeriod
            updateTracker()
        }
    }

    private fun initTimer() {
        timer = fixedRateTimer(name = "TimeTrackerTimer", period = period) {
            updateTracker()
        }
    }

    private fun updateTracker() {
        timeRemained -= period
        val timeInSec = timeRemained / 1000
        val hours = timeInSec / 3600
        val minutes = timeInSec / 60 % 60
        val seconds = timeInSec % 60
        var changed = false

        if (timeRemained == 0.toLong()) {
            isRelaxing = !isRelaxing
            changed = true
            timeRemained = if (isRelaxing)
                relaxPeriod
            else
                workingPeriod
        }

        var notification =
            if (isRelaxing)
            Notification(
                "TimeTracker",
                "Pomodoro tracker",
                "You have spent 25 minutes. Now you should have a rest for 5 minutes",
                NotificationType.INFORMATION
            )
            else
            Notification(
                "TimeTracker",
                "Pomodoro tracker",
                "5 minutes have passed, now you can work again",
                NotificationType.INFORMATION
            )

        val elapsedString = when (hours) {
            0.toLong() -> "Time remained: %02d:%02d".format(minutes, seconds)
            else -> "Time remained: %d:%02d:%02d".format(hours, minutes, seconds)
        }

        if (changed) {
            Notifications.Bus.notify(notification)
        }
        timeLabel!!.text = elapsedString
    }

    fun getContent(): JPanel? {
        return contentPanel
    }
}