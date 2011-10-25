package perseus.ie.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import perseus.util.StringUtil;

/**
 * Class representing a person, with fields for various types of names.
 */
public class Person extends AbstractEntity {

    private List<PersonName> names = new ArrayList<PersonName>();

    private Map<String,List<PersonName>> specificNames =
	new HashMap<String,List<PersonName>>();

    public static final String SURNAME = "surname";
    public static final String FORENAME = "forename";
    public static final String ROLE_NAME = "rolename";
    public static final String NAME_LINK = "namelink";
    public static final String ADDITIONAL_NAME = "addname";
    public static final String GENERATIONAL_NAME = "genname";

    public Person() {}

    /**
     * Creates a Person from the tokens contained in `authName`.
     */
    public static Person parseAuthorityName(String authName) {
	Person person = new Person();

	person.authorityName = authName;
	String[] names = authName.split(",");

	person.addName(SURNAME, names[0]);
	for (int i = 1; i < names.length; i++) {
	    person.addName(FORENAME, names[i]);
	}

	return person;
    }

    /**
     * Parses the given display name. Can parse strings like the following:
     *
     * <ul>
     * <li>Fred Smith, Salesman</li>
     * <li>Adrian Packel, Commander, Asiatic Squadron</li>
     * <li>Robert E. Lee</li>
     * <li>Washington</li>
     * </ul>
     */
    public static Person parseDisplayName(String dispName) {
	Person person = new Person();

	person.setDisplayName(dispName);
        person.buildNames();
	return person;
    }

    public void buildNames() {
	// First see if we have a role name, which we assume is demarcated by
	// a comma (and may itself contain commas).
	String actualName = getDisplayName();
	String role = null;
	int roleIndex = actualName.indexOf(",");
	if (roleIndex != -1 && roleIndex+1 < actualName.length()-1) {
	    role = getDisplayName().substring(roleIndex+1).trim();
	    // Don't add the role name yet; save it for last
	    actualName = getDisplayName().substring(0, roleIndex);
	}
	    
	// Assume that the last token of the actual name the person's last
	// name and everything else is a display name.
	String[] names = actualName.split("\\s+");
	for (int i = 0; i < names.length-1; i++) {
	    addName(FORENAME, names[i]);
	}

	if (names.length > 0) addName(SURNAME, names[names.length-1]);
	if (role != null) { addName(ROLE_NAME, role); }

    }

    public List<PersonName> getNames() { return names; }
    public void setNames(List<PersonName> n) { names = n; buildSpecificNames(); }

    protected String toXMLHelper() {
	StringBuffer output = new StringBuffer();

	for (int i = 0, n = names.size(); i < n; i++) {
	    PersonName name = (PersonName) names.get(i);
	    output.append("<name type=\"").append(name.getNameType())
		.append("\">").append(name.getName()).append("</name>\n");
	}

	return output.toString();
    }

    private void buildSpecificNames() {
	specificNames.clear();

	for (PersonName name : names) {
	    String nameType = name.getNameType();

	    List<PersonName> nameList;
	    if (specificNames.containsKey(nameType)) {
		nameList = (List<PersonName>) specificNames.get(nameType);
	    } else {
		nameList = new ArrayList<PersonName>();
		specificNames.put(nameType, nameList);
	    }
	    nameList.add(name);
	}
    }

    /**
     * Returns the first name registered for this person under `nameType`.
     */
    public String getName(String nameType) {
	return getName(nameType, 0);
    }

    /**
     * Returns all this person's names of `nameType`.
     */
    public String[] getNamesOfType(String nameType) {
	if (!specificNames.containsKey(nameType)) {
	    return new String[0];
	}

	List<PersonName> nameList = specificNames.get(nameType);

	String[] output = new String[nameList.size()];
	for (int i = 0, n = nameList.size(); i < n; i++) {
	    output[i] = ((PersonName) nameList.get(i)).getName();
	}

	return output;
    }

