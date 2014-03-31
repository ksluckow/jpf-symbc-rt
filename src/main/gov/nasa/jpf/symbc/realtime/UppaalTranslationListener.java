/**
 * 
 */
package gov.nasa.jpf.symbc.realtime;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import uppaal.NTA;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.bytecode.FieldInstruction;
import gov.nasa.jpf.jvm.bytecode.GETFIELD;
import gov.nasa.jpf.jvm.bytecode.GETSTATIC;
import gov.nasa.jpf.jvm.bytecode.PUTFIELD;
import gov.nasa.jpf.jvm.bytecode.PUTSTATIC;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.symbc.realtime.loopbounds.LoopBound;
import gov.nasa.jpf.symbc.realtime.loopbounds.LoopBoundExtractor;
import gov.nasa.jpf.symbc.realtime.optimization.RTOptimizer;
import gov.nasa.jpf.symbc.realtime.optimization.SeqInstructionReduction;
import gov.nasa.jpf.symbc.realtime.rtsymexectree.jop.JOPNodeFactory;
import gov.nasa.jpf.symbc.realtime.rtsymexectree.platformagnostic.PlatformAgnosticTimingNodeFactory;
import gov.nasa.jpf.symbc.realtime.rtsymexectree.platformagnostic.PlatformAgnosticTimingStdNode;
import gov.nasa.jpf.symbc.realtime.rtsymexectree.timingdoc.TimingDocNodeFactory;
import gov.nasa.jpf.symbc.symexectree.ASymbolicExecutionTreeListener;
import gov.nasa.jpf.symbc.symexectree.InstrContext;
import gov.nasa.jpf.symbc.symexectree.NodeFactory;
import gov.nasa.jpf.symbc.symexectree.SymExecTreeUtils;
import gov.nasa.jpf.symbc.symexectree.structure.SymbolicExecutionTree;
import gov.nasa.jpf.util.ObjectList;
import gov.nasa.jpf.util.Source;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

/**
 * @author Kasper S. Luckow <luckow@cs.aau.dk>
 *
 */
public class UppaalTranslationListener extends ASymbolicExecutionTreeListener {
	/**
	 * This is the listener used for translating the symbolic execution tree generated
	 * by SPF to a timed automaton amenable to model checking using UPPAAL.
	 * The configurations for this listener are:
	 * 
	 * symbolic.realtime.platform 			=	[jop|agnostic|timingdoc]	(default: jop)
	 * symbolic.realtime.targetsymrt	 	=	[true|false]				(default: false)
	 * symbolic.realtime.outputbasepath 	=	<output path>				(default: ./)
	 * symbolic.realtime.optimize 			= 	[true|false]				(default: true)
	 * symbolic.realtime.progressmeasure	= 	[true|false]				(default: true)
	 * symbolic.realtime.generatequeries 	= 	[true|false]				(default: true)
	 * 
	 * If the target platform is 'timingdoc', a Timing Doc - describing the execution
	 * times of the individual Java Bytecodes of the particular platform - must be
	 * supplied as well using:
	 * 
	 * symbolic.realtime.timingdocpath = <source path>
	 */
	
	private static final String DEF_OUTPUT_PATH = "./";
	private String targetPlatform;
	private boolean targetSymRT;
	private final boolean optimize;
	private String outputBasePath;
	private final boolean generateQueries;
	private final boolean useProgressMeasure;
	private int injectedSymbID;
	
	private HashSet<ElementInfo> visitedEi = new HashSet<ElementInfo>();

