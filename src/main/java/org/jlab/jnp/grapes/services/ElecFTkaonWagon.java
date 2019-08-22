package org.jlab.jnp.grapes.services;

import org.jlab.jnp.hipo4.io.*;
import org.jlab.jnp.hipo4.data.*;
import org.jlab.jnp.reader.*;
import org.jlab.jnp.physics.*;
import org.jlab.jnp.utils.benchmark.*;
import org.jlab.jnp.hipo.data.HipoNode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * Skim for elec FT Kaon plus analysis.
 *
 */
public class ElecFTkaonWagon extends Wagon {

	public elecFTkaonWagon(){
        super("elecFTkaonWagon","baltzell","0.1");
    }

	@Override
	public boolean init(String jsonString) {
		System.out.println("elecFTkaonWagon READY.");
		return true;
	}

	@Override
	public boolean processDataEvent(Event event) {

		Bank bank = new Bank(reader.getSchemaFactory().getSchema("REC::Particle"));
		Bank bankRECFT = new Bank(reader.getSchemaFactory().getSchema("RECFT::Particle"));

		event.read(bank);

		event.read(bankRECFT);

		int pid = 0;
		short status = 0;

		boolean flag_FTelec = false;
		boolean flag_kp = false;

		for (int ii = 0; ii < bank.getRows(); ii++) {
			pid = bank.getInt("pid", ii);
			status = bank.getShort("status", ii);
			if (bankRECFT.getRows() > 0) {
				pid = bankRECFT.getInt("pid", ii);
				status = bankRECFT.getShort("status", ii);
			}

			if (pid == 11 && (int) (status / 1000) == 1)
				flag_FTelec = true;
			if (pid == 321)
				flag_kp = true;

		}

		if (flag_FTelec && flag_kp) {
			return true;
		} else {
			return false;
		}
	}

	}

	}

	private HashMap<Integer, ArrayList<Integer>> mapByIndex(HipoNode indices) {
		HashMap<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
		for (int ii = 0; ii < indices.getDataSize(); ii++) {
			final int index = indices.getInt(ii);
			if (!map.containsKey(index))
				map.put(index, new ArrayList<Integer>());
			map.get(index).add(ii);
		}
		return map;
	}

}