    /**
     * Adds a name to this Person.
     *
     * @param nameType the type of name (SURNAME, FORENAME, etc.)
     * @param name the actual name
     */
    public void addName(String nameType, String name) {
	PersonName pn = new PersonName();
	pn.setNameType(nameType);
	pn.setName(StringUtil.capitalizeName(name));
	pn.setPerson(this);

	names.add(pn);

	List<PersonName> nameList;
	if (specificNames.containsKey(nameType)) {
	    nameList = specificNames.get(nameType);
	} else {
	    nameList = new ArrayList<PersonName>();
	    specificNames.put(nameType, nameList);
	}
	nameList.add(pn);

	// Clear these, so that they get rebuilt the next time someone asks
	// for them
	displayName = null;
	authorityName = null;
    }

    /**
     * Returns the name of type `nameType` at position `which`.
     */
    public String getName(String nameType, int which) {
	if (!specificNames.containsKey(nameType)) {
	    throw new IllegalArgumentException("No names found for type "
		    + nameType + "!");
	}

	List<PersonName> names = specificNames.get(nameType);
	if (which >= names.size()) {
	    throw new IllegalArgumentException("No name found at index "
		    + which + ", type " + nameType);
	}

	return names.get(which).getName();
    }

    private String buildDisplayName() {
	List<String> nameList = new ArrayList<String>();

	// Save surnames for last.
	for (PersonName name : names) {
	    if (name.getNameType().equals(SURNAME)) continue;

	    nameList.add(name.getName());
	}
	
	String[] surnames = getNamesOfType(SURNAME);
	for (int i = 0; i < surnames.length; i++) {
	    nameList.add(surnames[i]);
	}
	    
	return StringUtil.join(nameList, " ");
    }

    private String buildAuthorityName() {
	List<String> nameList = new ArrayList<String>();

	// First get surnames...
	String[] surnames = getNamesOfType(SURNAME);
	for (int i = 0; i < surnames.length; i++) {
	    nameList.add(surnames[i].toLowerCase());
	}

	// ...then add forenames.
	for (PersonName name : names) {
	    if (name.getNameType().equals(FORENAME)) {
		nameList.add(name.getName().toLowerCase());
	    }
	}
	    
	return StringUtil.join(nameList, ",");
    }

    public String getDisplayName() {
	if (displayName == null) {
	    displayName = buildDisplayName();
	}

	return displayName;
    }

    public String getAuthorityName() {
	if (authorityName == null) {
	    authorityName = buildAuthorityName();
	}

	return authorityName;
    }

    public String getSurname() {
	return getName(SURNAME);
    }

    public String getRolename() {
	return getName(ROLE_NAME);
    }

    public String getForename() {
	return getName(FORENAME);
    }

    public String getForename(int which) {
	return getName(FORENAME, which);
    }

    public String getSortableString() {
	return getAuthorityName();
    }

    public String toString() {
	return getDisplayName();
    }

    /**
     * Class that represents a specific surname or forename.
     */
    public static class PersonName {

	private String nameType = null;
	private String name = null;
	// This will be used to speed up searching for people by initials;
	// it's a String instead of a char so that Hibernate doesn't complain
	private String initial;
	private Person person;

	public PersonName() {}

	public PersonName(String nameType, String name) {
	    this.nameType = nameType;
	    this.name = name;
	    this.initial = name.substring(0, 1);
	}

	public String getNameType() { return nameType; }
	public String getName() { return name; }
	public String getInitial() { return initial; }
	public Person getPerson() { return person; }

	public void setNameType(String nt) { nameType = nt; }
	public void setName(String n) { name = n; }
	public void setInitial(String i) { initial = i; }
	public void setPerson(Person p) { person = p; }

	public String toString() { return name + " (" + nameType + ")"; }

	public boolean equals(Object o) {
	    if (!(o instanceof PersonName)) { return false ; }

	    PersonName pn = (PersonName) o;
	    return (getName().equalsIgnoreCase(pn.getName()) &&
		    getNameType().equals(pn.getNameType()));
	}
    }
}
