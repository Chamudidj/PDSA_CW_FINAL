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
