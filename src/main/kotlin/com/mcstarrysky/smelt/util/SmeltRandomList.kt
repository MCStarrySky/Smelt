package com.mcstarrysky.smelt.util

import java.util.concurrent.CopyOnWriteArrayList
import kotlin.random.Random

/**
 * @author sky
 * @since 2020-10-1 10:43
 */
class SmeltRandomList<T>(vararg element: Pair<T, Int>) {

    constructor(element: Collection<Pair<T, Int>>) : this(*element.toTypedArray())

    /** 随机列表 */
    private val value = CopyOnWriteArrayList<Value<T>>()

    init {
        element.forEach { add(it.first, it.second) }
    }

    /**
     * 获取随机元素
     */
    fun random(): T? {
        val sum = value.sumOf { it.index }
        if (sum > 0) {
            var m = Random.nextInt(sum)
            for (obj in value) {
                m -= obj.index
                if (m <= 0) {
                    return obj.element
                }
            }
        }
        return null
    }

    /**
     * 获取随机元素并移除
     */
    fun take(): T? {
        val element = random()
        if (element != null) {
            remove(element)
        }
        return element
    }

    /**
     * 添加元素
     *
     * @param element 元素
     * @param weight 权重
     */
    fun add(element: T, weight: Int = 1) {
        value.add(Value(element, weight))
    }

    /**
     * 添加元素
     */
    fun addAll(elements: Collection<Pair<T, Int>>) {
        elements.forEach { add(it.first, it.second) }
    }

    /**
     * 移除元素
     *
     * @param element 元素
     */
    fun remove(element: T) {
        value.removeIf { it.element == element }
    }

    /**
     * 获取所有元素
     */
    fun values(): MutableList<Value<T>> {
        return value
    }

    /**
     * 获取元素数量
     */
    fun size(): Int {
        return value.size
    }

    /**
     * 清空元素
     */
    fun clear() {
        value.clear()
    }

    data class Value<T>(val element: T, val index: Int)
}
