package contacts;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, IOException {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        String action = "";
        Contact currectRecord = null;
        while (true) {
            if (action.equals("")) {
                System.out.println("[menu] Enter action (add, list, search, count, exit):");
                action = scanner.nextLine();
            }
            if (action.equals("add")) {
                System.out.println("Enter the type (person, organization):");
                String type = scanner.nextLine();
                if(type.equalsIgnoreCase("person")) {
                    System.out.println("Enter the name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter the surname:");
                    String surname = scanner.nextLine();
                    System.out.println("Enter the birth date:");
                    String date = scanner.nextLine();
                    String regex = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";
                    Pattern compiledPattern = Pattern.compile(regex);
                    Matcher matcher = compiledPattern.matcher(date);
                    if(!matcher.matches()) {
                        System.out.println("Bad birth date!");
                        date = null;
                    }
                    System.out.println("Enter the gender (M, F):");
                    String gender = scanner.nextLine();
                    if(!gender.equalsIgnoreCase("F") && !gender.equalsIgnoreCase("M")) {
                        System.out.println("Bad gender!");
                        gender = null;
                    }
                    System.out.println("Enter the number:");
                    String number = scanner.nextLine();
                    phoneBook.addPerson(name, surname, date, gender, number);
                }
                if(type.equalsIgnoreCase("organization")) {
                    System.out.println("Enter the organization name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter the address:");
                    String address = scanner.nextLine();
                    System.out.println("Enter the number:");
                    String number = scanner.nextLine();
                    phoneBook.addOrganization(name, address, number);
                }
                action = "";
            }
            if (action.equals("search")) {
                List<Contact> contacts = phoneBook.search();
                System.out.println();
                System.out.println("[search] Enter action ([number], back, again): ");
                String input = scanner.nextLine();
                String intRegex = "\\d+";
                if (input.matches(intRegex)) {
                    int index = Integer.parseInt(input);
                    Contact contact = contacts.get(index-1);
                    currectRecord = contact;
                    action = "record";
                } else if (input.equals("back")) {
                    action = "";
                } else if (input.equals("again")) {
                    action = "search";
                    continue;
                }
            }
            if(action.equals("record")) {
                phoneBook.info(currectRecord);
                System.out.println();
                System.out.println("[record] Enter action (edit, delete, menu):");
                String input = scanner.nextLine();
                if(input.equals("edit")) {
                    phoneBook.edit(currectRecord);
                    continue;
                } else if(input.equals("delete")) {
                    phoneBook.remove(currectRecord);
                    action = "";
                    continue;
                } else if (input.equals("menu")) {
                    action = "";
                    continue;
                }
            }
            if (action.equals("count")) {
                System.out.println("The Phone Book has "+ phoneBook.count() +" records.");
                action = "";
            }
            if (action.equals("list")) {
                phoneBook.list();
                System.out.println();
                if (phoneBook.count() == 0) {
                    action = "";
                    continue;
                }
                System.out.println("[list] Enter action ([number], back): ");
                String input = scanner.nextLine();
                String intRegex = "\\d+";
                if(input.matches(intRegex)) {
                    int tmp = Integer.parseInt(input);
                    if (tmp <= phoneBook.count()) {
                        currectRecord = phoneBook.getAt(tmp-1);
                        action = "record";
                    } else {
                        System.out.println("No such record. ");
                    }
                } else if (input.equals("back")) {
                    action = "";
                }
            }
            if (action.equals("exit")) {
                phoneBook.exit();
            }
            System.out.println();
        }
    }
}
