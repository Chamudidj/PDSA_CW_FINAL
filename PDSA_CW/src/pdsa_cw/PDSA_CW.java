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
            System.out.println("[ 1 ]  Add A New Student");
            System.out.println("[ 2 ]  Display All Students");
            System.out.println("[ 3 ]  Search A Student");
            System.out.println("[ 4 ]  Delete A Student");
            System.out.println("[ 5 ]  Update A Student");
            System.out.println("[ 6 ]  Add A New Subject To The System");
            System.out.println("[ 7 ]  Add Marks For Student To A Subject");
            System.out.println("[ 8 ]  Update Mark Of A Student");
            System.out.println("[ 9 ]  Calculate Total Marks For A Student");
            System.out.println("[ 10 ]  Calculate Average Marks For A Student");
            System.out.println("[ 11 ] Calculate Total Marks For A Subject");
            System.out.println("[ 12 ] Calculate Average Marks For A Subject");
            System.out.println("[ 13 ] Display Highest Marks Of A Student");
            System.out.println("[ 14 ] Display Lowest Marks Of A Student");
            System.out.println("[ 15 ] Display Highest Marks For A Subject");
            System.out.println("[ 16 ] Display Lowest Marks For A Subject");
            System.out.println("[ 17 ] Delete Subject Of A Student");
            System.out.println("[ 18 ] Display All Subjects Details");
            System.out.println("[ 19 ] Display A Subject Details");
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
                case 1:
                    System.out.println("How many new students do you want to add: ");
                    int c1 = -1;
                    while (c1 < 0) {
                        System.out.print("Enter a positive number: ");
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
                        int admissionNumber = -1;
                        while (admissionNumber < 0) {
                            System.out.print("Enter Admission Number: ");
                            if (scanner.hasNextInt()) {
                                admissionNumber = scanner.nextInt();
                                if (admissionNumber < 0) {
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
                        boolean validName = false;
                        while (!validName) {
                            System.out.print("Enter Name: ");
                            name = scanner.nextLine();
                            // Check if name contains only letters and spaces
                            if (name.matches("[a-zA-Z ]+")) {
                                validName = true;
                            } else {
                                System.out.println("Please enter a valid name with no special characters or numbers.");
                            }
                        }

                        studentsTree.insert(admissionNumber, name);
                    }
                    break;
                    
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
                        
                    case 7:
                    int student_admission_no = -1;
                    while (student_admission_no < 0) {
                        System.out.print("Enter Admission Number of the Student: ");
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
                    
                case 21:
                    running = false;
                    System.out.println("....EXITING SCHOOL MANAGEMENT SYSTEM. GOODBYE!....");
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 21.");    
            }
        }
        
    }
    
}
