/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2001-2008, Martin Schoeberl (martin@jopdesign.com)

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package gov.nasa.jpf.symbc.realtime.jembench.lift;

import gov.nasa.jpf.symbc.Debug;

public class SimLiftIo {

	public static final int IO_BASE = 0xffffff80;

	// TAL, baseio (scio_baseio.vhd)
	public static final int IO_IN = IO_BASE+0x40+0;
	public static final int IO_LED = IO_BASE+0x40+0;
	public static final int IO_OUT = IO_BASE+0x40+1;
	public static final int IO_ADC1 = IO_BASE+0x40+1;
	public static final int IO_ADC2 = IO_BASE+0x40+2;
	public static final int IO_ADC3 = IO_BASE+0x40+3;
	
	/**
	 * Generate some dummy values to avoid optimizing
	 * this method away.
	 */
	static int cnt;
	
	private static int symID = 0;
	public static int rd(int addr) {
		return cnt+Debug.makeSymbolicInteger("READ SYMB" + symID++);//addr;
	}
	public static void wr(int val, int addr) {
		cnt += Debug.makeSymbolicInteger("WRITE SYMB" + symID++);//val + addr;
	}

}
