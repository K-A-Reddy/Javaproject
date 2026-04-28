import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class MessManagementSystem {

    static Scanner sc = new Scanner(System.in);

    // ---------------- USER CLASS ----------------
    static class Student {
        String name;
        String password;
        String roomNo;
        String rollNo;

        Student(String name, String password, String roomNo, String rollNo) {
            this.name = name;
            this.password = password;
            this.roomNo = roomNo;
            this.rollNo = rollNo;
        }
    }

    // ---------------- DATA STORAGE ----------------
    static ArrayList<Student> students = new ArrayList<>();
    static ArrayList<Review> reviews = new ArrayList<>();
    static ArrayList<Suggestion> suggestions = new ArrayList<>();
    static ArrayList<Complaint> complaints = new ArrayList<>();
    static ArrayList<Rebate> rebates = new ArrayList<>();
    static ArrayList<String> notifications = new ArrayList<>();

    // Committee & Manager Login
    static String committeeUser = "committee";
    static String committeePass = "1234";

    static String managerUser = "manager";
    static String managerPass = "1234";

    // ---------------- LOAD STUDENTS FROM CSV ----------------
    static void loadStudents() {
        try (BufferedReader br = new BufferedReader(new FileReader("Student.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    students.add(new Student(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            // File might not exist or empty, ignore
        }
    }

    // ---------------- SAVE STUDENT TO CSV ----------------
    static void saveStudentToCSV(Student s) {
        try (FileWriter fw = new FileWriter("Student.csv", true)) {
            fw.write(s.name + "," + s.password + "," + s.roomNo + "," + s.rollNo + "\n");
        } catch (IOException e) {
            System.out.println("Error saving student to CSV: " + e.getMessage());
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        notifications.add("Welcome to Mess Management System");
        notifications.add("Breakfast Timing: 8 AM");
        notifications.add("Dinner Timing: 7 PM");

        loadStudents();
        loadReviews();
        loadSuggestions();
        loadComplaints();
        loadRebates();

        while (true) {
            System.out.println("\n========== MESS MANAGEMENT SYSTEM ==========");
            System.out.println("1. Student Register");
            System.out.println("2. Student Login");
            System.out.println("3. Mess Committee Login");
            System.out.println("4. Mess Manager Login");
            System.out.println("5. Exit");
            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    registerStudent();
                    break;
                case 2:
                    studentLogin();
                    break;
                case 3:
                    committeeLogin();
                    break;
                case 4:
                    managerLogin();
                    break;
                case 5:
                    System.out.println("Thank You!");
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // ---------------- STUDENT REGISTER ----------------
    static void registerStudent() {
        System.out.println("\n--- Student Registration ---");

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        System.out.print("Enter Room Number: ");
        String room = sc.nextLine();

        System.out.print("Enter Roll Number: ");
        String roll = sc.nextLine();

        students.add(new Student(name, password, room, roll));

        saveStudentToCSV(new Student(name, password, room, roll));

        System.out.println("Registration Successful!");
    }

    // ---------------- STUDENT LOGIN ----------------
    static void studentLogin() {
        System.out.println("\n--- Student Login ---");

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        for (Student s : students) {
            if (s.name.equals(name) && s.password.equals(password)) {
                System.out.println("Login Successful!");
                studentMenu(s);
                return;
            }
        }

        System.out.println("Invalid Username or Password!");
    }

    // ---------------- STUDENT MENU ----------------
    static void studentMenu(Student s) {
        while (true) {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("Welcome " + s.name);
            System.out.println("1. View My Profile");
            System.out.println("2. Submit Review");
            System.out.println("3. Give Suggestion");
            System.out.println("4. File Complaint");
            System.out.println("5. Fill Rebate Form");
            System.out.println("6. View Notifications");
            System.out.println("7. Logout");
            System.out.print("Enter Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    System.out.println("Name: " + s.name);
                    System.out.println("Room No: " + s.roomNo);
                    System.out.println("Roll No: " + s.rollNo);
                    break;

                case 2: {
                    System.out.print("Enter Review: ");
                    Review r = new Review(s.name, sc.nextLine());
                    reviews.add(r);
                    saveReview(r);
                    System.out.println("Review Submitted!");
                    break;
                }

                case 3: {
                    System.out.print("Enter Suggestion: ");
                    Suggestion sug = new Suggestion(s.name, sc.nextLine());
                    suggestions.add(sug);
                    saveSuggestion(sug);
                    System.out.println("Suggestion Submitted!");
                    break;
                }

                case 4: {
                    System.out.print("Enter Complaint: ");
                    Complaint c = new Complaint(s.name, sc.nextLine());
                    complaints.add(c);
                    saveComplaint(c);
                    System.out.println("Complaint Submitted!");
                    break;
                }

                case 5: {
                    System.out.print("Enter Rebate Reason: ");
                    Rebate r = new Rebate(s.name, s.rollNo, sc.nextLine(), "Pending");
                    rebates.add(r);
                    saveRebate(r);
                    System.out.println("Rebate Form Submitted!");
                    break;
                }

                case 6: {
                    System.out.println("\n--- Notifications ---");
                    for (String n : notifications)
                        System.out.println("- " + n);
                    break;
                }

                case 7:
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // ---------------- COMMITTEE LOGIN ----------------
    static void committeeLogin() {
        System.out.println("\n--- Committee Login ---");

        System.out.print("Username: ");
        String u = sc.nextLine();

        System.out.print("Password: ");
        String p = sc.nextLine();

        if (u.equals(committeeUser) && p.equals(committeePass)) {
            committeeMenu();
        } else {
            System.out.println("Invalid Login!");
        }
    }

    // ---------------- COMMITTEE MENU ----------------
    static void committeeMenu() {
        while (true) {
            System.out.println("\n--- COMMITTEE MENU ---");
            System.out.println("1. View Reviews");
            System.out.println("2. View Suggestions");
            System.out.println("3. View Complaints");
            System.out.println("4. Add Notification");
            System.out.println("5. Logout");
            System.out.print("Enter Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    showList(reviews);
                    break;
                case 2:
                    showList(suggestions);
                    break;
                case 3:
                    showList(complaints);
                    break;
                case 4:
                    System.out.print("Enter Notification: ");
                    notifications.add(sc.nextLine());
                    System.out.println("Added!");
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // ---------------- MANAGER LOGIN ----------------
    static void managerLogin() {
        System.out.println("\n--- Manager Login ---");

        System.out.print("Username: ");
        String u = sc.nextLine();

        System.out.print("Password: ");
        String p = sc.nextLine();

        if (u.equals(managerUser) && p.equals(managerPass)) {
            managerMenu();
        } else {
            System.out.println("Invalid Login!");
        }
    }

    // ---------------- MANAGER MENU ----------------
    static void managerMenu() {
        while (true) {
            System.out.println("\n--- MANAGER MENU ---");
            System.out.println("1. View Reviews");
            System.out.println("2. View Suggestions");
            System.out.println("3. View Complaints");
            System.out.println("4. Manage Rebates");
            System.out.println("5. Add Announcement");
            System.out.println("6. Logout");
            System.out.print("Enter Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    showList(reviews);
                    break;
                case 2:
                    showList(suggestions);
                    break;
                case 3:
                    showList(complaints);
                    break;
                case 4:
                    showRebates();

                    System.out.print("Enter index to update (-1 to skip): ");
                    int i = sc.nextInt();
                    sc.nextLine();

                    if (i >= 0 && i < rebates.size()) {
                        System.out.print("Enter status (Approved/Rejected): ");
                        rebates.get(i).status = sc.nextLine();

                        rewriteRebates();  // THIS is the important part

                        System.out.println("Rebate status updated!");
                    }
                    break;
                case 5:
                    System.out.print("Enter Announcement: ");
                    notifications.add(sc.nextLine());
                    System.out.println("Added!");
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    // ---------------- DISPLAY LIST ----------------
    static <T> void showList(ArrayList<T> list) {
        if (list.size() == 0) {
            System.out.println("No Data Available");
        } else {
            for (T item : list)
                System.out.println("- " + item);
        }
    }
    static void loadReviews() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("reviews.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", 2);
                if (d.length == 2) {
                    reviews.add(new Review(d[0], d[1]));
                }
            }
            br.close();
        } catch (Exception e) {}
    }
    static void loadSuggestions() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("suggestions.csv"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", 2);
                if (d.length == 2) {
                    suggestions.add(new Suggestion(d[0], d[1]));
                }
            }
            br.close();
        } catch (Exception e) {}
    }
    static void loadComplaints() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("complaints.csv"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", 2);
                if (d.length == 2) {
                    complaints.add(new Complaint(d[0], d[1]));
                }
            }
            br.close();
        } catch (Exception e) {}
    }
    static void loadRebates() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("rebates.csv"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split(",", 4);
                if (d.length == 4) {
                    rebates.add(new Rebate(d[0], d[1], d[2], d[3]));
                }
            }
            br.close();
        } catch (Exception e) {}
    }
    static void saveReview(Review r) {
        try {
            FileWriter fw = new FileWriter("reviews.csv", true);
            fw.write(r.studentName + "," + r.message + "\n");
            fw.close();
        } catch (Exception e) {}
    }
    static void saveSuggestion(Suggestion s) {
        try {
            FileWriter fw = new FileWriter("suggestions.csv", true);
            fw.write(s.studentName + "," + s.message + "\n");
            fw.close();
        } catch (Exception e) {}
    }
    static void saveComplaint(Complaint c) {
        try {
            FileWriter fw = new FileWriter("complaints.csv", true);
            fw.write(c.studentName + "," + c.message + "\n");
            fw.close();
        } catch (Exception e) {}   
    }
    static void saveRebate(Rebate r) {
        try {
            FileWriter fw = new FileWriter("rebates.csv", true);
            fw.write(r.studentName + "," + r.rollNo + "," + r.reason + "," + r.status + "\n");
            fw.close();
        } catch (Exception e) {}   
    }
    static void rewriteRebates() {
        try {
            FileWriter fw = new FileWriter("rebates.csv");
            for (Rebate r : rebates) {
                fw.write(r.studentName + "," + r.rollNo + "," + r.reason + "," + r.status + "\n");
            }
            fw.close();
        } catch (Exception e) {}
    }
    static void showRebates() {
    if (rebates.isEmpty()) {
        System.out.println("No Rebates");
        return;
    }

    for (int i = 0; i < rebates.size(); i++) {
        Rebate r = rebates.get(i);
        System.out.println(i + ". " + r.studentName +
                " | " + r.rollNo +
                " | " + r.reason +
                " | " + r.status);
    }
}
}