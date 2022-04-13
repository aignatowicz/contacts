package contacts;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneBook {
    List<Contact> contactList = new ArrayList<>();
    public void addPerson(String name, String surname, String date, String gender, String phone) {
        LocalDate birthdate = null;
        if (date != null) {
            birthdate = LocalDate.parse(date);
        }
        Contact contact = new Person(name, surname, birthdate, gender);
        contact.setPhoneNumber(phone);
        contactList.add(contact);
    }
    public void addOrganization(String name, String address, String phone) {
        Contact contact = new Organization(name, address);
        contact.setPhoneNumber(phone);
        contactList.add(contact);
    }
    public int count() {
        return contactList.size();

    }
    public Contact getAt(int index) {
        return contactList.get(index);
    }
    public void list() throws ClassNotFoundException, IllegalAccessException {
        int counter = 1;
        if (contactList.size() == 0) {
            System.out.println("No records.");
            return;
        }
        for(Contact contact : contactList) {
            Field[] fields = contact.getFields();
            String displayRecord = counter + ". " + contact.getName();
            for (Field f : fields) {
                if(f.getName().equals("surname")) {
                    displayRecord = counter + ". " + contact.getName() + " " + contact.getFieldValue(contact, "surname");
                }
            }
            System.out.println(displayRecord);
            counter++;
        }
    }
    public void info(Contact contact) throws ClassNotFoundException, IllegalAccessException {
        Field[] fields = contact.getFields();
        String displayNameField = "Name: ";
        // name phoneNumber timeCreated timeEdited surname birthday gender address
        for (Field f : fields) {
            if (f.getName().equals("address")) {
                displayNameField = "Organization name: ";
                break;
            }
        }
        System.out.println(displayNameField + contact.getName());
        for (Field f : fields) {
            if (f.getName().equals("address")) {
                System.out.println("Address: " + contact.getFieldValue(contact, "address"));
            } else if (f.getName().equals("surname")) {
                System.out.println("Surname: " + contact.getFieldValue(contact, "surname"));
            } else if (f.getName().equals("birthday")) {
                Object date = contact.getFieldValue(contact, "birthday");
                if (date == null) {
                    System.out.println("Birth date: [no data]");
                } else {
                    System.out.println("Birth date: " + date);
                }
            } else if (f.getName().equals("gender")) {
                Object gen = contact.getFieldValue(contact, "gender");
                if (gen == null) {
                    System.out.println("Gender: [no data]");
                } else {
                    System.out.println("Gender: " + gen);
                }
            }
        }
        System.out.println("Number: " + contact.getPhoneNumber());
        System.out.println("Time created: " + contact.getTimeCreated());
        System.out.println("Time last edit: " + contact.getTimeEdited());
    }
    public void exit() {
        System.exit(0);
    }
    public void remove(Contact contact) {
        contactList.remove(contact);
        System.out.println("The record removed!");
    }

    public void edit(Contact contact) throws ClassNotFoundException, IllegalAccessException {
        Field[] fields = contact.getFields();
        Scanner scanner = new Scanner(System.in);
        String query = "Select a field (name, address, number): ";
        for (Field f : fields) {
            if (f.getName().equals("surname")) {
                query = "Select a field (name, surname, birth, gender, number): ";
            }
        }
        System.out.println(query);
        String action = scanner.nextLine();
        if (action.equals("birth")) {
            System.out.println("Enter birth date: ");
        } else {
            System.out.println("Enter " + action + ": ");
        }
        String newValue = scanner.nextLine();
        if(action.equals("number")) {
            contact.setPhoneNumber(newValue);
            contact.setTimeEdited(LocalDateTime.now());
        } else if(action.equals("gender")) {
            if(newValue.equals("F") || newValue.equals("M")) {
                contact.changeField(contact, action, newValue);
                contact.setTimeEdited(LocalDateTime.now());
            } else {
                System.out.println("Bad gender!");
            }
        } else if(action.equals("birth")) {
            String regex = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";
            Pattern compiledPattern = Pattern.compile(regex);
            Matcher matcher = compiledPattern.matcher(newValue);
            if(!matcher.matches()) {
                System.out.println("Bad birth date!");
            } else {
                LocalDate newDate = LocalDate.parse(newValue);
                contact.changeField(contact, action, newDate);
                contact.setTimeEdited(LocalDateTime.now());
            }
        } else {
            contact.changeField(contact, action, newValue);
            contact.setTimeEdited(LocalDateTime.now());
        }
        System.out.println("Saved");
    }


    public List<Contact> search() throws ClassNotFoundException, IllegalAccessException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter search query: ");
        String input = scanner.nextLine();
        Pattern compiledPattern = Pattern.compile(input, Pattern.CASE_INSENSITIVE);
        StringBuilder sb = new StringBuilder();
        List<Contact> foundContacts = new ArrayList<>();
        for (Contact contact : contactList) {
            Field[] fields = contact.getFields();
            sb.append(contact.getName());
            sb.append(contact.getPhoneNumber());
            for (Field f : fields) {
                Object value = contact.getFieldValue(contact, f.getName());
                sb.append(value);
            }
            Matcher matcher = compiledPattern.matcher(sb);
            if (matcher.find()) {
                foundContacts.add(contact);
            }
            sb = new StringBuilder("");
        }
        System.out.println("Found " + foundContacts.size() + " results: ");
        int counter = 1;
        for (Contact contact : foundContacts) {
            Field[] fields = contact.getFields();
            String result = counter + ". " + contact.getName();
            for (Field f : fields) {
                if (f.getName().equals("surname")) {
                    String value = (String) contact.getFieldValue(contact, "surname");
                    result = counter + ". " + contact.getName() + " " + value;
                }
            }
            System.out.println(result);
            counter++;
        }
        return foundContacts;

    }
}
