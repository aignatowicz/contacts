package contacts;

import java.lang.reflect.Field;

public class Organization extends Contact {
    private String address = "";

    public Organization(String name, String address) {
        super(name);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Field[] getFields() throws ClassNotFoundException {
        Class organization = Class.forName("contacts.Organization");
        Field[] fields = organization.getDeclaredFields();
        return fields;
    }

    @Override
    public void changeField(Contact contact, String field, Object value) throws ClassNotFoundException, IllegalAccessException {
        Organization organization = (Organization) contact;
        Field[] fields = getFields();
        for (Field f : fields) {
            if(f.getName().equals(field)) {
                f.setAccessible(true);
                f.set(organization, value);
            }
        }
    }

    public Object getFieldValue(Contact contact, String field) throws ClassNotFoundException, IllegalAccessException {
        Field[] fields = getFields();
        Object value = null;
        for (Field f : fields) {
            if(f.getName().equals(field)) {
                f.setAccessible(true);
                value = f.get(contact);
            }
        }
        return value;
    }
}
