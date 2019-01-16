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
        Timer().scheduleAtFixedRate(timerTask { updateTracker() }, 0, 500)
    }

    private fun updateTracker() {
        val timeElapsed = Date().time - startTime
        timeLabel!!.text = Date(timeElapsed).toString()
    }

    fun getContent(): JPanel? {
        return contentPanel
    }
}