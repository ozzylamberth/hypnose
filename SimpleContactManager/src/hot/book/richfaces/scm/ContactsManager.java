package book.richfaces.scm;

import java.util.ArrayList;
import java.util.List;

import book.richfaces.scm.model.ContactBean;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("contactsManager")
@Scope(ScopeType.SESSION)
public class ContactsManager {
	
	private List<ContactBean> contactsList;

	private ContactBean newContact;

	public ContactBean getNewContact() {
		if (newContact == null) {
			newContact = new ContactBean();
		}
		return newContact;
	}

	public void setNewContact(ContactBean newContact) {
		this.newContact = newContact;
	}

	public void insertContact() {
		getContactsList().add(0, getNewContact());
		setNewContact(null);
	}

	public List<ContactBean> getContactsList() {
		if (contactsList == null) {
			contactsList = new ArrayList<ContactBean>();
		}
		return contactsList;
	}

	public void setContactsList(List<ContactBean> contactsList) {
		this.contactsList = contactsList;
	}
}
