package ntu.sce.cz4015;

public class RandomNumberGenerator {
	
	private final double carInterArrivalTimeMean = 1.36981692647652;
	private final double callDurationMean = 99.835900738747;
	private final double velocityMean = 120.072098016858;
	private final double velocityVariance = 9.01905789789691;
	
	public double carInterArrival() {
		return ExponentialRN(carInterArrivalTimeMean);
	}

	public int baseStation() {
		return (int) Math.ceil(UniformRN(0, 20));
	}

	public double positionInBaseStation() {
		return UniformRN(0, 2000); // 2km = 2000m
	}

	public double callDuration() {
		return 10 + ExponentialRN(callDurationMean);
	}

	public double velocity() {
		return NormalRN(velocityMean, velocityVariance);
	}

	private double ExponentialRN(double beta) {
		// f(x) = (1/b)*e^(-x/b)
		// F(x) = 1-e^(-x/b)
		// X = -b*ln(1-U)
		double RN = 0;
		double U = Math.random();
		RN = (-beta) * Math.log(1 - U);
		return RN;
	}

	private double UniformRN(double a, double b) {
		// f(x) = 1/(b-a)
		// F(x) = (x-a)/(b-a)
		// X = (b-a)*U+a
		double RN = 0;
		double U = Math.random();
		RN = (b - a) * U + a;
		return RN;
	}

	private double NormalRN(double mean, double dev) {
		// Z=(x-mean)/dev
		// Z~N(0,1)
		// according to CLT, can use U1+U2+...+U12-6 to approximate Z~N(0,1)
		double RN = 0;
		double U, sum = 0;
		for (int i = 0; i < 12; i++) {
			U = Math.random();
			sum += U;
		}
		double z = sum - 6;
		RN = z * dev + mean;
		return RN;
	}
}
