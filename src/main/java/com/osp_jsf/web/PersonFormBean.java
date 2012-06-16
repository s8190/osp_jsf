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


import com.osp_jsf.domain.Person;
import com.osp_jsf.service.PersonManager;

@SessionScoped
@Named("personBean")
public class PersonFormBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Person person = new Person();
	private String text = null;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private ListDataModel<Person> persons = new ListDataModel<Person>();
	
	@Inject
	private PersonManager pm;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public ListDataModel<Person> getAllPersons() {
		persons.setWrappedData(pm.getAllPersons());
		return persons;
	}
	
	public ListDataModel<Person> getPersonSearch() {
		persons.setWrappedData(pm.searchPerson(person));
		return persons;
	}

	// Actions
	public String addPerson() {
		pm.addPerson(person);
		return "showDetalis";
		//return null;
	}
	public String showPerson() {
		return "showPersons";
		//return null;
	}
	public String Back() {
		pm.deletePerson(person);
		return "addSimple";
		//return null;
	}
	public String searchPerson() {
		person.setPin(text);
		person.setFirstName(text);
		return "searchResult";
		 }
	
	public String editPerson() {
		Person personToEdit = persons.getRowData();
		pm.deletePerson(personToEdit);
		return "addSimple";
		 }


	public String deletePerson() {
		Person personToDelete = persons.getRowData();
		pm.deletePerson(personToDelete);
		return null;
	}

	// Validators

	// Business logic validation
	public void uniquePin(FacesContext context, UIComponent component,
			Object value) {

		String pin = (String) value;

		for (Person person : pm.getAllPersons()) {
			if (person.getPin().equalsIgnoreCase(pin)) {
				FacesMessage message = new FacesMessage(
						"Person with this PIN already exists in database");
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
