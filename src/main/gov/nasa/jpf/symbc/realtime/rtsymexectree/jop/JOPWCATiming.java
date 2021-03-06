/**
 * 
 */
package gov.nasa.jpf.symbc.realtime.rtsymexectree.jop;

import gov.nasa.jpf.jvm.bytecode.ARETURN;
import gov.nasa.jpf.jvm.bytecode.DRETURN;
import gov.nasa.jpf.jvm.bytecode.FRETURN;
import gov.nasa.jpf.jvm.bytecode.IRETURN;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;
import gov.nasa.jpf.jvm.bytecode.LRETURN;
import gov.nasa.jpf.jvm.bytecode.ReturnInstruction;
import gov.nasa.jpf.symbc.realtime.InstructionNotImplementedException;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;

/**
 * @author Kasper S. Luckow <luckow@cs.aau.dk>
 *
 */
public class JOPWCATiming extends JOPTiming {
	
	public JOPWCATiming(int ram_cnt) {
		super(ram_cnt);
	}
	
	public JOPWCATiming(int rws, int wws) {
		super(rws, wws);
	}
	
	@Override
	protected int getCacheLoadCycles() {
		/*
		 * This is the WCA-tool way of setting the cache load cycles which is not in accordance with jop handbook!
		 * NOTE: this is not documented in MS thesis!
		 */
		c = r > 0 ? r - 1 : 0;
		return c;
	}
	
	@Override
	public int calculateCacheLoadTime(InvokeInstruction instr, MethodInfo method, boolean cacheHit) {
		/*This is the cache load time according to the WCA tool (which does not correspond to the description in JOP Handbook)
		 * NOTE: this is not documented in MS thesis!
		 */
		int n = this.getMethodSize(method);
		int cacheLoadCost = (cacheHit) ? 4 : 6 + (n + 1) * (2 + c);
		return (cacheLoadCost >= 39) ? cacheLoadCost - 39 : 0;
	}
	
	@Override
	public int calculateCacheLoadTime(ReturnInstruction instr, MethodInfo method, boolean cacheHit) {
		/*This is the cache load cost according to the WCA tool (which does not correspond to the description in JOP Handbook)
		 * NOTE: this is not documented in MS thesis, but only in WCA tool!
		 */
		int n = this.getMethodSize(method);
		int cacheLoadCost = (cacheHit) ? 4 : 6 + (n + 1) * (2 + c);
		
		if (instr instanceof IRETURN || instr instanceof FRETURN
				|| instr instanceof ARETURN) {
			return ((cacheLoadCost >= 8) ? cacheLoadCost - 8 : 0); // from MS thesis
		} else if (instr instanceof LRETURN || instr instanceof DRETURN) {
			return ((cacheLoadCost >= 9) ? cacheLoadCost - 9 : 0); // from MS thesis
		} else { // Must be RETURN
			return ((cacheLoadCost >= 7) ? cacheLoadCost - 7 : 0); // from MS thesis
		}
	}
	
