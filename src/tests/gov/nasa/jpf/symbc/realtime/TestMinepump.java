/**
 * 
 */
package gov.nasa.jpf.symbc.realtime;

import gov.nasa.jpf.symbc.InvokeTest;
import gov.nasa.jpf.symbc.realtime.minepump.scj.PeriodicMethaneDetectionEventHandler;

import org.junit.Test;

/**
 * @author Kasper S. Luckow <luckow@cs.aau.dk>
 *
 */
public class TestMinepump extends InvokeTest {
	private static final String SYM_METHOD = "+symbolic.method=gov.nasa.jpf.symbc.realtime.minepump.scj.PeriodicMethaneDetectionEventHandler.run()";
	private static final String LISTENER = "+listener = gov.nasa.jpf.symbc.realtime.UppaalTranslationListener";

	//Real time config:
	private static final String CLPATH = "+classpath=${jpf-symbc}/lib/scjNoRelativeTime_1.0.0.jar";
	private static final String REALTIME = "+symbolic.realtime = true";
	private static final String SYMRT = "+symbolic.realtime.targetsymrt = false";
	private static final String REALTIME_PLATFORM = "+symbolic.realtime.platform = jop";
	private static final String OPTIMIZE = "+symbolic.realtime.optimize = true";
	private static final String REALTIME_PATH = "+symbolic.realtime.outputbasepath = ${jpf-symbc-rt}/";
	
	private static final String[] JPF_ARGS = {INSN_FACTORY, 
											  LISTENER, 
											  SYM_METHOD, 
											  CLPATH,
											  REALTIME,
											  SYMRT,
											  REALTIME_PLATFORM,
											  REALTIME_PATH,
											  OPTIMIZE};
	@Test
	public void mainTest() {
		if (verifyNoPropertyViolation(JPF_ARGS)) {
			PeriodicMethaneDetectionEventHandler.main(null);
		}
	}
}
