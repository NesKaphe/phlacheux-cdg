package Animations;

public class EasingFunction {
	private int type;//type d'easing function
	
	//macro easing :
	public static final int LINEAR = 0;
	public static final int SMOOTHSTEP = 1;
	public static final int SQUARE = 2;
	public static final int CUBE = 3;
	public static final int INVERT = 4;
	public static final int INVERT_SQUARE = 5;
	public static final int INVERT_CUBE = 6;
	public static final int POWER = 7;
	public static final int POWER_SQUARE = 8;
	public static final int POWER_CUBE = 9;
	EasingFunction(int type){
		this.type = type;
	}
	
	/**
	 * x doit être compris entre 0 et 1
	 * retourne la valeur easing correspondante au x
	 * @param x
	 * @return
	 */
	public double getEasing(double x){
		switch (type) {
		case LINEAR:
			System.out.println("LINEAR");
			return x;
		case SMOOTHSTEP:
			return ((x) * (x) * (3 - 2 * (x)));
		case SQUARE :
			return x*x;
		case CUBE :
			return x*x*x;
		case INVERT:
			return (1 - x);
		case INVERT_SQUARE:
			return 1 - (1 - x) * (1 - x);
		case INVERT_CUBE:
			return 1 - (1 - x) * (1 - x) * (1 - x);
		case POWER:
			return Math.pow(x, (1-x));
		case POWER_SQUARE:
			return Math.pow(x, (1- x * x));
		case POWER_CUBE:
			return Math.pow(x, (1-x * x * x));
		default:
			return x;
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
