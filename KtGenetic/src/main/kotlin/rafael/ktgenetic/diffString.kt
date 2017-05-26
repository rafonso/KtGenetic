fun charDistance(ch1: Char, ch2: Char ): Int = ch1 - ch2

interface StringFitness {
    fun calculate(str1: String, str2 : String): Double;
}

abstract class CharByCharFitness: StringFitness {
    
    abstract fun calculateDiff(ch1: Char, ch2: Char ): Int;
    
    override fun calculate(str1: String, str2 : String): Double {
        if(str1.length != str1.length) {
            error("tamahios diferentes")
        }

//        var diff = (0 until str1.length).map(it -> it.length).sum()
        var diff =0
        for(i in 0 until str1.length) {
            diff = diff + calculateDiff(str1[i], str2[i])
        }
        
        return Math.abs(1.0 / (1.0 + diff))
    }
}

class SubtractCharsFitness: CharByCharFitness() {
    override fun calculateDiff(ch1: Char, ch2: Char ): Int = Math.abs(ch1 - ch2)
}

class EqualCharsFitness : CharByCharFitness() {
    override fun calculateDiff(ch1: Char, ch2: Char ): Int = if(ch1 == ch2) 0 else 1
}

fun main(args: Array<String>) {
    
    if (args.size == 0) {
        println("Please provide a name as a command-line argument")
        return
    }

    println(args[0] + " - " + args[1])
    val fitnessGenerator = EqualCharsFitness()

    
    val result = fitnessGenerator.calculate(args[0], args[1])
    
    println(result)
}