	public UppaalTranslationListener(Config conf, JPF jpf) {
		super(conf, jpf);
		this.targetSymRT = conf.getBoolean("symbolic.realtime.targetsymrt", false);
		this.optimize = conf.getBoolean("symbolic.realtime.optimize", true);
		this.outputBasePath = conf.getString("symbolic.realtime.outputbasepath", UppaalTranslationListener.DEF_OUTPUT_PATH);
		this.generateQueries = conf.getBoolean("symbolic.realtime.generatequeries", !this.targetSymRT);
		this.useProgressMeasure = conf.getBoolean("symbolic.realtime.progressmeasure", !this.targetSymRT);
		if(this.useProgressMeasure && this.targetSymRT)
			throw new RealTimeRuntimeException("Progress measures are currently not supported in SymRT");
		this.injectedSymbID = 0;
	}

	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
		if (!vm.getSystemState().isIgnored()) {
			MethodInfo mi = executedInstruction.getMethodInfo();
			if(SymExecTreeUtils.isInSymbolicCallChain(mi, currentThread.getTopFrame(), this.jpfConf)) {
				if(executedInstruction.isBackJump()) {
					handleLoop(executedInstruction, vm);
				}
			}
		}
	}
	
	
	@Override
	public void executeInstruction(VM vm, ThreadInfo currentThread, Instruction instructionToExecute) {
		super.executeInstruction(vm, currentThread, instructionToExecute);
		if (!vm.getSystemState().isIgnored()) {
			MethodInfo mi = instructionToExecute.getMethodInfo();
			if(SymExecTreeUtils.isInSymbolicCallChain(mi, currentThread.getTopFrame(), this.jpfConf)) {
				if(instructionToExecute instanceof GETFIELD ||
					instructionToExecute instanceof GETSTATIC) {
						ThreadInfo ti = vm.getCurrentThread();
						if(ti.getTopFrame() != null) {
							if(ti.getTopFrame().getSlots().length > 0) {
								FieldInstruction fieldInstr = (FieldInstruction) instructionToExecute;
								ElementInfo ei = fieldInstr.peekElementInfo(ti);
								if(ei != null) {
									if(ei.isShared()) {
										if(!ei.hasFieldAttr(Expression.class) && !ei.isFrozen()) {//Assuming the if the field already has an attr of type Expression, it is symbolic.
											FieldInfo fi = fieldInstr.getFieldInfo();
											ei.addFieldAttr(fi, new SymbolicInteger("SHARED SYMB " + this.injectedSymbID++));
										}
									}
								}
							}
						}
					}

				if(instructionToExecute instanceof PUTFIELD ||
				   instructionToExecute instanceof PUTSTATIC) {
					ThreadInfo ti = vm.getCurrentThread();
					FieldInstruction putInstr = (FieldInstruction) instructionToExecute;
					
					ElementInfo eiOwner = putInstr.peekElementInfo(ti);
					FieldInfo fi = putInstr.getFieldInfo();
					if(fi.isReference() && eiOwner.isShared()) {
						int objRef = ti.getTopFrame().peek();
						if(objRef == -1) {
						} else {
							ElementInfo ei = ti.getElementInfo(objRef);
							this.setSharedness(ei, ti);
						}
					}					
				}
			}
		}
	}
	
	private void setSharedness(ElementInfo ei, ThreadInfo ti) {
		this.visitedEi.clear();
		recursivelySetSharedness(ei, ti);
	}
	

	
	private void recursivelySetSharedness(ElementInfo ei, ThreadInfo ti) {
		if(visitedEi.contains(ei))
			return;
		ClassInfo ci = ei.getClassInfo();
		FieldInfo[] fis = ci.getDeclaredInstanceFields();
		
		for(FieldInfo fi : fis) {
			if(fi.isReference()) {
				int objRef = ei.getReferenceField(fi);
				if(objRef == -1)
					continue;
				ElementInfo thisEi = ti.getElementInfo(objRef);
				if(thisEi == null)
					throw new RuntimeException("ElementInfo is null!");
				thisEi.setShared(true);
				visitedEi.add(thisEi);
				recursivelySetSharedness(thisEi, ti);
			}
		}
	}

	@Override
	public void searchConstraintHit(Search search) {
		if (!search.isEndState() && !search.isErrorState()) {
			String searchDepth = super.jpfConf.getString("search.depth_limit");
			System.err.println("Warning: Search depth " + searchDepth + " has been hit! You may want to increase the bound or adjust loop bounds. Otherwise, the timing model is possibly unsafe!");
		}
	}
	
	private static class LoopProcessedMarker { public boolean containedBound;}
	private void handleLoop(Instruction instr, VM vm) {
		//Maybe this is a hack...
		if(!instr.hasAttr(LoopProcessedMarker.class)) {
			String fileLocation = instr.getFileLocation();
			fileLocation = fileLocation.substring(0, fileLocation.indexOf(':'));
			LoopBound lb = new LoopBound(LoopBoundExtractor.extractBound(fileLocation, instr.getLineNumber()));
			LoopProcessedMarker marker = new LoopProcessedMarker();
			if(lb.getLoopBound() >= 0) {
				instr.addAttr(lb);
				marker.containedBound = true;
			} else
				marker.containedBound = false;
			instr.addAttr(marker);
		}
		if(instr.getAttr(LoopProcessedMarker.class).containedBound) {
			LoopBound lb = instr.getAttr(LoopBound.class);
			int newBound = lb.getLoopBound() - 1;
			if (newBound <= 0){
				vm.getSystemState().setIgnored(true);
			}
			lb.setLoopBound(newBound);
		}
	}
	
	@Override
	protected NodeFactory getNodeFactory() {
		this.targetPlatform = super.jpfConf.getString("symbolic.realtime.platform", "").toLowerCase();
		switch(this.targetPlatform) {
			case "jop":
				return new JOPNodeFactory();
			case "agnostic":
				return new PlatformAgnosticTimingNodeFactory();
			case "timingdoc":
				String timingDocPath = super.jpfConf.getString("symbolic.realtime.timingdocpath");
				if(timingDocPath == null) 
					throw new TimingDocException("symbolic.realtime.timingdocpath has not been set.");
				TimingDoc tDoc = TimingDocGenerator.generate(timingDocPath);
				return new TimingDocNodeFactory(tDoc);
			default:
				System.out.println("Default platform JOP is used");
				return new JOPNodeFactory();
		}
	}

	@Override
	protected void processSymbExecTree(LinkedList<SymbolicExecutionTree> trees) {
		if(trees.isEmpty())
			throw new UppaalTranslatorException("No symbolic execution trees were generated! Have you set the target method correctly?");
		UppaalTranslator translator = new UppaalTranslator(this.targetSymRT, this.useProgressMeasure);
		RTOptimizer optimizer = null;
		if(this.optimize) {
			optimizer = new RTOptimizer();
			optimizer.addOptimization(new SeqInstructionReduction(this.targetSymRT));
		}
		for(SymbolicExecutionTree tree : trees) {
			if(this.optimize)
				optimizer.optimize(tree);
			NTA ntaSystem = translator.translateSymTree(tree);
			ntaSystem.writePrettyLayoutModelToFile(this.getNTAFileName(ntaSystem, tree));
			if(this.generateQueries)
				QueriesFileGenerator.writeQueriesFile(ntaSystem, getQueriesFileName(ntaSystem, tree));
		}
	}
	
	private String getNTAFileName(NTA nta, SymbolicExecutionTree tree) {
		return this.getBaseFileName(nta, tree) + ".xml";
	}
	
	private String getQueriesFileName(NTA nta, SymbolicExecutionTree tree) {
		return this.getBaseFileName(nta, tree) + ".q";
	}
	private String getBaseFileName(NTA nta, SymbolicExecutionTree tree) {
		return outputBasePath + (outputBasePath.endsWith("/") ? "" : "/") + tree.getTargetMethod().getMethodName() + "_SPF";
	}
}
