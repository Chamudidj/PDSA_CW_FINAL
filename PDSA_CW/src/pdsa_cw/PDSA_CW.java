package pdsa_cw;

import java.util.Scanner;

class Node 
{
    int admission_no;
    String name;

    public Node(int admission_no, String name) {
        this.admission_no = admission_no;
        this.name = name;
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
        if (node != null) {
            display(node.left);
            System.out.println("Admission Number: " + node.student.admission_no + ", Name: " + node.student.name);
            display(node.right);
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
        
        boolean running = true;
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
        studentsTree.display();
        
    }
    
}
