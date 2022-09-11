import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.AfterThrowing;
import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BodyMarker;

public class DiSLClass {

	@SyntheticLocal
	private static long bodyStart;
	
	@SyntheticLocal
	private static long methodStart; 
	
	@SyntheticLocal
	private static long methodTotalTime = 0; 

    private static final String SCOPE = "Main.interesting";

	@Before(marker = BodyMarker.class, scope = SCOPE)
	public static void beforeBody() {
		bodyStart = System.nanoTime();
	}

	@Before(marker = MethodInvocationMarker.class, scope = SCOPE)
	public static void beforeMethod() {
		methodStart = System.nanoTime(); 
	}

	@After(marker = MethodInvocationMarker.class, scope = SCOPE)
	public static void afterMethod() {
		long methodStop = System.nanoTime();
		methodTotalTime = methodTotalTime + methodStop - methodStart;
	}

	@After(marker = BodyMarker.class, scope = SCOPE)
	public static void afterBody() {
		long bodyStop = System.nanoTime();
		long bodyTime = bodyStop - bodyStart;
		System.out.println("METHOD_TOTAL_TIME= " + String.valueOf(methodTotalTime / 1000000) + "ms (" + String.valueOf(methodTotalTime) + "ns)");
		System.out.println("BODY_TIME= " + String.valueOf(bodyTime / 1000000) + "ms (" + String.valueOf(methodTotalTime) + "ns)");
		System.out.println("BODY_TIME - METHOD_TOTAL_TIME= " + String.valueOf((bodyTime - methodTotalTime) / 1000000) + "ms (" + String.valueOf(bodyTime - methodTotalTime) + "ns)");
	}
}
