import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterThrowing;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BodyMarker;

/**
 * <p>
 * This example shows how to insert snippets at various regions and entries or exits of these regions.
 * 
 * <p>
 * It also shows how to implements custom code marker.
 */
public class DiSLClass {

	@SyntheticLocal
	static long mainStart;
	
	@SyntheticLocal 
	static long stringStart; 
	
	@SyntheticLocal 
	static long stringStop; 

	/**
	 * <p>
	 * This is added before every method call in Main.worker method.
	 */
	@Before(marker = MethodInvocationMarker.class, scope = "Main.worker")
	public static void beforeInvocation() {
		System.out.println("disl: before invocation");
		mainStart = System.nanoTime(); 
	}

	/**
	 * <p>
	 * This is added after every method call in Main.worker method.
	 */
	@After(marker = MethodInvocationMarker.class, scope = "Main.worker")
	public static void afterInvocation() {
		long mainStop = System.nanoTime();
		System.out.println("disl: after invocation");
		System.out.println("\tMain method invocation took " + String.valueOf(mainStop - mainStart) + "ns to complete and " + String.valueOf((stringStart - mainStart) + (mainStop - stringStop)) + "ns to complete without the nested string method call");
	}

	/**
	 * <p>
	 * This is added before every method matching Main.string* no matter how it
	 * ends (throw or no throw).
	 */
	@Before(marker = BodyMarker.class, scope = "Main.string*")
	public static void beforeMethod() {
		System.out.println("disl: before method Main.string*");
		stringStart = System.nanoTime();
	}

	/**
	 * <p>
	 * This is added after every method matching Main.string* no matter how it
	 * ends (throw or no throw).
	 */
	@After(marker = BodyMarker.class, scope = "Main.string*")
	public static void afterMethod() {
		System.out.println("disl: after method Main.string*");
		stringStop = System.nanoTime();
		System.out.println("\tNested method invocation took " + String.valueOf(stringStop - stringStart) + "ns to complete");
		stringStop = System.nanoTime();
	}
}
