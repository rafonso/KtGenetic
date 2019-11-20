package rafael.ktgenetic.salesman.fx

import javafx.util.StringConverter
import rafael.ktgenetic.salesman.PathType
import java.math.BigInteger

private fun factorial(n: Int) = (1..n).fold(BigInteger.ONE) { prod, i -> prod * i.toBigInteger() }

enum class PathTypeOptions(val type: PathType, val title: String, val maxPossiblePaths: (Int) -> BigInteger) {
    // TODO: verificar melhor o cálculo de caminhos possíveis
    OPEN(PathType.OPEN, "Open", { n -> factorial(n - 1) }),
    OPEN_START(PathType.OPEN_START, "Open With Start", { n -> factorial(n - 1) + BigInteger.ONE }),
    OPEN_END(PathType.OPEN_END, "Open With End", { n -> factorial(n - 1) + BigInteger.ONE }),
    OPEN_START_END(
        PathType.OPEN_START_END,
        "Open With Start and End",
        { n -> factorial(n - 1) + BigInteger.ONE + BigInteger.ONE }),
    CLOSED(PathType.CLOSED, "Closed", { n -> factorial(n) }),
    CLOSED_START(PathType.CLOSED_START, "Closed With Start", { n -> factorial(n) });

}

object PathTypeOptionsStingConverter : StringConverter<PathTypeOptions>() {

    override fun toString(option: PathTypeOptions?): String = option!!.title

    override fun fromString(string: String?): PathTypeOptions =
            PathTypeOptions.values().first { opt -> opt.title == string }

}