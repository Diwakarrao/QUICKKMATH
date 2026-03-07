package com.quickmath.app.data

import kotlin.random.Random

data class QuizCard(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val difficulty: Difficulty,
)

object MathQuiz {
    private val ops = listOf("+", "-", "×", "÷")
    private val letters = listOf("A", "B", "C", "D")

    private fun randInt(min: Int, max: Int): Int = Random.nextInt(min, max + 1)

    private fun getOperands(difficulty: Difficulty, op: String): Pair<Int, Int> {
        if (op == "÷") {
            return when (difficulty) {
                Difficulty.EASY -> {
                    val b = randInt(2, 9)
                    val a = b * randInt(1, 9)
                    a to b
                }
                Difficulty.MEDIUM -> {
                    val b = randInt(2, 9)
                    val a = b * randInt(10, 19)
                    a to b
                }
                Difficulty.HARD -> {
                    val b = randInt(2, 12)
                    val a = b * randInt(10, 29)
                    a to b
                }
            }
        }
        return when (difficulty) {
            Difficulty.EASY -> randInt(1, 9) to randInt(1, 9)
            Difficulty.MEDIUM -> randInt(10, 99) to randInt(1, 9)
            Difficulty.HARD -> randInt(10, 99) to randInt(10, 99)
        }
    }

    private fun compute(a: Int, b: Int, op: String): Int = when (op) {
        "+" -> a + b
        "-" -> a - b
        "×" -> a * b
        "÷" -> a / b
        else -> 0
    }

    private fun generateDistractors(correct: Int, count: Int): List<Int> {
        val set = mutableSetOf<Int>()
        var attempts = 0
        while (set.size < count && attempts < 200) {
            attempts++
            val delta = randInt(1, 10)
            val candidate = correct + if (Random.nextBoolean()) delta else -delta
            if (candidate != correct && candidate >= 0) set.add(candidate)
        }
        while (set.size < count) set.add(correct + set.size + 1)
        return set.toList()
    }

    fun generateQuizCard(difficulty: Difficulty = Difficulty.EASY): QuizCard {
        val op = ops.random()
        val (a, b) = getOperands(difficulty, op)
        val correct = compute(a, b, op)
        val distractors = generateDistractors(correct, 3)
        val allOptions = (listOf(correct) + distractors).shuffled()
        val correctIndex = allOptions.indexOf(correct)
        val options = allOptions.mapIndexed { i, v -> "${letters[i]}. $v" }
        return QuizCard(
            question = "$a $op $b = ?",
            options = options,
            correctIndex = correctIndex,
            difficulty = difficulty,
        )
    }

    fun adaptDifficulty(
        current: Difficulty,
        responseTimeMs: Long,
        wasCorrect: Boolean,
    ): Difficulty {
        if (wasCorrect && responseTimeMs < 8000) {
            return when (current) {
                Difficulty.EASY -> Difficulty.MEDIUM
                Difficulty.MEDIUM, Difficulty.HARD -> Difficulty.HARD
            }
        }
        if (!wasCorrect || responseTimeMs > 12000) {
            return when (current) {
                Difficulty.HARD -> Difficulty.MEDIUM
                Difficulty.MEDIUM, Difficulty.EASY -> Difficulty.EASY
            }
        }
        return current
    }
}
