package com.karthik.pro.engr.github.api.android_interview_mastery

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    fun divide(a: Int, b: Int): Int {
        return a / b
    }

    fun isAdult(age: Int): Boolean {
        return age >= 18
    }

    fun add(a: Int, b: Int): Int {
        return a + b
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun givenTwoNumber_whenAdding_returnSum() {
        // Arrange
        val a = 10
        val b = 20

        // Act
        val result = add(a, b)

        // Assert
        assertEquals(30, result)
    }

    @Test
    fun givenAge_whenAdultAge_returnTrue() {
        // Arrange
        val age = 18

        // Act
        val isAdult = isAdult(age)

//        Assert
        assertTrue(isAdult)
    }

    @Test
    fun givenAge_whenMinorAge_returnFalse() {
//        Arrange
        val age = 5

//        Act
        val isAdult = isAdult(age)

//        Assert
        assertFalse(isAdult)
    }

    @Test
    fun divide_whenDividing_returnsTheResult() {
//        Assert
        val a = 10
        val b = 5

//        Act
        val result = divide(a, b)

//        Assert
        assertEquals(2, result)
    }

    @Test(expected = ArithmeticException::class)
    fun divide_whenDivingByZero_returnsException() {
        val a = 10
        val b = 0

//        Act
        val result = divide(a, b)
    }


}