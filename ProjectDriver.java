package finalProject;

import java.io.*;
import java.util.*;

public class ProjectDriver {
	private static Scanner scanner;
	private static String mainMenu (){
		scanner = new Scanner(System.in);
		String option = "-1";
		System.out.println("\n\nMain Menu\n");
		System.out.println("1 : Student Management");
		System.out.println("2 : Course Management");
		System.out.println("0 : Exit");
		System.out.print("\n\nEnter your selection: ");
		option = scanner.nextLine();
		//returns the user’s choice of the menu (just like we did in class!)
		return option;
	}
	private static String studentMenu() {
		scanner = new Scanner(System.in);
		String option = "0";
		System.out.println("Student Management Menu:\n\n"
				+ "Choose one of:\n\n"
				+ "   A - Search add a student\n"
				+ "   B - Delete a Student\n"
				+ "   C - Print Fee Invoice\n"
				+ "   D - Print List of Students\n"
				+ "   X - Back to main menu\n\n\n");
		System.out.print("Enter your selection: ");
		option = scanner.nextLine();
		return option;
	}
	private static String courseMenu() {
		scanner = new Scanner(System.in);
		String option = "0";
		System.out.println("Course Management Menu:\n\n"
				+ "Choose one of:\n\n"
				+ "   A - Search for a class or lab using the class/lab number\n"
				+ "   B - delete a class\n"
				+ "   C - Add a lab to a class\n"
				+ "   X - Back to main menu\n\n\n");
		System.out.print("Enter your selection: ");
		option = scanner.nextLine();
		return option;
	}
	public static void main(String[] args) {
		scanner = new Scanner(System.in);
		String selection = mainMenu();
		while(selection.compareTo("0") != 0) {
			switch(selection) {
			case "1":
				String stuSelect = studentMenu();
				while((stuSelect.toUpperCase()).compareTo("X") != 0) {
					switch((stuSelect.toUpperCase())) {
					case "A":
						Scanner scan = new Scanner(System.in);
						System.out.println("Enter Student's ID: ");
						String stuID = scan.nextLine();
						System.out.println("Student Type (PhD, MS or Undergrad): ");
						String stuType = scan.nextLine();
						System.out.println("Enter remaining information\n");
						String remInfo = scan.nextLine();
						break;
					case "B":
						break;
					case "C":
						break;
					case "D":
						break;
					case "X":
						break;
					}
					stuSelect = studentMenu();
				}
			case "2":
				String courseSelect = courseMenu();
				while((courseSelect.toUpperCase()).compareTo("X") != 0) {
					switch((courseSelect.toUpperCase())) {
					case "A":
						System.out.print("Enter the Class/Lab Number: ");
						Scanner scan = new Scanner(System.in);
						try {
							findCourse(scan.nextLine());
						} catch (FileNotFoundException e) {
							System.out.println("File error / file not found");
						}
						break;
					case "B":
						System.out.print("Enter the Class/Lab Number: ");
						scan = new Scanner(System.in);
						try {
							deleteCourse(scan.nextLine());
						} catch (FileNotFoundException e) {
							System.out.println("File error / file not found");
						}
						break;
					case "C": // code to add a lab to a class
						System.out.print("Enter the Class Number to add a Lab to: ");
						String classNumber = scanner.nextLine();
						System.out.print("Enter the Lab's Class Number: ");
						String labNumber = scanner.nextLine();
						try {
							addLabToClass(classNumber, labNumber);  
						} catch (FileNotFoundException e) {
						        System.out.println("File error / file not found");
						}
						break;
					case "X":
						break;
					}
					courseSelect = courseMenu();
				}
			}
			selection = mainMenu();
		}
		System.out.println("Take Care!");
	}
	public static void findCourse(String crn) throws FileNotFoundException {
		String line = "";
		String[] lecture = {};
		File file = new File("D:\\Users\\beric\\eclipse-workspace\\Final Project\\src\\finalProject\\lec.txt");
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] arr = line.split(",");
			if(arr.length > 2) {
				lecture = arr;
			}
			if(arr[0].compareTo(crn) == 0) {
				if(arr.length > 2) {
					System.out.println("[ " + arr[0] + "," + arr[1] + "," + arr[2] + " ]");
					return;
				}
				else {
					System.out.println("Lab for [ " + lecture[0] + "," + lecture[1] + "," + lecture[2] + " ]");
					System.out.println("Lab Room " + arr[1]);
					return;
				}
			}
		}
		System.out.println("[ ]");
	}
	public static void deleteCourse(String crn) throws FileNotFoundException{
		String line = "";
		File file = new File("D:\\Users\\beric\\eclipse-workspace\\Final Project\\src\\finalProject\\lec.txt");
		ArrayList <String> strList = new ArrayList<String>();
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] arr = line.split(",");
			if(arr[0].compareTo(crn) == 0) {
				if(arr.length < 3) {
					System.out.println("Can not delete a lab");
					return;
				}
				System.out.println("[ " + arr[0] + "," + arr[1] + "," + arr[2] + " ] deleted!");
				if(scanner.hasNextLine()) {
					line = scanner.nextLine();
					arr = line.split(",");
					while(arr.length < 3 && scanner.hasNextLine()) {
						line = scanner.nextLine();
						arr = line.split(",");
					}
					if(arr.length > 2)
						strList.add(line);
				}
			}
			else {
				strList.add(line);
			}
		}
		PrintWriter pw = new PrintWriter(file);
		while(strList.size() != 0) {
			pw.println(strList.get(0));
			strList.remove(0);
		}
		pw.close();
	}
	public static void addLabToClass(String classNumber, String labNumber) throws FileNotFoundException {
		File file = new File("D:\\Users\\beric\\eclipse-workspace\\Final Project\\src\\finalProject\\lec.txt"); 
		List<String> lines = new ArrayList<>();
		boolean classFound = false;
		int lectureIndex = 0;
		int i = 0;

		// Read all lines from the file and check if the class exists
		try (Scanner fileScanner = new Scanner(file)) {
		    while (fileScanner.hasNextLine()) {
		        String line = fileScanner.nextLine();
		        lines.add(line);
		        String[] arr = line.split(",");
		        i++;
		        if (arr[0].compareTo(classNumber) == 0) { // Check if the line represents the class
		            classFound = true;
		            lectureIndex = i;
		        }
		        if(arr[0].compareTo(labNumber) == 0) {
		        	System.out.println("Class number for lab already in use.");
		        	return;
		        }
		    }
		}

		if (classFound) {
		    // Prompts the user for lab details
		    System.out.println("Enter Lab details (e.g., LabNumber Room): ");
		    String labDetails = scanner.nextLine(); // Use the existing scanner instance

		    // Add the new lab to the list
		    lines.add(lectureIndex, (labNumber + " " + labDetails));

		    // Write the updated list back to the file
		    try (PrintWriter writer = new PrintWriter(file)) {
		        for (String line : lines) {
		            writer.println(line);
		        }
		    }

		    System.out.println("Lab added successfully.");
		} else {
		    System.out.println("Class not found.");
		}
	}
}

