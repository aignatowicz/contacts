package contacts;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Contact {
    private String name;
    private String phoneNumber;
    private LocalDateTime timeCreated;
    private LocalDateTime timeEdited;

    public Contact(String name) {
        this.name = name;
        this.phoneNumber = "";
        timeCreated = LocalDateTime.now();
        timeEdited = timeCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        boolean isCorrect = validateNumber(phoneNumber);
        if(isCorrect) {
            this.phoneNumber = phoneNumber;
        } else {
            this.phoneNumber = "";
        }
    }
    public boolean hasNumber(Contact contact) {
        if(contact.getPhoneNumber().equals("")) {
            return false;
        } else return true;
    }
    private boolean validateNumber(String phoneNumber) {
        String regex = "^\\+?(\\(\\w+\\)|\\w+[ -]\\(\\w{2,}\\)|\\w+)([ -]\\w{2,})*";
        Pattern compiledPattern = Pattern.compile(regex);
        Matcher matcher = compiledPattern.matcher(phoneNumber);
        if(matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public LocalDateTime getTimeEdited() {
        return timeEdited;
    }

    public void setTimeEdited(LocalDateTime timeEdited) {
        this.timeEdited = timeEdited;
    }
    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }
    abstract public Field[] getFields() throws ClassNotFoundException;
    abstract public Object getFieldValue(Contact contact, String field) throws ClassNotFoundException, IllegalAccessException;
    abstract public void changeField(Contact contact, String field, Object value) throws ClassNotFoundException, IllegalAccessException;
}
