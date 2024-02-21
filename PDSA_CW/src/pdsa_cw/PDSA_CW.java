package pdsa_cw;

import java.util.Scanner;
import java.util.regex.*;

class SubjectNode { //Subject Node Class
    String subject;
    int marks;
    SubjectNode prev;
    SubjectNode next;

    SubjectNode(String subject, int marks) {
        this.subject = subject;
        this.marks = marks;
        this.prev = null;
        this.next = null;
    }
}

class Node //Student Node Class
{
    int admission_no;
    String name;
    SubjectNode subjects_head, subjects_tail;

    public Node(int admission_no, String name) {
        this.admission_no = admission_no;
        this.name = name;
        subjects_head = null;
        subjects_tail = null;
    }
}

class AVLNode 
{
    Node student;
    int height;
    AVLNode left, right;

    public AVLNode(Node student) {
        this.student = student;
        this.height = 1;
        this.left = null;
        this.right = null;
    }
}

class AVLTree {
    AVLNode root;

    AVLTree() {
        root = null;
    }

    // Utility functions for AVL tree operations
    public int height(AVLNode node) 
    {
        if (node == null)
            return 0;
        return node.height;
    }

    public int getBalance(AVLNode node) 
    {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    public AVLNode rightRotate(AVLNode y) 
    {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    public AVLNode leftRotate(AVLNode x) 
    {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }
    
    public AVLNode insert(AVLNode node, Node student) {
        if (node == null)
            return new AVLNode(student);

        if (student.admission_no < node.student.admission_no)
            node.left = insert(node.left, student);
        else if (student.admission_no > node.student.admission_no)
            node.right = insert(node.right, student);
        else
        {
            System.err.println("Duplicate Addmission Number Cannot Add");
            return node; // Duplicate admission number not allowed
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && student.admission_no < node.left.student.admission_no)
            return rightRotate(node);

        if (balance < -1 && student.admission_no > node.right.student.admission_no)
            return leftRotate(node);

        if (balance > 1 && student.admission_no > node.left.student.admission_no) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && student.admission_no < node.right.student.admission_no) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    public void insert(int admission_no, String name) 
    {
        Node newNode = new Node(admission_no, name);
        root = insert(root, newNode);
    }
    
    public void display() 
    {
        display(root);
    }

    public void display(AVLNode node) 
    {
        if (node != null) 
        {
            display(node.left);
            System.out.println("Admission Number: " + node.student.admission_no + ", Name: " + node.student.name);
            //displaySubjects(node.student.subjects_head);
            display(node.right);
        }
    }
    
    public void displaySubjects(SubjectNode subjectsHead) {
        SubjectNode current = subjectsHead;
        while (current != null) {
            System.out.println("Subject: " + current.subject + ", Mark: " + current.marks);
            current = current.next;
        }
    }
    
    public Node search(int admission_no) {
        return search(root, admission_no);
    }

    public Node search(AVLNode node, int admission_no) {
        if (node == null || node.student.admission_no == admission_no)
            return node != null ? node.student : null;

        if (admission_no < node.student.admission_no)
            return search(node.left, admission_no);
        else
            return search(node.right, admission_no);
    }
    
    public AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;

        while (current.left != null)
            current = current.left;

        return current;
    }
    
    public AVLNode deleteNode(AVLNode root, int admission_no) {
        if (root == null)
            return root;

        if (admission_no < root.student.admission_no)
            root.left = deleteNode(root.left, admission_no);
        else if (admission_no > root.student.admission_no)
            root.right = deleteNode(root.right, admission_no);
        else {
            if (root.left == null || root.right == null) {
                AVLNode temp = null;
                if (root.left == null)
                    temp = root.right;
                else
                    temp = root.left;

                if (temp == null) {
                    temp = root;
                    root = null;
                } else
                    root = temp;
            } else {
                AVLNode temp = minValueNode(root.right);
                root.student = temp.student;
                root.right = deleteNode(root.right, temp.student.admission_no);
            }
        }

        if (root == null)
            return root;

        root.height = Math.max(height(root.left), height(root.right)) + 1;

        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
}
    public void delete(int admission_no) {
        boolean deleted = findStudent(root, admission_no); // Check if student exists before deletion
        root = deleteNode(root, admission_no);
        boolean foundAfterDelete = findStudent(root, admission_no); // Check if student exists after deletion
        if (deleted && !foundAfterDelete) {
            System.out.println("Student with admission number " + admission_no + " has been successfully deleted.");
        } else {
            System.out.println("No student found with admission number " + admission_no + ". Nothing to delete.");
        }
    }

    private boolean findStudent(AVLNode node, int admission_no) {
        if (node == null) {
            return false;
        }
        if (node.student.admission_no == admission_no) {
            return true;
        }
        return findStudent(node.left, admission_no) || findStudent(node.right, admission_no);
    }
    
    public void update(int admission_no, String new_name) {
        Node student = search(admission_no);
        if (student != null) {
            student.name = new_name;
            System.out.println("Student with admission number " + admission_no + " updated successfully.");
        } else {
            System.out.println("Student with admission number " + admission_no + " not found.");
        }
    }

    public void addMarks(int admission_no, String subject, int marks) {
    Node student_node = search(admission_no);
    if (student_node != null) {
        // Check if the subject already exists for the student
        SubjectNode current = student_node.subjects_head;
        while (current != null) {
            if (current.subject.equals(subject)) {
                System.out.println("Subject '" + subject + "' already exists for student with admission number " + admission_no + ". Marks not added.");
                System.out.println("");
                return;
            }
            current = current.next;
        }
        // If the subject doesn't exist, add marks for it
        SubjectNode new_subject_node = new SubjectNode(subject, marks);
        if (student_node.subjects_head == null) {
            student_node.subjects_head = student_node.subjects_tail = new_subject_node;
        } else {
            student_node.subjects_tail.next = new_subject_node;
            new_subject_node.prev = student_node.subjects_tail;
            student_node.subjects_tail = new_subject_node;
        }
        System.out.println("Marks added for admission number " + admission_no + ", Subject: " + subject + ", Mark: " + marks);
        System.out.println("");
    } else {
        System.out.println("Student with admission number " + admission_no + " not found.");
    }
}
    
    public void updateMarks(int admission_no, String subject, int new_marks) {
        Node student_node = search(admission_no);
        if (student_node != null) {
            SubjectNode current = student_node.subjects_head;
            while (current != null) {
                if (current.subject.equals(subject)) {
                    current.marks = new_marks;
                    System.out.println("Marks updated for admission number " + admission_no + ", Subject: " + subject + ", New Mark: " + new_marks);
                    return;
                }
                current = current.next;
            }
            System.out.println("Subject '" + subject + "' not found for student with admission number " + admission_no + ". Marks not updated.");
        } else {
            System.out.println("Student with admission number " + admission_no + " not found.");
        }
    }
    
    public int sumMarks(int admission_no) {
    Node student_node = search(admission_no);
    if (student_node != null) {
        SubjectNode current = student_node.subjects_head;
        if (current != null) {
            int sum = 0;
            while (current != null) {
                sum += current.marks;
                current = current.next;
            }
            return sum;
        } else {
            System.out.println("No subjects found for the student with admission number: " + admission_no);
        }
    } else {
        System.out.println("Student with admission number " + admission_no + " not found.");
    }
    return -1;
}
    
    public double averageMarks(int admission_no) {
    Node student_node = search(admission_no);
    if (student_node != null) {
        SubjectNode current = student_node.subjects_head;
        if (current != null) {
            int sum = sumMarks(admission_no);
            if (sum != -1) {
                int count = 0;
                while (current != null) {
                    count++;
                    current = current.next;
                }
                return (double) sum / count;
            }
        } else {
            System.out.println("No subjects found for the student with admission number: " + admission_no);
        }
    } else {
        System.out.println("No student found with admission number: " + admission_no);
    }
    return -1.0;
}
    
    public int total_marksForSubject(String subject) {
        return total_marksForSubject(root, subject);
    }

    public int total_marksForSubject(AVLNode node, String subject) {
        if (node == null)
            return 0;

        int total_marks = 0;
        if (node.student.subjects_head != null) {
            SubjectNode current_subject = node.student.subjects_head;
            while (current_subject != null) {
                if (current_subject.subject.equals(subject)) {
                    total_marks += current_subject.marks;
                }
                current_subject = current_subject.next;
            }
        }

        total_marks += total_marksForSubject(node.left, subject);
        total_marks += total_marksForSubject(node.right, subject);

        return total_marks;
    }
    
    public double averageMarksForSubject(String subject) {
    int[] result = new int[2]; // Index 0 for total marks, Index 1 for total students
    averageMarksForSubject(root, subject, result);
    int total_marks = result[0];
    int total_students = result[1];
    System.out.println("total " + total_marks);
    System.out.println("students " + total_students);
    return total_students == 0 ? 0.0 : ((double) total_marks / total_students);
}

public void averageMarksForSubject(AVLNode node, String subject, int[] result) {
    if (node != null) {
        if (node.student.subjects_head != null) {
            SubjectNode current_subject = node.student.subjects_head;
            while (current_subject != null) {
                if (current_subject.subject.equals(subject)) {
                    result[0] += current_subject.marks; // Accumulate total marks
                    result[1]++; // Increment total students
                }
                current_subject = current_subject.next;
            }
        }
        // Recursively traverse left and right subtrees
        averageMarksForSubject(node.left, subject, result);
        averageMarksForSubject(node.right, subject, result);
    }
}

public void displayHighestMarksForStudent(int admission_no) 
    {
    Node student_node = search(admission_no);
    if (student_node != null) {
        SubjectNode current = student_node.subjects_head;
        if (current != null) {
            int highest_marks = Integer.MIN_VALUE;
            String highest_subject = "";
            while (current != null) {
                if (current.marks > highest_marks) {
                    highest_marks = current.marks;
                    highest_subject = current.subject;
                }
                current = current.next;
            }
            System.out.println("Highest Marks for Student " + student_node.name + ": " + highest_marks + " in Subject " + highest_subject);
        } else {
            System.out.println("No subjects found for the student with admission number: " + admission_no);
        }
    } else {
        System.out.println("Student with admission number " + admission_no + " not found.");
    }
}

public void displayLowestMarksForStudent(int admission_no) 
    {
    Node student_node = search(admission_no);
    if (student_node != null) {
        SubjectNode current = student_node.subjects_head;
        if (current != null) {
            int lowest_marks = Integer.MAX_VALUE;
            String lowest_subject = "";
            while (current != null) {
                if (current.marks < lowest_marks) {
                    lowest_marks = current.marks;
                    lowest_subject = current.subject;
                }
                current = current.next;
            }
            System.out.println("Lowest Marks for Student " + student_node.name + ": " + lowest_marks + " in Subject " + lowest_subject);
        } 
        else 
        {
            System.out.println("No subjects found for the student with admission number: " + admission_no);
        }
    } 
    else 
    {
        System.out.println("Student with admission number " + admission_no + " not found.");
    }
}

public void displayHighestMarksForSubject(String subject) {
    String[] result = new String[2]; // Index 0 for student name, Index 1 for highest marks
    result[1] = String.valueOf(Integer.MIN_VALUE); // Initialize to minimum value
    displayHighestMarksForSubject(root, subject, result);
    String student_name = result[0];
    int highest_marks = Integer.parseInt(result[1]);
    if (!student_name.isEmpty()) {
        System.out.println("Highest Marks for Subject " + subject + ": " + highest_marks + " by Student " + student_name);
    } else {
        System.out.println("No student found for subject " + subject);
    }
}

public void displayHighestMarksForSubject(AVLNode node, String subject, String[] result) {
    if (node != null) {
        if (node.student.subjects_head != null) {
            SubjectNode current_subject = node.student.subjects_head;
            while (current_subject != null) {
                if (current_subject.subject.equals(subject) && current_subject.marks > Integer.parseInt(result[1])) {
                    result[1] = String.valueOf(current_subject.marks);
                    result[0] = node.student.name;
                }
                current_subject = current_subject.next;
            }
        }
        displayHighestMarksForSubject(node.left, subject, result);
        displayHighestMarksForSubject(node.right, subject, result);
    }
}

public void displayLowestMarksForSubject(String subject) {
    String[] result = new String[2]; // Index 0 for student name, Index 1 for lowest marks
    result[1] = String.valueOf(Integer.MAX_VALUE); // Initialize to maximum value
    displayLowestMarksForSubject(root, subject, result);
    String student_name = result[0];
    int lowest_marks = Integer.parseInt(result[1]);
    if (!student_name.isEmpty()) {
        System.out.println("Lowest Marks for Subject " + subject + ": " + lowest_marks + " by Student " + student_name);
    } else {
        System.out.println("No student found for subject " + subject);
    }
}

public void displayLowestMarksForSubject(AVLNode node, String subject, String[] result) {
    if (node != null) {
        if (node.student.subjects_head != null) {
            SubjectNode current_subject = node.student.subjects_head;
            while (current_subject != null) {
                if (current_subject.subject.equals(subject) && current_subject.marks < Integer.parseInt(result[1])) {
                    result[1] = String.valueOf(current_subject.marks);
                    result[0] = node.student.name;
                }
                current_subject = current_subject.next;
            }
        }
        displayLowestMarksForSubject(node.left, subject, result);
        displayLowestMarksForSubject(node.right, subject, result);
    }
}

public void deleteSubjectForStudent(int admission_no, String subject) 
{
    Node student_node = search(admission_no);
    if (student_node != null) 
    {
        SubjectNode current = student_node.subjects_head;
        while (current != null) 
        {
            if (current.subject.equals(subject)) 
            {
                if (current.prev != null)
                    current.prev.next = current.next;
                if (current.next != null)
                    current.next.prev = current.prev;
                if (current == student_node.subjects_head)
                    student_node.subjects_head = current.next;
                if (current == student_node.subjects_tail)
                    student_node.subjects_tail = current.prev;
                System.out.println("Subject '" + subject + "' deleted successfully for student with admission number " + admission_no + ".");
                return;
            }
            current = current.next;
        }
        System.out.println("Subject " + subject + " not found for student with admission number " + admission_no + ".");
    } else {
        System.out.println("Student with admission number " + admission_no + " not found.");
    }
}

// Method to display all subjects with the students who study it along with marks
public void displaySubjectsAndMarks() 
{
    boolean students_found = displaySubjectsAndMarks(root);
    if (!students_found) 
    {
        System.out.println("No students found for any subject.");
    }
}

public boolean displaySubjectsAndMarks(AVLNode node) 
{
    boolean students_found = false;
    if (node != null) 
    {
        boolean left_students = displaySubjectsAndMarks(node.left);
        boolean right_students = displaySubjectsAndMarks(node.right);
        students_found = left_students || right_students || (node.student != null && node.student.subjects_head != null);
        if (students_found) 
        {
            System.out.println("Student: " + node.student.name + ", Admission Number: " + node.student.admission_no);
            if (node.student.subjects_head != null) 
            {
                displaySubjects(node.student.subjects_head);
                System.out.println("");
            } 
            else 
            {
                System.out.println("No subjects for this student.");
            }
        }
    }
    return students_found;
}

public void displaySubjectDetails(String subject) 
{
    boolean found = displaySubjectDetails(root, subject);
    if (!found) 
    {
        System.out.println("No students found for subject: " + subject);
    }
}

public boolean displaySubjectDetails(AVLNode node, String subject) {
    boolean found = false;
    if (node != null) {
        // Search in the left subtree
        found |= displaySubjectDetails(node.left, subject);
        
        // Check the current node for the subject
        SubjectNode current = node.student.subjects_head;
        while (current != null) {
            if (current.subject.equals(subject)) {
                System.out.println("Student: " + node.student.name + ", Admission Number: " + node.student.admission_no + ", Marks: " + current.marks);
                found = true;
            }
            current = current.next;
        }
        
        // Search in the right subtree
        found |= displaySubjectDetails(node.right, subject);
    }
    return found;
}
    
    public void displayAVLTree() 
    {
        displayAVLTree(root, 0);
    }

    public void displayAVLTree(AVLNode node, int level) 
    {
        if (node != null) {
            displayAVLTree(node.right, level + 1);
            for (int i = 0; i < level; i++) {
                System.out.print("    ");
            }
            
            System.out.println(node.student.admission_no + "[" + level + "]");
            displayAVLTree(node.left, level + 1);
        }
    }
}
public class PDSA_CW {
   
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        AVLTree studentsTree = new AVLTree();
        /*boolean running = true;
        while (running)
        {
            System.out.print("Enter Admission Number: ");
            int admission_no = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if(admission_no==-1)
            {
                running=false;
            }
            
            else
            {
                System.out.print("Enter Name: ");
                String name = scanner.nextLine();
                studentsTree.insert(admission_no, name);
            }   
        }
        
        studentsTree.displayAVLTree(); 
        studentsTree.display();*/
        
