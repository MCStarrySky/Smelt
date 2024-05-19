package com.mcstarrysky.smelt

import taboolib.common5.cint
import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.conversion
import taboolib.module.configuration.util.asMap

/**
 * Smelt
 * me.mical.smelt.SmeltSettings
 *
 * @author mical
 * @since 2024/5/19 16:03
 */
object SmeltSettings {

    @Config
    lateinit var config: Configuration
        private set

    @ConfigNode(value = "star.empty")
    var star_empty = "〇"

    @ConfigNode(value = "star.fill")
    var star_fill = "۞"

    @delegate:ConfigNode(value = "star.fill")
    val star_colors by conversion<ConfigurationSection, Map<Int, String>> {
        asMap().mapKeys { it.key.toInt() }.mapValues { it.value.toString() }
    }

    @ConfigNode(value = "jd.tipLevel")
    var jd_tip_level = 5

    @delegate:ConfigNode(value = "jd.levels")
    val jd_levels by conversion<ConfigurationSection, Map<Int, Int>> {
        asMap().mapKeys { it.key.toInt() }.mapValues { it.value.cint }
    }

    @ConfigNode(value = "dig.tipLevel")
    var dig_tip_level = 5

    @delegate:ConfigNode(value = "dig.levels")
    val dig_levels by conversion<ConfigurationSection, Map<Int, Int>> {
        asMap().mapKeys { it.key.toInt() }.mapValues { it.value.cint }
    }

    @ConfigNode(value = "smith.need")
    var smith_need = 5
}