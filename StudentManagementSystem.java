import java.io.*;
import java.util.*;

class Student {
    private String id;
    private String name;
    private int age;
    private String course;

    public Student(String id, String name, int age, String course) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCourse() {
        return course;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Age: " + age + ", Course: " + course;
    }
}

class StudentDatabase {
    private List<Student> students;

    public StudentDatabase() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(String id) {
        students.removeIf(student -> student.getId().equals(id));
    }

    public Student findStudentById(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    public List<Student> findStudentByName(String name) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(student);
            }
        }
        return result;
    }

    public void updateStudent(String id, String name, int age, String course) {
        Student student = findStudentById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
            student.setCourse(course);
        }
    }

    public void listAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            students = (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}

public class StudentManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDatabase studentDatabase = new StudentDatabase();
    private static final String FILE_NAME = "students.dat";

    public static void main(String[] args) {
        studentDatabase.loadFromFile(FILE_NAME);

        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    listStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    removeStudent();
                    break;
                case 6:
                    saveAndExit();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\nStudent Management System");
        System.out.println("1. Add Student");
        System.out.println("2. List All Students");
        System.out.println("3. Search Student by Name");
        System.out.println("4. Update Student");
        System.out.println("5. Remove Student");
        System.out.println("6. Save and Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addStudent() {
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline character
        System.out.print("Enter student course: ");
        String course = scanner.nextLine();

        Student student = new Student(id, name, age, course);
        studentDatabase.addStudent(student);
        System.out.println("Student added successfully.");
    }

    private static void listStudents() {
        studentDatabase.listAllStudents();
    }

    private static void searchStudent() {
        System.out.print("Enter student name to search: ");
        String name = scanner.nextLine();
        List<Student> foundStudents = studentDatabase.findStudentByName(name);

        if (foundStudents.isEmpty()) {
            System.out.println("No students found with that name.");
        } else {
            for (Student student : foundStudents) {
                System.out.println(student);
            }
        }
    }

    private static void updateStudent() {
        System.out.print("Enter student ID to update: ");
        String id = scanner.nextLine();
        Student student = studentDatabase.findStudentById(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline character
        System.out.print("Enter new course: ");
        String course = scanner.nextLine();

        studentDatabase.updateStudent(id, name, age, course);
        System.out.println("Student updated successfully.");
    }

    private static void removeStudent() {
        System.out.print("Enter student ID to remove: ");
        String id = scanner.nextLine();
        studentDatabase.removeStudent(id);
        System.out.println("Student removed successfully.");
    }

    private static void saveAndExit() {
        studentDatabase.saveToFile(FILE_NAME);
        System.out.println("Data saved. Exiting...");
    }
}
