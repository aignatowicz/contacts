package contacts;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class Person extends Contact {
    private String surname;
    private LocalDate birthday;
    private String gender;

    public Person(String name, String surname, LocalDate birthday, String gender) {
        super(name);
        this.surname = surname;
        this.birthday = birthday;
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public Field[] getFields() throws ClassNotFoundException {
        Class person = Class.forName("contacts.Person");
        Field[] fields = person.getDeclaredFields();
        return fields;
    }

    @Override
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


    @Override
    public void changeField(Contact contact, String field, Object value) throws ClassNotFoundException, IllegalAccessException {
        Person person = (Person) contact;
        Field[] fields = getFields();
        for (Field f : fields) {
            if(f.getName().equals(field)) {
                f.setAccessible(true);
                f.set(person, value);
            }
        }
    }


}
