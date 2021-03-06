/**
 * 
 */
package gov.nasa.jpf.symbc.realtime.rtsymexectree.jop;

import gov.nasa.jpf.jvm.bytecode.ReturnInstruction;
import gov.nasa.jpf.symbc.realtime.rtsymexectree.RTStdNode;
import gov.nasa.jpf.symbc.symexectree.InstrContext;
import gov.nasa.jpf.symbc.symexectree.structure.SymbolicExecutionTree;
import gov.nasa.jpf.vm.Instruction;

/**
 * @author Kasper S. Luckow <luckow@cs.aau.dk>
 *
 */
public class JOPStdNode extends RTStdNode implements IJOPRealTimeNode {
	
	private int wcet;
	
	public JOPStdNode(InstrContext instructionContext, JOPTiming jopTiming) {
		this(instructionContext, jopTiming, null);
	}

	public JOPStdNode(InstrContext instructionContext, JOPTiming jopTiming, SymbolicExecutionTree tree) {
		super(instructionContext, tree);
		Instruction instr = instructionContext.getInstr();
		this.wcet = jopTiming.getWCET(instr);
	}

	@Override
	public int getWCET() {
		return this.wcet;
	}

	@Override
	public void setWCET(int wcet) {
		this.wcet = wcet;
	}

	@Override
	public boolean isReducible() {
		return true;
	}
}