        String[] subs=new String[10];
        int n=subs.length;
        subs[0]="IT";
        subs[1]="Maths";
        subs[2]="Science";
        int c=3;
        
        boolean running = true;
        while (running) {
            System.out.println("");
            System.out.println("~~~~WELCOME TO STUDENT MANAGEMENT SYSTEM MENU~~~~");
            System.out.println("[ 1 ]  Add a New Student");
            System.out.println("[ 2 ]  Display All Students");
            System.out.println("[ 3 ]  Search a Student");
            System.out.println("[ 4 ]  Delete a Student");
            System.out.println("[ 5 ]  Update a Student");
            System.out.println("[ 6 ]  Add a New Subject To The System");
            System.out.println("[ 7 ]  Add Marks For Student To a Subject");
            System.out.println("[ 8 ]  Update Mark Of a Student");
            System.out.println("[ 9 ]  Calculate Total Marks For a Student");
            System.out.println("[ 10 ] Calculate Average Marks For a Student");
            System.out.println("[ 11 ] Calculate Total Marks For a Subject");
            System.out.println("[ 12 ] Calculate Average Marks For a Subject");
            System.out.println("[ 13 ] Display Highest Marks Of a Student");
            System.out.println("[ 14 ] Display Lowest Marks Of a Student");
            System.out.println("[ 15 ] Display Highest Marks For a Subject");
            System.out.println("[ 16 ] Display Lowest Marks For a Subject");
            System.out.println("[ 17 ] Delete Subject Of a Student");
            System.out.println("[ 18 ] Display All Subjects Details");
            System.out.println("[ 19 ] Display a Subject Details");
            System.out.println("[ 20 ] Display AVL Tree Diagram");
            System.out.println("[ 21 ] Exit");
            System.out.println("");
            int choice = -1;
                while (choice <= 0) {
                    System.out.print("Enter your choice: ");
                    if (scanner.hasNextInt()) {
                        choice = scanner.nextInt();
                        if (choice <= 0) {
                            System.out.println("Please enter a positive number for your choice.");
                        }
                    } else {
                        System.out.println("Please enter a valid integer number for your choice.");
                        scanner.next(); // Clear the invalid input
                    }
                }

            scanner.nextLine();
            
            switch (choice) 
            {
                /*case 1:
                    System.out.println("How many new students do you want to add: ");
                    int c1 = -1;
                    while (c1 < 0) {
                        System.out.print("Enter a number(positive): ");
                        if (scanner.hasNextInt()) {
                            c1 = scanner.nextInt();
                            if (c1 < 0) {
                                System.out.println("Please enter a positive number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    for (int i = 0; i < c1; i++) {
                        int admission_number = -1;
                        while (admission_number < 0) {
                            System.out.print("Enter Admission Number: ");
                            if (scanner.hasNextInt()) {
                                admission_number = scanner.nextInt();
                                if (admission_number < 0) {
                                    System.out.println("Please enter a positive number.");
                                }
                            } else {
                                System.out.println("Please enter a valid integer number.");
                                scanner.next(); // Clear the invalid input
                            }
                        }
                        scanner.nextLine(); // Consume newline

                        // Validate name input
                        String name = "";
                        boolean valid_name = false;
                        while (!valid_name) {
                            System.out.print("Enter Name: ");
                            name = scanner.nextLine();
                            // Check if name contains only letters and spaces
                            if (name.matches("[a-zA-Z ]+")) {
                                valid_name = true;
                            } else {
                                System.out.println("Please enter a valid name with no special characters or numbers.");
                            }
                        }

                        studentsTree.insert(admission_number, name);
                    }
                    break;*/
                
                case 1:
                        //System.out.println("How many new students do you want to add: ");
                        int c1 = -1;
                        boolean confirmed = false;
                        while (!confirmed) {
                            while (c1 < 0) {
                                System.out.print("How many new students do you want to add(positive no): ");
                                if (scanner.hasNextInt()) {
                                    c1 = scanner.nextInt();
                                    if (c1 < 0) {
                                        System.out.println("Please enter a positive number.");
                                    }
                                } else {
                                    System.out.println("Please enter a valid integer number.");
                                    scanner.next(); // Clear the invalid input
                                }
                            }
                            System.out.println("You want to add " + c1+" new students.");
                            System.out.println("To confirm, enter '1'. To re-enter the number of students, enter '0'.");
                            int confirmation = scanner.nextInt();
                            if (confirmation == 1) {
                                confirmed = true;
                            } else if (confirmation == 0) {
                                c1 = -1; // Reset c1 to re-enter the number of students
                            } else {
                                System.out.println("Invalid input. Please enter '1' to confirm or '0' to re-enter the number of students.");
                            }
                        }

                        for (int i = 0; i < c1; i++) {
                            int admission_number = -1;
                            while (admission_number < 0) {
                                System.out.print("Enter Admission Number: ");
                                if (scanner.hasNextInt()) {
                                    admission_number = scanner.nextInt();
                                    if (admission_number < 0) {
                                        System.out.println("Please enter a positive number.");
                                    }
                                } else {
                                    System.out.println("Please enter a valid integer number.");
                                    scanner.next(); // Clear the invalid input
                                }
                            }
                            scanner.nextLine(); // Consume newline

                            // Validate name input
                            String name = "";
                            boolean valid_name = false;
                            while (!valid_name) {
                                System.out.print("Enter Name: ");
                                name = scanner.nextLine();
                                // Check if name contains only letters and spaces
                                if (name.matches("[a-zA-Z ]+")) {
                                    valid_name = true;
                                } else {
                                    System.out.println("Please enter a valid name with no special characters or numbers.");
                                }
                            }

                            studentsTree.insert(admission_number, name);
            }
                case 2:
                    studentsTree.display();
                    break;
                    
                case 3:
                    int search_admission_number = -1;
                    while (search_admission_number < 0) {
                        System.out.print("Enter Admission Number to search: ");
                        if (scanner.hasNextInt()) {
                            search_admission_number = scanner.nextInt();
                            if (search_admission_number < 0) {
                                System.out.println("Please enter a positive number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline
                    Node searched_student = studentsTree.search(search_admission_number);
                    if (searched_student != null) {
                        System.out.println("Student Found:");
                        System.out.println("Admission Number: " + searched_student.admission_no + ", Name: " + searched_student.name);
                        studentsTree.displaySubjects(searched_student.subjects_head);
                    } else {
                        System.out.println("Student with admission number " + search_admission_number + " not found.");
                    }
                    break;
                    
                case 4:
                    int delete_admission_no = -1;
                    while (delete_admission_no < 0) {
                        System.out.print("Enter Admission Number to delete: ");
                        if (scanner.hasNextInt()) {
                            delete_admission_no = scanner.nextInt();
                            if (delete_admission_no < 0) {
                                System.out.println("Please enter a positive number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline
                    studentsTree.delete(delete_admission_no);
                    break;
                    
                    case 5:
                        int update_admission_no = -1;
                        while (update_admission_no < 0) {
                            System.out.print("Enter Admission Number to update: ");
                            if (scanner.hasNextInt()) {
                                update_admission_no = scanner.nextInt();
                                if (update_admission_no < 0) {
                                    System.out.println("Please enter a positive number.");
                                }
                            } else {
                                System.out.println("Please enter a valid integer number.");
                                scanner.next(); // Clear the invalid input
                            }
                        }
                        scanner.nextLine(); // Consume newline

                        // Validate new name input
                        String new_name = "";
                        boolean valid_new_name = false;
                        while (!valid_new_name) {
                            System.out.print("Enter New Name: ");
                            new_name = scanner.nextLine();
                            // Check if new name contains only letters and spaces
                            if (new_name.matches("[a-zA-Z ]+")) {
                                valid_new_name = true;
                            } else {
                                System.out.println("Please enter a valid name with no special characters or numbers.");
                            }
                        }

                        studentsTree.update(update_admission_no, new_name);
                        break;
                        
                case 6:
                        System.out.println("Subjects Available Already: ");

                        int count = 0;
                        for (int i = 0; i < n; i++) {
                            if (subs[i] != null) {
                                System.out.println("Subject " + (i + 1) + " -> " + subs[i]);
                                count++;
                            } else {
                                while (true) {
                                    System.out.println();
                                    System.out.print("How many new subjects do you want to add: ");
                                    int c6 = -1;
                                    while (c6 < 0) {
                                        if (scanner.hasNextInt()) {
                                            c6 = scanner.nextInt();
                                            if (c6 < 0) {
                                                System.out.println("Please enter a positive number.");
                                            }
                                        } else {
                                            System.out.println("Please enter a valid integer number.");
                                            scanner.next(); // Clear the invalid input
                                        }
                                    }

                                    for (int k = 0; k < c6; k++) {
                                        System.out.print("New Subject Name: ");
                                        subs[count] = scanner.next();
                                        System.out.println(subs[count] + " Added Successfully to the System.\n");
                                        count++;
                                    }

                                    System.out.println();
                                    for (int j = 0; j < count; j++) {
                                        System.out.println("Subject " + (j + 1) + " -> " + subs[j]);
                                    }
                                    c = c - c + (count);
                                    break;
                                }
                                break;
                            }
                        }
                        break;        
                        
                    case 7:
                    int student_admission_no = -1;
                    while (student_admission_no < 0) {
                        System.out.println("Enter Admission Number of the Student: ");
                        if (scanner.hasNextInt()) {
                            student_admission_no = scanner.nextInt();
                            if (student_admission_no < 0) {
                                System.out.println("Please enter a positive number for Admission Number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Admission Number.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    Node student = studentsTree.search(student_admission_no);
                    if (student != null) {
                        System.out.print("How many subjects do you want to add marks: ");
                        int c7 = -1;
                        while (c7 < 0 || c7 > c) {
                            if (scanner.hasNextInt()) {
                                c7 = scanner.nextInt();
                                if (c7 < 0) {
                                    System.out.println("Please enter a positive number for the number of subjects.");
                                } else if (c7 > c) {
                                    System.out.println("There are only " + c + " subjects available.");
                                }
                            } else {
                                System.out.println("Please enter a valid integer number for the number of subjects.");
                                scanner.next(); // Clear the invalid input
                            }
                        }

                        for (int k = 0; k < c7; k++) {
                            System.out.println("Please Select relative no of the subject");
                            System.out.println("No of subjects: " + c);
                            for (int i = 0; i < c; i++) {
                                System.out.println("Subject " + (i + 1) + " -> " + subs[i]);
                            }

                            int subject_no = -1;
                            while (subject_no < 0 || subject_no > c) {
                                System.out.print("Enter Subject No: ");
                                if (scanner.hasNextInt()) {
                                    subject_no = scanner.nextInt();
                                    if (subject_no < 0 || subject_no > c) {
                                        System.out.println("Please enter a valid subject number.");
                                    }
                                } else {
                                    System.out.println("Please enter a valid integer number for Subject No.");
                                    scanner.next(); // Clear the invalid input
                                }
                            }
                            String subject = subs[subject_no - 1];
                            System.out.println("Selected Subject: " + subject);

                            int marks = -1;
                            while (marks < 0 || marks > 100) {
                                System.out.print("Enter Marks (0-100): ");
                                if (scanner.hasNextInt()) {
                                    marks = scanner.nextInt();
                                    if (marks < 0 || marks > 100) {
                                        System.out.println("Please enter a number between 0 and 100 for Marks.");
                                    }
                                } else {
                                    System.out.println("Please enter a valid integer number for Marks.");
                                    scanner.next(); // Clear the invalid input
                                }
                            }
                            studentsTree.addMarks(student_admission_no, subject, marks);
                        }
                    } else {
                        System.out.println("Student with admission number " + student_admission_no + " not found.");
                    }
                    break;
                    
                    case 8:
                    int stu_ad_no = -1;
                    while (stu_ad_no < 0) {
                        System.out.print("Enter Admission Number of the Student: ");
                        if (scanner.hasNextInt()) {
                            stu_ad_no = scanner.nextInt();
                            if (stu_ad_no < 0) {
                                System.out.println("Please enter a positive number for Admission Number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Admission Number.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    System.out.println("Enter Subject no: ");
                    for (int i = 0; i < c; i++) {
                        System.out.println("Subject " + (i + 1) + " -> " + subs[i]);
                    }
                    int subject_num = -1;
                    while (subject_num < 0 || subject_num > c) {
                        System.out.print("Enter Subject No: ");
                        if (scanner.hasNextInt()) {
                            subject_num = scanner.nextInt();
                            if (subject_num < 0 || subject_num > c) {
                                System.out.println("Please enter a valid subject number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Subject No.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    String subject = subs[subject_num - 1];

                    int new_marks = -1;
                    while (new_marks < 0 || new_marks > 100) {
                        System.out.print("Enter New Marks (0-100): ");
                        if (scanner.hasNextInt()) {
                            new_marks = scanner.nextInt();
                            if (new_marks < 0 || new_marks > 100) {
                                System.out.println("Please enter a number between 0 and 100 for New Marks.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for New Marks.");
                            scanner.next(); // Clear the invalid input
                        }
                    }

                    studentsTree.updateMarks(stu_ad_no, subject, new_marks);
                    break;
                    
                    case 9:
                    int sum_admission_number = -1;
                    while (sum_admission_number < 0) {
                        System.out.print("Enter Admission Number to calculate sum marks: ");
                        if (scanner.hasNextInt()) {
                            sum_admission_number = scanner.nextInt();
                            if (sum_admission_number < 0) {
                                System.out.println("Please enter a positive number for Admission Number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Admission Number.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    int sum = studentsTree.sumMarks(sum_admission_number);
                    if (sum != -1) {
                        System.out.println("Sum of Marks: " + sum);
                    }
                    break;
                    
                    case 10:
                    int avg_admission_number = -1;
                    while (avg_admission_number < 0) {
                        System.out.print("Enter Admission Number to calculate average marks: ");
                        if (scanner.hasNextInt()) {
                            avg_admission_number = scanner.nextInt();
                            if (avg_admission_number < 0) {
                                System.out.println("Please enter a positive number for Admission Number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Admission Number.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    double average = studentsTree.averageMarks(avg_admission_number);
                    if (average != -1.0) {
                        System.out.println("Average Marks: " + average);
                    }
                    break;
                    
                case 11:
                    int subject_total_no = -1;
                    while (subject_total_no < 0 || subject_total_no > c) {
                        System.out.println("Enter Subject no to calculate total marks: ");
                        for (int i = 0; i < c; i++) {
                            System.out.println("Subject " + (i + 1) + " -> " + subs[i]);
                        }
                        System.out.print("Enter Subject No: ");
                        if (scanner.hasNextInt()) {
                            subject_total_no = scanner.nextInt();
                            if (subject_total_no < 0 || subject_total_no > c) {
                                System.out.println("Please enter a valid subject number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Subject No.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    String total_marks_subject = subs[subject_total_no - 1];
                    int total_marks = studentsTree.total_marksForSubject(total_marks_subject);
                    System.out.println("Total Marks for Subject '" + total_marks_subject + "': " + total_marks);
                    break;
                    
                case 12:
                    int subject_avg_no = -1;
                    while (subject_avg_no < 0 || subject_avg_no > c) {
                        System.out.println("Enter Subject no to calculate average marks: ");
                        for (int i = 0; i < c; i++) {
                            System.out.println("Subject " + (i + 1) + " -> " + subs[i]);
                        }
                        System.out.print("Enter Subject No: ");
                        if (scanner.hasNextInt()) {
                            subject_avg_no = scanner.nextInt();
                            if (subject_avg_no < 0 || subject_avg_no > c) {
                                System.out.println("Please enter a valid subject number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Subject No.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    String average_marks_subject = subs[subject_avg_no - 1];
                    double avg_marks = studentsTree.averageMarksForSubject(average_marks_subject);
                    System.out.println("Average Marks for Subject '" + average_marks_subject + "': " + avg_marks);
                    break;
                    
                    case 13:
                    int highest_marks_admission_number = -1;
                    while (highest_marks_admission_number < 0) {
                        System.out.print("Enter Admission Number to display highest marks for student: ");
                        if (scanner.hasNextInt()) {
                            highest_marks_admission_number = scanner.nextInt();
                            if (highest_marks_admission_number < 0) {
                                System.out.println("Please enter a positive number for Admission Number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Admission Number.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    studentsTree.displayHighestMarksForStudent(highest_marks_admission_number);
                    break;
                    
                    case 14:
                    int lowest_marks_admission_number = -1;
                    while (lowest_marks_admission_number < 0) {
                        System.out.print("Enter Admission Number to display lowest marks for student: ");
                        if (scanner.hasNextInt()) {
                            lowest_marks_admission_number = scanner.nextInt();
                            if (lowest_marks_admission_number < 0) {
                                System.out.println("Please enter a positive number for Admission Number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Admission Number.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    studentsTree.displayLowestMarksForStudent(lowest_marks_admission_number);
                    break;
                    
                case 15:
                    int highest_marks_subject_no = -1;
                    while (highest_marks_subject_no < 0 || highest_marks_subject_no > c) {
                        System.out.println("Enter Subject no to display highest marks: ");
                        for (int i = 0; i < c; i++) {
                            System.out.println("Subject " + (i + 1) + " -> " + subs[i]);
                        }
                        System.out.print("Enter Subject No: ");
                        if (scanner.hasNextInt()) {
                            highest_marks_subject_no = scanner.nextInt();
                            if (highest_marks_subject_no < 0 || highest_marks_subject_no > c) {
                                System.out.println("Please enter a valid subject number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Subject No.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    String highest_marks_subject = subs[highest_marks_subject_no - 1];
                    studentsTree.displayHighestMarksForSubject(highest_marks_subject);
                    break;
                    
                case 16:
                    int lowest_marks_subject_no = -1;
                    while (lowest_marks_subject_no < 0 || lowest_marks_subject_no > c) {
                        System.out.println("Enter Subject no to display lowest marks: ");
                        for (int i = 0; i < c; i++) {
                            System.out.println("Subject " + (i + 1) + " -> " + subs[i]);
                        }
                        System.out.print("Enter Subject No: ");
                        if (scanner.hasNextInt()) {
                            lowest_marks_subject_no = scanner.nextInt();
                            if (lowest_marks_subject_no < 0 || lowest_marks_subject_no > c) {
                                System.out.println("Please enter a valid subject number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Subject No.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    String lowest_marks_subject = subs[lowest_marks_subject_no - 1];
                    studentsTree.displayLowestMarksForSubject(lowest_marks_subject);
                    break;
                    
                    case 17:
                    int stu_admission_no = -1;
                    while (stu_admission_no < 0) {
                        System.out.print("Enter Admission Number of the Student: ");
                        if (scanner.hasNextInt()) {
                            stu_admission_no = scanner.nextInt();
                            if (stu_admission_no < 0) {
                                System.out.println("Please enter a positive number for Admission Number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Admission Number.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    int subject_to_delete_no = -1;
                    while (subject_to_delete_no < 0 || subject_to_delete_no > c) {
                        System.out.println("Enter the subject to delete along with its marks: ");
                        for (int i = 0; i < c; i++) {
                            System.out.println("Subject " + (i + 1) + " -> " + subs[i]);
                        }
                        System.out.print("Enter Subject No: ");
                        if (scanner.hasNextInt()) {
                            subject_to_delete_no = scanner.nextInt();
                            if (subject_to_delete_no < 0 || subject_to_delete_no > c) {
                                System.out.println("Please enter a valid subject number.");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Subject No.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    String subject_to_delete = subs[subject_to_delete_no - 1];
                    studentsTree.deleteSubjectForStudent(stu_admission_no, subject_to_delete);
                    break;
                    
                    case 18:
                    System.out.println("All Subjects with Students and Marks:");
                    studentsTree.displaySubjectsAndMarks();
                    break;
                    
                    case 19:
                    int subject_no = -1;
                    while (subject_no < 0 || subject_no > c) {
                        System.out.println("Enter the subject number to display details:");
                        for (int i = 0; i < c; i++) {
                            System.out.println("Subject " + (i + 1) + " -> " + subs[i]);
                        }
                        System.out.print("Enter Subject No: ");
                        if (scanner.hasNextInt()) {
                            subject_no = scanner.nextInt();
                            if (subject_no < 0 || subject_no > c) {
                                System.out.println("Please enter a valid subject number between 1 and " + c + ".");
                            }
                        } else {
                            System.out.println("Please enter a valid integer number for Subject No.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    scanner.nextLine(); // Consume newline

                    String subject_to_display = subs[subject_no - 1];
                    System.out.println("Details of Subject '" + subject_to_display + "':");
                    studentsTree.displaySubjectDetails(subject_to_display);
                    break;
                    
                    case 20:
                    System.out.println("AVL Tree Diagram:");
                    studentsTree.displayAVLTree();
                    break;
                    
                case 21:
                    running = false;
                    System.out.println("....EXITING STUDENT MANAGEMENT SYSTEM. GOODBYE!....");
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 21.");    
            }
        }
        
    }
    
}
