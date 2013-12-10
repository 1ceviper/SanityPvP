package net.iceviper.flyphants.sanitypvp;

public class MathShiz {
	public static double erf(double z) {
		double t = 1.0 / (1.0 + 0.47047 * Math.abs(z));
		double poly = t * (0.3480242 + t * (-0.0958798 + t * (0.7478556)));
		double ans = 1.0 - poly * Math.exp(-z*z);
		if (z >= 0)
			return  ans;
		else
			return -ans;
	}
}
