import cn.hutool.core.util.IdUtil
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    for (i in 1..10) {
        Thread.sleep(10)
        println(IdUtil.createSnowflake(1, 1).nextIdStr())
    }
}
