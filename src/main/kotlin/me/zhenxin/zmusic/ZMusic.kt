package me.zhenxin.zmusic

import me.zhenxin.zmusic.module.Lang
import me.zhenxin.zmusic.module.Logger
import me.zhenxin.zmusic.module.config
import me.zhenxin.zmusic.module.taboolib.registerChannel
import me.zhenxin.zmusic.utils.colored
import me.zhenxin.zmusic.utils.loginNetease
import me.zhenxin.zmusic.utils.setLocale
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform.*
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.runningPlatform
import taboolib.common.platform.function.submit
import taboolib.module.metrics.Metrics
import taboolib.module.nms.MinecraftVersion


/**
 * ZMusic 主类
 *
 * @author 真心
 * @since 2021/8/14 14:33
 * @email qgzhenxin@qq.com
 */
@Suppress("unused")
object ZMusic {
    lateinit var VERSION: String

    private const val logo = "" +
            "  ______  __  __                 _        \n" +
            " |___  / |  \\/  |               (_)       \n" +
            "    / /  | \\  / |  _   _   ___   _    ___ \n" +
            "   / /   | |\\/| | | | | | / __| | |  / __|\n" +
            "  / /__  | |  | | | |_| | \\__ \\ | | | (__ \n" +
            " /_____| |_|  |_|  \\__,_| |___/ |_|  \\___|\n"

    @Awake(LifeCycle.LOAD)
    fun onLoad() {
        // 初始化变量
        VERSION = pluginVersion

        // 初始化日志模块
        logger = Logger(console())
    }

    @Awake(LifeCycle.ENABLE)
    fun onEnable() {
        setLocale()
        logo.split("\n").forEach {
            logger.info("§b$it")
        }
        logger.info("\t§6v$VERSION\tby ZhenXin")

        logger.info(Lang.INIT_LOADING)
        // 注册bStats
        Metrics(7291, VERSION, BUKKIT)
        Metrics(8864, VERSION, BUNGEE)
        Metrics(12426, VERSION, VELOCITY)

        // 注册通信频道
        registerChannel("zmusic:channel")
        registerChannel("allmusic:channel")

        Lang.INIT_LOADED.forEach {
            logger.info(
                it.replace("{0}", VERSION)
                    .replace("{1}", runningPlatform.name.lowercase())
            )
        }

        if (runningPlatform == BUKKIT) {
            logger.debug(MinecraftVersion.majorLegacy)
        }

        if (config.DEBUG) {
            submit(async = true) {
                logger.info("&a正在尝试登录网易云音乐...".colored())
                val result = loginNetease()
                logger.info(result.message.colored())
            }
        }
    }
}

lateinit var logger: Logger