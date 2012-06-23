package com.osp_jsf.web;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.ListDataModel;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;


import com.osp_jsf.domain.Employ;
import com.osp_jsf.service.EmployManager;

@SessionScoped
@Named("employBean")
public class EmployFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Employ employ = new Employ();
	private String text = null;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private ListDataModel<Employ> employs = new ListDataModel<Employ>();
	
	@Inject
	private EmployManager pm;

	public Employ getEmploy() {
		return employ;
	}

	public void setEmploy(Employ employ) {
		this.employ = employ;
	}

	public ListDataModel<Employ> getAllEmploys() {
		employs.setWrappedData(pm.getAllEmploys());
		return employs;
	}
	
	public ListDataModel<Employ> getEmploySearch() {
		employs.setWrappedData(pm.searchEmploy(employ));
		return employs;
	}

	// Actions
	public String addEmploy() {
		pm.addEmploy(employ);
		return "showDetalis";
		//return null;
	}
	public String showEmploy() {
		return "showEmploys";
		//return null;
	}
	public String Back() {
		pm.deleteEmploy(employ);
		return "addSimple";
		//return null;
	}
	public String searchEmploy() {
		employ.setPin(text);
		employ.setFirstName(text);
		return "searchResult";
		 }
	
	public String editEmploy() {
		Employ employToEdit = employs.getRowData();
		pm.deleteEmploy(employToEdit);
		return "addSimple";
		 }


	public String deleteEmploy() {
		Employ employToDelete = employs.getRowData();
		pm.deleteEmploy(employToDelete);
		return null;
	}

	// Validators

	// Business logic validation
	public void uniquePin(FacesContext context, UIComponent component,
			Object value) {

		String pin = (String) value;

		for (Employ employ : pm.getAllEmploys()) {
			if (employ.getPin().equalsIgnoreCase(pin)) {
				FacesMessage message = new FacesMessage(
						"Employ with this PIN already exists in database");
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
		}
	}

	// Multi field validation with <f:event>
	// Rule: first two digits of PIN must match last two digits of the year of
	// birth
	public void validatePinDob(ComponentSystemEvent event) {

		UIForm form = (UIForm) event.getComponent();
		UIInput pin = (UIInput) form.findComponent("pin");
		UIInput dob = (UIInput) form.findComponent("dob");

		if (pin.getValue() != null && dob.getValue() != null
				&& pin.getValue().toString().length() >= 2) {
			String twoDigitsOfPin = pin.getValue().toString().substring(0, 2);
			Calendar cal = Calendar.getInstance();
			cal.setTime(((Date) dob.getValue()));

			String lastDigitsOfDob = ((Integer) cal.get(Calendar.YEAR))
					.toString().substring(2);

			if (!twoDigitsOfPin.equals(lastDigitsOfDob)) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(form.getClientId(), new FacesMessage(
						"PIN doesn't match date of birth"));
				context.renderResponse();
			}
		}
	}
}
