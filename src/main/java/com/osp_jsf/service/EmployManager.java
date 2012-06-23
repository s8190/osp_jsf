package com.osp_jsf.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.osp_jsf.domain.Employ;

@ApplicationScoped
public class EmployManager {
	private List<Employ> db = new ArrayList<Employ>();

	public void addEmploy(Employ employ) {
		Employ newEmploy = new Employ();

		newEmploy.setFirstName(employ.getFirstName());
		newEmploy.setZipCode(employ.getZipCode());
		newEmploy.setPin(employ.getPin());
		newEmploy.setDateOfBirth(employ.getDateOfBirth());
		newEmploy.setMarried(employ.isMarried());
		newEmploy.setWeight(employ.getWeight());
		newEmploy.setNumOfChildren(employ.getNumOfChildren());

		db.add(newEmploy);
	}

	// Removes the employ with given PIN
	public void deleteEmploy(Employ employ) {
		Employ employToRemove = null;
		for (Employ p : db) {
			if (employ.getPin().equals(p.getPin())) {
				employToRemove = p;
				break;
			}
		}
		if (employToRemove != null)
			db.remove(employToRemove);
	}

	public List<Employ> getAllEmploys() {
		return db;
	}

	public List<Employ> searchEmploy(Employ c) {
			List<Employ> cl = new ArrayList<Employ>();
			for (Employ p : db) {
				if (c.getPin().equals(p.getPin()) || c.getFirstName().equals(p.getFirstName())) {
				cl.add(p);
				}
			}
			return cl;
	}
}
