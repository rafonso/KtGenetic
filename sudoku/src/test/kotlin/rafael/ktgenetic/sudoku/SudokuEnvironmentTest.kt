package rafael.ktgenetic.sudoku

import org.junit.Test
import rafael.ktgenetic.geneticRandom
import kotlin.test.assertEquals

class SudokuEnvironmentTest {

    private fun test(str: String, expectedFitness: Double) {
        val puzzle: List<Cell> = str.map { Cell(geneticRandom.nextInt(), (it - '0')) }

//        assertEquals(expectedFitness, SudokuEnvironment().calculateFitness(puzzle))
    }

    @Test fun acceptablePuzzle() {
        test("851426793" +
                "947381256" +
                "236975184" +
                "482653971" +
                "793142568" +
                "165897342" +
                "619734825" +
                "574218639" +
                "328569417", 1.0)
    }

    @Test fun inacceptablePuzzle() {
        // first line from acceptablePuzzle() inverted
        test("397624158" +
                "947381256" +
                "236975184" +
                "482653971" +
                "793142568" +
                "165897342" +
                "619734825" +
                "574218639" +
                "328569417", 17.0 / 27)
    }

    @Test fun repeatedRows() {
        test("123456789" +
                "123456789" +
                "123456789" +
                "123456789" +
                "123456789" +
                "123456789" +
                "123456789" +
                "123456789" +
                "123456789", 9.0 / 27)
    }

    @Test fun repeatedSectors() {
        test("123123123" +
                "456456456" +
                "789789789" +
                "123123123" +
                "456456456" +
                "789789789" +
                "123123123" +
                "456456456" +
                "789789789", 9.0 / 27)
    }

    @Test fun repeatedColumns() {
        test("111111111" +
                "222222222" +
                "333333333" +
                "444444444" +
                "555555555" +
                "666666666" +
                "777777777" +
                "888888888" +
                "999999999", 9.0 / 27)
    }

}