	@Override
	public int getWCET(Instruction instruction) throws InstructionNotImplementedException {
		int wcet = 0;
		int opcode = instruction.getByteCode();
		
		switch (opcode) {
		case (0):
			wcet = 1;
			break;
		case (1):
			wcet = 1;
			break;
		case (2):
			wcet = 1;
			break;
		case (3):
			wcet = 1;
			break;
		case (4):
			wcet = 1;
			break;
		case (5):
			wcet = 1;
			break;
		case (6):
			wcet = 1;
			break;
		case (7):
			wcet = 1;
			break;
		case (8):
			wcet = 1;
			break;
		case (9):
			wcet = 2;
			break;
		case (10):
			wcet = 2;
			break;
		case (11):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (12):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (13):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (14):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (15):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (16):
			wcet = 2;
			break;
		case (17):
			wcet = 3;
			break;
		case (18):
			wcet = 3 + r;
			break;
		case (19):
			wcet = 4 + r;
			break;
		case (20):
			wcet = 8 + r + ((r >= 6) ? r - 2 : 4);
			break;
		case (21):
			wcet = 2;
			break;
		case (22):
			wcet = 11;
			break;
		case (23):
			wcet = 2;
			break;
		case (24):
			wcet = 11;
			break;
		case (25):
			wcet = 2;
			break;
		case (26):
			wcet = 1;
			break;
		case (27):
			wcet = 1;
			break;
		case (28):
			wcet = 1;
			break;
		case (29):
			wcet = 1;
			break;
		case (30):
			wcet = 2;
			break;
		case (31):
			wcet = 2;
			break;
		case (32):
			wcet = 2;
			break;
		case (33):
			wcet = 11;
			break;
		case (34):
			wcet = 1;
			break;
		case (35):
			wcet = 1;
			break;
		case (36):
			wcet = 1;
			break;
		case (37):
			wcet = 1;
			break;
		case (38):
			wcet = 2;
			break;
		case (39):
			wcet = 2;
			break;
		case (40):
			wcet = 2;
			break;
		case (41):
			wcet = 11;
			break;
		case (42):
			wcet = 1;
			break;
		case (43):
			wcet = 1;
			break;
		case (44):
			wcet = 1;
			break;
		case (45):
			wcet = 1;
			break;
		case (46):
			wcet = 19 + r + ((r >= 6) ? r - 2 : 4);
			break;
		case (47):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (48):
			wcet = 19 + r + ((r >= 6) ? r - 2 : 4);
			break;
		case (49):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (50):
			wcet = 19 + r + ((r >= 6) ? r - 2 : 4);
			break;
		case (51):
			wcet = 19 + r + ((r >= 6) ? r - 2 : 4);
			break;
		case (52):
			wcet = 19 + r + ((r >= 6) ? r - 2 : 4);
			break;
		case (53):
			wcet = 19 + r + ((r >= 6) ? r - 2 : 4);
			break;
		case (54):
			wcet = 2;
			break;
		case (55):
			wcet = 11;
			break;
		case (56):
			wcet = 2;
			break;
		case (57):
			wcet = 11;
			break;
		case (58):
			wcet = 2;
			break;
		case (59):
			wcet = 1;
			break;
		case (60):
			wcet = 1;
			break;
		case (61):
			wcet = 1;
			break;
		case (62):
			wcet = 1;
			break;
		case (63):
			wcet = 2;
			break;
		case (64):
			wcet = 2;
			break;
		case (65):
			wcet = 2;
			break;
		case (66):
			wcet = 11;
			break;
		case (67):
			wcet = 1;
			break;
		case (68):
			wcet = 1;
			break;
		case (69):
			wcet = 1;
			break;
		case (70):
			wcet = 1;
			break;
		case (71):
			wcet = 2;
			break;
		case (72):
			wcet = 2;
			break;
		case (73):
			wcet = 2;
			break;
		case (74):
			wcet = 11;
			break;
		case (75):
			wcet = 1;
			break;
		case (76):
			wcet = 1;
			break;
		case (77):
			wcet = 1;
			break;
		case (78):
			wcet = 1;
			break;
		case (79):
			wcet = 22 + w + ((r >= 6) ? r - 2 : 4);
			break;
		case (80):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (81):
			wcet = 22 + w + ((r >= 6) ? r - 2 : 4);
			break;
		case (82):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (83):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (84):
			wcet = 22 + w + ((r >= 6) ? r - 2 : 4);
			break;
		case (85):
			wcet = 22 + w + ((r >= 6) ? r - 2 : 4);
			break;
		case (86):
			wcet = 22 + w + ((r >= 6) ? r - 2 : 4);
			break;
		case (87):
			wcet = 1;
			break;
		case (88):
			wcet = 2;
			break;
		case (89):
			wcet = 1;
			break;
		case (90):
			wcet = 5;
			break;
		case (91):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (92):
			wcet = 6;
			break;
		case (93):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (94):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (95):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (96):
			wcet = 1;
			break;
		case (97):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (98):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (99):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (100):
			wcet = 1;
			break;
		case (101):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (102):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (103):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (104):
			wcet = 35;
			break;
		case (105):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (106):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (107):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (108):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (109):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (110):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (111):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (112):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (113):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (114):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (115):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (116):
			wcet = 4;
			break;
		case (117):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (118):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (119):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (120):
			wcet = 1;
			break;
		case (121):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (122):
			wcet = 1;
			break;
		case (123):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (124):
			wcet = 1;
			break;
		case (125):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (126):
			wcet = 1;
			break;
		case (127):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (128):
			wcet = 1;
			break;
		case (129):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (130):
			wcet = 1;
			break;
		case (131):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (132):
			wcet = 11;
			break;
		case (133):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (134):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (135):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (136):
			wcet = 3;
			break;
		case (137):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (138):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (139):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (140):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (141):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (142):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (143):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (144):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (145):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (146):
			wcet = 2;
			break;
		case (147):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (148):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (149):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (150):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (151):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (152):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (153):
			wcet = 4;
			break;
		case (154):
			wcet = 4;
			break;
		case (155):
			wcet = 4;
			break;
		case (156):
			wcet = 4;
			break;
		case (157):
			wcet = 4;
			break;
		case (158):
			wcet = 4;
			break;
		case (159):
			wcet = 4;
			break;
		case (160):
			wcet = 4;
			break;
		case (161):
			wcet = 4;
			break;
		case (162):
			wcet = 4;
			break;
		case (163):
			wcet = 4;
			break;
		case (164):
			wcet = 4;
			break;
		case (165):
			wcet = 4;
			break;
		case (166):
			wcet = 4;
			break;
		case (167):
			wcet = 4;
			break;
		case (168):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (169):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (170):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (171):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (172):
			wcet = 15 + ((r >= 7) ? r-3 : 4); //NOTE: This is WITHOUT the cache load cost. Add the value of calculateCacheLoadTime()
			break;
		case (173):
			wcet = 15 + ((r >= 7) ? r-3 : 4); //NOTE: This is WITHOUT the cache load cost. Add the value of calculateCacheLoadTime()
			break;
		case (174):
			wcet = 15 + ((r >= 7) ? r-3 : 4); //NOTE: This is WITHOUT the cache load cost. Add the value of calculateCacheLoadTime()
			break;
		case (175):
			wcet = 15 + ((r >= 7) ? r-3 : 4); //NOTE: This is WITHOUT the cache load cost. Add the value of calculateCacheLoadTime()
			break;
		case (176):
			wcet = 15 + ((r >= 7) ? r-3 : 4); //NOTE: This is WITHOUT the cache load cost. Add the value of calculateCacheLoadTime()
			break;
		case (177):
			wcet = 13 + ((r >= 7) ? r-3 : 4); //NOTE: This is WITHOUT the cache load cost. Add the value of calculateCacheLoadTime()
			break;
		case (178):
			wcet = 4 + 2 * r;
			break;
		case (179):
			wcet = 5 + r + w;
			break;
		case (180):
			wcet = 10 + 2 * r;
			break;
		case (181):
			wcet = 13 + r + w;
			break;
		case (182):
			wcet = 78 + (2 * r) + ((r >= 7) ? r - 3 : 4) + ((r >= 6) ? r - 2 : 4); //NOTE: This is WITHOUT the cache load cost. Add the value of calculateCacheLoadTime()
			break;
		case (183):
			wcet = 58 + r + ((r >= 7) ? r - 3 : 4) + ((r >= 6) ? r - 2 : 4); //NOTE: This is WITHOUT the cache load cost. Add the value of calculateCacheLoadTime()
			break;
		case (184):
			wcet = 58 + r + ((r >= 7) ? r - 3 : 4) + ((r >= 6) ? r - 2 : 4); //NOTE: This is WITHOUT the cache load cost. Add the value of calculateCacheLoadTime()
			break;
		case (185):
			wcet = 84 + (4 * r) + ((r >= 7) ? r - 3 : 4) + ((r >= 6) ? r - 2 : 4); //NOTE: This is WITHOUT the cache load cost. Add the value of calculateCacheLoadTime()
			break;
		case (186):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (187):
			wcet=100;
			System.err.println("wcet not known for [" + instruction.getMnemonic() + "]. Using default: "+ 100 +" cycles");
			//throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					//+ "]");
			break;
		case (188):
			wcet=100;
			System.err.println("wcet not known for [" + instruction.getMnemonic() + "]. Using default: "+ 100 +" cycles");
			//throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					//+ "]");
			break;
			//throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic() + "]");
		case (189):
			wcet=100;
			System.err.println("wcet not known for [" + instruction.getMnemonic() + "]. Using default: "+ 100 +" cycles");
			//throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					//+ "]");
			break;
		case (190):
			wcet = 2 + r;
			break;
		case (191):
			wcet=100;
			System.err.println("wcet not known for [" + instruction.getMnemonic() + "]. Using default: "+ 100 +" cycles");
			//throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					//+ "]");
			break;
			//throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic() + "]");
			//break;
		case (192):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (193):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (194):
			wcet = 9;
			break;
		case (195):
			wcet = 11; // MS thesis p. 240 in pdf says 10/11 cycles here
			break;
		case (196):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (197):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (198):
			wcet = 4;
			break;
		case (199):
			wcet = 4;
			break;
		case (200):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (201):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (202):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (203):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (204):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (205):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (206):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (207):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (208):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (209):
			wcet = 3;
			break;
		case (210):
			wcet = 3;
			break;
		case (211):
			wcet = r;
			break;
		case (212):
			wcet = w + 1;
			break;
		case (213):
			wcet = 8;
			break;
		case (214):
			wcet = 8;
			break;
		case (215):
			wcet = 3;
			break;
		case (216):
			wcet = 4;
			break;
		case (217):
			wcet = 1;
			break;
		case (218):
			wcet = 2;
			break;
		case (219):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
			// n = STACK_SIZE - STACK_OFF which is the max stack size.
			//wcet = 14 + r + n * (23 + ((w <= 8) ? 0 : w - 8));
			//break;
		case (220):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
			// n = STACK_SIZE - STACK_OFF which is the max stack size. 
			//wcet = 14 + r + n * (23 + ((r <= 10) ? 0 : w - 10));
			//break;
		case (221):
			wcet = 1;
			break;
		case (222):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (223):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (224):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (225):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (226):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (227):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (228):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (229):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (230):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (231):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (232):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (233):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (234):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (235):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (236):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (237):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (238):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (239):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (240):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (241):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (242):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (243):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (244):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (245):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (246):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (247):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (248):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (249):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (250):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (251):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (252):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (253):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (254):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		case (255):
			throw new InstructionNotImplementedException("wcet not known for [" + instruction.getMnemonic()
					+ "]");
		default:
			//System.err.println("Unknown opcode: (" + opcode + ") for bytecode: " + instruction.getMnemonic() + " appearing: " + instruction.getFileLocation());
			//System.err.println("Warning: defaulting to WCET = 1");
		}
		return wcet;
	}
}
