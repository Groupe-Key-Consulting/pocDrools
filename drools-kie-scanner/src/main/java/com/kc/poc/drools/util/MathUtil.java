package com.kc.poc.drools.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class MathUtil {

	private static final Map<Character, Integer> ROMAN_VALUES;

	public static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);

	static {
		ROMAN_VALUES = new HashMap<>();
		ROMAN_VALUES.put('I', 1);
		ROMAN_VALUES.put('V', 5);
		ROMAN_VALUES.put('X', 10);
		ROMAN_VALUES.put('L', 50);
		ROMAN_VALUES.put('C', 100);
		ROMAN_VALUES.put('D', 500);
		ROMAN_VALUES.put('M', 1000);
	}

	private MathUtil() {
		super();
	}

	public static BigDecimal denullify(BigDecimal value) {
		if (value == null) {
			return BigDecimal.ZERO;
		} else {
			return value;
		}
	}

	public static BigDecimal denullify(Optional<BigDecimal> value) {
		return value.orElse(BigDecimal.ZERO);
	}

	public static double asDoubleValue(BigDecimal bigValue) {
		if (bigValue == null) {
			return 0.0;
		} else {
			return bigValue.doubleValue();
		}
	}

	public static double asDoubleValue(Optional<BigDecimal> bigOptional) {
		return asDoubleValue(denullify(bigOptional));
	}

	public static double asDoubleValue(Integer intValue) {
		if (intValue == null) {
			return 0.0;
		} else {
			return intValue.doubleValue();
		}
	}

	public static <T> Map<T, Double> asDoubleValue(Map<T, BigDecimal> map) {
		return map.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> MathUtil.asDoubleValue(e.getValue())));
	}

	public static double denullify(Double doubleValue) {
		if (doubleValue == null) {
			return 0.0;
		} else {
			return doubleValue.doubleValue();
		}
	}

	public static int denullify(Integer integerValue) {
		if (integerValue == null) {
			return 0;
		} else {
			return integerValue.intValue();
		}
	}

	public static BigDecimal asBigDecimal(Double doubleValue, boolean keepNullValue) {
		if (doubleValue == null || !Double.isFinite(doubleValue) || Double.isNaN(doubleValue)) {
			return keepNullValue ? null : BigDecimal.ZERO;
		} else {
			return BigDecimal.valueOf(doubleValue);
		}
	}
    public static BigDecimal asBigDecimal(String value) {
        if (value == null ) {
            return  BigDecimal.ZERO;
        } else {
            return new BigDecimal(value.replace(" ",""));
        }
    }


	public static BigDecimal asBigDecimal(Double doubleValue) {
		return asBigDecimal(doubleValue, false);
	}

	public static BigDecimal asBigDecimal(Integer intValue) {
		if (intValue == null || !Double.isFinite(intValue) || Double.isNaN(intValue)) {
			return BigDecimal.ZERO;
		} else {
			return BigDecimal.valueOf(intValue);
		}
	}

	public static BigDecimal asBigDecimal(Double value, int scale) {
		return asBigDecimal(value).setScale(scale, RoundingMode.HALF_UP);
	}

	public static <T> Function<T, BigDecimal> asBigDecimal(Function<T, Double> doubleFunc) {
		return doubleFunc.andThen(MathUtil::asBigDecimal);
	}

	public static <T> Function<T, BigDecimal> intFuncAsBigDecimalFunc(Function<T, Integer> intFunc) {
		return intFunc.andThen(MathUtil::asBigDecimal);
	}

	public static int asIntValue(Integer value) {
		if (value == null) {
			return 0;
		} else {
			return value.intValue();
		}
	}

	public static double round(double value, int scale) {
		return asBigDecimal(value, scale).doubleValue();
	}

	public static Integer round(Double value) {
		if (value == null) {
			return null;
		} else {
			return asBigDecimal(value, 0).intValue();
		}
	}

	public static BigDecimal add(BigDecimal... values) {
		BigDecimal result = BigDecimal.ZERO;
		for (BigDecimal value : values) {
			if (value != null) {
				result = result.add(value);
			}
		}
		return result;
	}

	public static BigDecimal multiply(BigDecimal... values) {
		if (values.length == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal result = BigDecimal.ONE;
		for (BigDecimal v : values) {
			result = result.multiply(denullify(v));
		}
		return result;
	}

	public static Integer add(Integer... values) {
		Integer result = 0;
		for (Integer value : values) {
			if (value != null) {
				result += value;
			}
		}
		return result;
	}

	 /**
     * Retourner un map dont les clefs sont les clefs des maps donnés, et la valeur est la somme des valeurs correspondant aux clefs dans les maps donnés
     */
    public static <T> Map<T, BigDecimal> add(Collection<? extends Map<T, BigDecimal>> maps) {
    	return maps.stream()
    			.flatMap(map -> map.entrySet().stream())
    			.collect(Collectors.groupingBy(e -> e.getKey(),
    					Collectors.mapping(e -> denullify(e.getValue()),
    							Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
    }

    /**
     * Faire addition de deux Map<T, BigDecimal>, elle peut etre utilisee comme une operation de reduction
     */
    public static <T> Map<T, BigDecimal> add(Map<T, BigDecimal> map1, Map<T, BigDecimal> map2) {
    	return add(Arrays.asList(map1, map2));
    }

	public static Optional<BigDecimal> addOptionals(Collection<Optional<BigDecimal>> values) {
		return values.stream()
				.filter(Optional::isPresent)
				.map(Optional::get)
				.reduce(BigDecimal::add);
	}

	public static Integer addWithCoef(Integer value1, Integer value2, int coef) {
		Integer result = 0;
		if (value2 != null) {
			result = MathUtil.add(value1, value2 * coef);
		} else if (value1 != null) {
			result = value1;
		}
		return result;
	}

	public static BigDecimal addWithCoef(BigDecimal value1, BigDecimal value2, int coef) {
		return addWithCoef(value1, value2, BigDecimal.valueOf(coef));
	}

	public static BigDecimal addWithCoef(BigDecimal value1, BigDecimal value2, BigDecimal coef) {
		BigDecimal result = BigDecimal.ZERO;
		if (value2 != null) {
			result = MathUtil.add(value1, value2.multiply(coef));
		} else if (value1 != null) {
			result = value1;
		}
		return result;
	}

	public static BigDecimal divide(BigDecimal number, BigDecimal divisor, MathContext mathContext) {
		if (number == null || divisor == null || divisor.signum() == 0) {
			return null;
		} else {
			return number.divide(divisor, mathContext);
		}
	}

	public static BigDecimal divide(BigDecimal number, BigDecimal divisor) {
		return divide(number, divisor, MATH_CONTEXT);
	}

    public static BigDecimal divideAsPercentage(BigDecimal number, BigDecimal divisor) {
        return divide(BigDecimal.valueOf(100).multiply(MathUtil.denullify(number)), MathUtil.denullify(divisor), MATH_CONTEXT);
    }

	public static BigDecimal divide(BigDecimal number, BigDecimal divisor, int scale) {
		return divide(number, divisor, new MathContext(scale, RoundingMode.HALF_UP));
	}

	public static Optional<BigDecimal> divideOptional(BigDecimal number, BigDecimal divisor) {
		return Optional.ofNullable(divide(number, divisor));
	}

    public static BigDecimal subtract(BigDecimal number1, BigDecimal number2) {
        return denullify(number1).subtract(denullify(number2));
    }

	public static int parseRomanNumber(String str) {
		int number = 0;
		if (str != null && str.length() > 0) {
			final Map<Character, Integer> valuesMap = ROMAN_VALUES;

			number = valuesMap.get(str.charAt(str.length() - 1));

			for (int i = str.length() - 2; i >= 0; i--) {
				if (valuesMap.get(str.charAt(i)) < valuesMap.get(str.charAt(i + 1))) {
					number -= valuesMap.get(str.charAt(i));
				} else {
					number += valuesMap.get(str.charAt(i));
				}
			}
		}
		return number;
	}

	public static BigDecimal getDelta(Integer value, Integer referenceValue) {
		return getDelta(asBigDecimal(value), asBigDecimal(referenceValue));
	}

	public static BigDecimal getDelta(Double value, Double referenceValue) {
		return getDelta(asBigDecimal(value), asBigDecimal(referenceValue));
	}

	public static BigDecimal getDelta(BigDecimal value, BigDecimal referenceValue) {
		return denullify(value).subtract(denullify(referenceValue));
	}

	/** Calculer taux d'evolution de value1 par rapport value2 soit value1 / value2 - 1 */
	public static Optional<BigDecimal> getTauxEvolution(Optional<BigDecimal> value1, Optional<BigDecimal> value2) {
		if (!value1.isPresent() || !value2.isPresent()) {
			return Optional.empty();
		}

		// value1 et value2 tout present
		// value1 / value2
		return value1.flatMap(v1 -> divideOptional(v1, value2.get()))
				.map(q -> q.subtract(BigDecimal.ONE));
	}

    public static BigDecimal getKEuroValue(BigDecimal in, int scale) {
        return in.divide(BigDecimal.valueOf(1000), scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal getKEuroValue(BigDecimal in) {
        return in.divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide1000(Optional<BigDecimal> value) {
    	return value.map(v -> divide(v, BigDecimal.valueOf(1000))).orElse(BigDecimal.ZERO);
	}

	public static BigDecimal divide1000(BigDecimal value) {
    	return divide(value, BigDecimal.valueOf(1000));
    }

    /**
     * Retourner un map, la clef reste inchangée, et les valeurs sont négative (opposés) des anciennes valeurs
     *
     * @param map
     * @return
     */
    public static <T> Map<T, BigDecimal> negate(Map<T, BigDecimal> map) {
    	return map.entrySet().stream()
    			.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().negate()));
    }
}
