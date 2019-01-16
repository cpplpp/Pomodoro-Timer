import com.intellij.openapi.wm.*
import java.util.*
import java.util.Timer
import javax.swing.*
import kotlin.concurrent.timerTask

class TimeTracker(toolWindow: ToolWindow) {
    private var contentPanel: JPanel? = null
    private var timeLabel: JLabel? = null
    private var startTime: Long = Date().time

    init {
        Timer().scheduleAtFixedRate(timerTask { updateTracker() }, 0, 250)
    }

    private fun updateTracker() {
        val timeElapsed = (Date().time - startTime) / 1000
        val elapsedString =
            (timeElapsed / 3600).toString().padStart(2, '0') + ":" +
            (timeElapsed / 60 % 60).toString().padStart(2, '0') + ":" +
            (timeElapsed % 60).toString().padStart(2, '0')
        timeLabel!!.text = elapsedString
    }

    fun getContent(): JPanel? {
        return contentPanel
    }
}