class College{
	private ArrayList<Student> stuList;
}

abstract class Student{
	private String name;
	private String id;
	
	public Student ( String name , String id) {
		this.name = name;
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	//abstract public void printInvoice();
}

class UndergraduateStudent extends Student{
	int [] undergradCrnsTaken;
	double gpa;
	boolean resident;
	
	public UndergraduateStudent(String name, String id, int [] undergradCrnsTaken, double gpa, boolean resident) {
		super (name, id);
		this.undergradCrnsTaken = undergradCrnsTaken;
		this.gpa = gpa;
		this.resident = resident;
	}
}

abstract class GraduateStudent extends Student{
	private int crnTA;
	
	public GraduateStudent (String name, String id, int crn) {
		//crn is the crn that the grad student is a teaching assistant for
		super(name, id);
		this.crnTA = crn;
	}
}

class PhdStudent extends GraduateStudent{
	private String advisor;
	private String researchSubject;
	
	public PhdStudent (String name, String id, String advisor, String researchSubject, int crn) {
		//crn is the crn that the grad student is a teaching assistant for
		super(name, id, crn);
		this.advisor = advisor;
		this.researchSubject = researchSubject;
	}
}

class MsStudent extends GraduateStudent{
private int [] gradCrnsTaken;
	
	public MsStudent (String name, String id, int [] gradCrnsTaken, int crn) {
		// gradCoursesTaken is the array of the crns that the Ms student is taking
		//crn is the course number that the Phd student is a teaching assistant for
		super(name, id, crn);
		this.gradCrnsTaken = gradCrnsTaken;
	}
}

class IdException extends Exception{
	
}