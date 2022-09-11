import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

	private static final String METHOD = "stringUnitChange";
	
	public static void stringChange(String inputString) {
		inputString = inputString + "Changed";
	}

	public static void stringUnitChange(String inputString) {
		inputString = inputString + "C" + "h" + "a" + "n" + "g" + "e" + "d";
	}

	public static void stringClear(String inputString) {
		inputString = "";
	}

	public static void stringNullify(String inputString) {
		inputString = null;
	}

	public static void stringUpperCase(String inputString) {
		inputString = inputString.toUpperCase();
	}

	public static void stringLowerCase(String inputString) {
		inputString = inputString.toLowerCase();
	}

	public static void worker(Method method) throws IllegalAccessException, InvocationTargetException {
		// create String object
		String myString = "myString";

		// invoke METHOD method
		method.invoke(null, myString);

		// reinitialize String object
		myString = "myStringAgain";
	}

	public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		// get reference to nested string method based on METHOD compile-time constant
		Method method = Main.class.getMethod(METHOD, String.class);
		// call worker with METHOD method
		worker(method);
	}
}
