package com.ezzenix.smoothgui.config;

import java.util.function.Function;

import static java.lang.Math.*;

public enum EasingStyle {
	FLAT(x -> x),
	SINE(x -> 0.5 - cos(x * PI) / 2),
	QUAD(x -> x * x),
	CUBIC(x -> x * x * x),
	QUART(x -> x * x * x * x),
	QUINT(x -> x * x * x * x * x),
	EXPO(x -> x == 0 ? 0 : pow(2, 10 * x - 10)),
	CIRC(x -> 1 - sqrt(1 - pow(x, 2))),
	BACK(x -> 2.70158 * x * x * x - 1.70158 * x * x),
	ELASTIC(x -> x == 0 ? 0 : x == 1 ? 1 : -pow(2, 10 * x - 10) * sin((x * 10 - 10.75) * ((2 * PI) / 3)));

	final Function<Double, Number> function;

	EasingStyle(Function<Double, Number> function) {
		this.function = function;
	}

	public Double apply(Double x) {
		return function.apply(x).doubleValue();
	}

	public Double applyReverse(Double x) {
		return 1 - function.apply(1 - x).doubleValue();
	}
}
