package finalProject;

import java.io.*;
import java.util.*;

public class ProjectDriver {
	//array of students
	private static ArrayList<Student> stuList = new ArrayList<Student>();
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
	
	//student menu used for student management (1) selection
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
	
	//course menu used for course management (2) selection
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
		// hardcoded cases for testing student menu; need to remove before submitting
			int[] crnStuArr = {66636,56980,98123,36637};
			stuList.add(new UndergraduateStudent("Ed Johnson", "EJ1111",crnStuArr, 3.51, true));
			stuList.add(new UndergraduateStudent("William Jones","WJ1111",crnStuArr,2.9,true));
			stuList.add(new UndergraduateStudent("Kirby Sons","KS1111",crnStuArr,3.25,false));
			int[] crnStuArrGrad = {32658,69745,20315};
			int[] crnTaArr = {91862,95310};
			stuList.add(new MsStudent("Erika Jones","EJ1112",crnStuArrGrad,crnTaArr));
			stuList.add(new PhdStudent("John Junaid","jo2978","Arup Guha","Climate Change",crnTaArr));
		
		scanner = new Scanner(System.in);
		String selection = mainMenu();
		while(selection.compareTo("0") != 0) {
			switch(selection) {
			case "1": //student management
				String stuSelect = studentMenu();
				while((stuSelect.toUpperCase()).compareTo("X") != 0) {
					switch((stuSelect.toUpperCase())) {
					case "A": //add student
						addStudent();
						break;
					case "B": //remove student
					    System.out.print("Enter the Student's ID to delete: ");
					    String stuIdToDelete = scanner.nextLine();
					    deleteStudentById(stuIdToDelete);
						break;
					case "C": //print fee invoice
						System.out.print("Input ID of student to print fee invoice: ");
						String stuToPrintInvoice = scanner.nextLine();
						printFeeInvoice((stuToPrintInvoice).toUpperCase());
						break;
					case "D": //print all students
						break;
					case "X":
						break;
					}
					stuSelect = studentMenu(); //user selects option from student menu
				}
				
			case "2": //course management
				String courseSelect = courseMenu();
				while((courseSelect.toUpperCase()).compareTo("X") != 0) {
					switch((courseSelect.toUpperCase())) {
					case "A": //find/print course
						System.out.print("Enter the Class/Lab Number: ");
						Scanner scan = new Scanner(System.in);
						try {
							findCourse(scan.nextLine());
						} catch (FileNotFoundException e) {
							System.out.println("File error / file not found");
						}
						break;
					case "B": //delete lecture (I'm assuming we don't delete labs)
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
					courseSelect = courseMenu(); //user selects option from course menu
				}
			}
			selection = mainMenu(); //user selects option from main menu
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
			if(arr.length > 2) { //if a line has more than two components, it is a lecture
				lecture = arr; // saves most recently scanned/read lecture because crn might be a lab
			}
			if(arr[0].compareTo(crn) == 0) {
				if(arr.length > 2) { //crn is a lecture
					System.out.println("[ " + arr[0] + "," + arr[1] + "," + arr[2] + " ]");
					                         // crn          prefix          title
					return;
				}
				else { //crn is a lab
					System.out.println("Lab for [ " + lecture[0] + "," + lecture[1] + "," + lecture[2] + " ]");
					                          //lecture   crn              prefix              title
					System.out.println("Lab Room " + arr[1]);
					return;
				}
			}
		}
		System.out.println("[ ]"); //if crn is not found in text file
	}
	
	public static void deleteCourse(String crn) throws FileNotFoundException{
		// this method reads all of the lectures and labs except for the deleted lecture and its associated labs
		// then the list containing all of the nondeleted lectures and labs is printed back to text file
		String line = "";
		File file = new File("D:\\Users\\beric\\eclipse-workspace\\Final Project\\src\\finalProject\\lec.txt");
			//I can't get the relative file path to work on my eclipse, so we'll just have to change any file paths right before we submit
		
		ArrayList <String> strList = new ArrayList<String>(); //contains non-deleted lectures & labs
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] arr = line.split(",");
			if(arr[0].compareTo(crn) == 0) {
				String[] delLecture = arr;
				if(arr.length < 3) { //labs have only two components
					System.out.println("Can not delete a lab");
					return;
				}
				if(scanner.hasNextLine()) { //in case the deleted course is on last line
					line = scanner.nextLine();
					arr = line.split(",");
					while(arr.length < 3 && scanner.hasNextLine()) { //while the read line is a lab, it is not added to strList
						line = scanner.nextLine();
						arr = line.split(",");
					}
					if(arr.length > 2) // if a line has 3 or more components, it contains a non-deleted lecture
						strList.add(line); //which must be added back to the text file
				}
				System.out.println("[ " + delLecture[0] + "," + delLecture[1] + "," + delLecture[2] + " ] deleted!");
			}
			else {
				strList.add(line);
			}
		}
		
		//print the non-deleted courses back to file
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
	
	public static void addStudent() {
		Scanner scan = new Scanner(System.in);
		String stuID = "";
		try {
			System.out.println("Enter Student's ID: ");
			stuID = scan.nextLine();
			if(stuID.length() != 6) //check if id is 6 characters long
				throw new IdException();
			char[] idCharArr = stuID.toCharArray();
			for(int i = 0; i < 6; i++) {
				//check if first two characters are letters
				if(i < 2) {
					char c = Character.toUpperCase(idCharArr[i]);
					if(!(c > 64 && c < 91))
						throw new IdException();
				}
				
				//check if last four characters are digits
				else {
					if(!(idCharArr[i] > 47 && idCharArr[i] < 58))
						throw new IdException();
				}
			}
			
			//check if ID is unique
			if(stuList != null) {
				for(Student s : stuList) {
					if((s.getId()).compareToIgnoreCase(stuID) == 0)
						throw new IdException();
				}
			}
		}
		catch(IdException e) {
			System.out.println(e.getMessage());
			return;
		}
		System.out.println("Student Type (PhD, MS or Undergrad): ");
		String stuType = scan.nextLine();
		try {
			System.out.println("Enter remaining information\n");
			String[] remInfo = (scan.nextLine()).split("\\|");
			switch(stuType) {
				case "PhD":
					//must convert all courses TAed into integers
					String[] crnStr = remInfo[3].split(",");
					int[] crn = new int[crnStr.length];
					for(int i = 0; i < crnStr.length; i++)
						crn[i] = Integer.parseInt(crnStr[i]);
					
					PhdStudent pStu = new PhdStudent(remInfo[0], stuID, remInfo[1], remInfo[2], crn);
												          //name, id, advisor, research subject, TA courses
					stuList.add(pStu);
					break;
				case "MS":
					//must convert all courses taken/TAed into integers
					crnStr = remInfo[1].split(","); //courses taken
					crn = new int[crnStr.length];
					for(int i = 0; i < crnStr.length; i++)
						crn[i] = Integer.parseInt(crnStr[i]);
					
					String[] crnStrTA = remInfo[2].split(","); //courses student is TA
					int[] crnTA = new int[crnStrTA.length];
					for(int i = 0; i < crnStrTA.length; i++)
						crnTA[i] = Integer.parseInt(crnStrTA[i]);
					
					MsStudent mStu = new MsStudent(remInfo[0], stuID, crn, crnTA);
					                              //name, id, course taken, TA courses
					stuList.add(mStu);
					break;
				case "Undergrad":
					//must convert all input fields into respective types
					crnStr = remInfo[1].split(",");
					crn = new int[crnStr.length];
					for(int i = 0; i < crnStr.length; i++)
						crn[i] = Integer.parseInt(crnStr[i]);
					double gpa = Double.parseDouble(remInfo[2]);
					boolean res = Boolean.parseBoolean(remInfo[3]);
					
					UndergraduateStudent uStu = new UndergraduateStudent(remInfo[0], stuID, crn, gpa, res);
					stuList.add(uStu);
					break;
			}
			System.out.println("[ " + remInfo[0] + "] added!\n");
		}
		catch(Exception e) {
			System.out.print("Must input data in following pattern: \n\t");
			switch(stuType) {
				case "PhD":
					System.out.println("Student Name|Student ID|Advisor|Research Subject|Courses TAed\n");
					break;
				case "MS":
					System.out.println("Student Name|Student ID|Courses Taken|Courses TAed\n");
					break;
				case "Undergrad":
					System.out.println("Student Name|Student ID|Courses Taken|GPA|Resident Status (true/false) \n");
					break;
			}
		}
	}
	
	public static void deleteStudentById(String id) {
	    boolean found = false;
	    for (Iterator<Student> iterator = stuList.iterator(); iterator.hasNext(); ) {
	        Student stu = iterator.next();
	        if (stu.getId().compareToIgnoreCase(id) == 0) {
	            iterator.remove();
	            System.out.println("[ " + stu.getName() + " ] deleted!\n");
	            found = true;
	            break; 
	        }
	    }
	    if (!found) {
	        System.out.println("Student not found.\n");
	    }
	}

	public static void printFeeInvoice(String stuID) {
		for(Student s : stuList) {
			if((s.getId()).compareTo(stuID) == 0) { //if a student in the student arraylist has the stuID
				s.printInvoice(); //then call the printInvoice() method on the student object
				return;
			}
		}
		System.out.println("Student not found.\n"); //stuID not belonging to student arraylist
	}
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

	public int getListOfCrdHrs(int crn) throws FileNotFoundException {
		String line = "";
		File file = new File("C:\\Users\\beric\\OneDrive\\Documents\\lec.txt");
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) { //read text file
			line = scanner.nextLine();
			String[] arr = line.split(",");
			if(Integer.parseInt(arr[0]) == crn) {
				if(arr.length > 2) { //crn is a lecture
					if((arr[4].toUpperCase()).compareTo("ONLINE") == 0) { //if lecture is online, then the credit hours is at the 6th index
						return Integer.parseInt(arr[5]);
					}
					else {
						return Integer.parseInt(arr[7]); //if lecture is not online, then the credit hours is at the 8th index
					}
				}
				else { //crn is a lab
					return 0; //labs have no credit hours
				}
			}
		}
		return -1;
	}
		
	public String getListOfCrs(int crn) throws FileNotFoundException{
		String line = "";
		File file = new File("C:\\Users\\beric\\OneDrive\\Documents\\lec.txt");
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] arr = line.split(",");
			if(Integer.parseInt(arr[0]) == crn) { //if course number is found in text file
				return arr[1]; //return the course code at second element for all labs & lectures
			}
		}
		return "Course not found";
	}
	
	abstract public void printInvoice();
}

class UndergraduateStudent extends Student{
	int [] undergradCrnsTaken;
	double gpa;
	boolean resident;
	
	public UndergraduateStudent(String name, String id, int [] undergradCrnsTaken, double gpa, boolean resident) {
		super (name, id.toUpperCase());
		this.undergradCrnsTaken = undergradCrnsTaken;
		this.gpa = gpa;
		this.resident = resident;
	}

	@Override
	public void printInvoice() {
		System.out.print("VALENCE COLLEGE\n" +
						 "ORLANDO FL 10101\n" +
						 "---------------------\n\n" +
						 "Fee Invoice Prepared for Student:\n" +
						 (this.getId()).toUpperCase() + "-" + (this.getName()).toUpperCase() +"\n\n" +
						 "1 Credit Hour = $120.25\n\n" +
						 "CRN   CR_PREFIX   CR_HOURS\n");
		double total = 35;
		for(int undergradCrns : undergradCrnsTaken) {
			int CrdHrs = 0;
			String courseCode = "";
			try {
				CrdHrs = getListOfCrdHrs(undergradCrns);
				courseCode = getListOfCrs(undergradCrns);
			} catch (FileNotFoundException e) {
				System.out.println("File error / file not found");
			}
			double cost = CrdHrs*120.25;
			total += cost;
			System.out.printf("" + undergradCrns + "  " + courseCode + "     " + CrdHrs + "           $ %.2f" + "\n", cost);
		}
		System.out.printf("\n            Health & id fees  $ 35.00\n\n" +
						 "--------------------------------------\n" +
						 "                              $ %.2f" + "\n", total);
		if(this.gpa > 3.5 && total > 500) {
			System.out.printf("                             -$ %.2f" + "\n", (total*0.25));
			total *= 0.75;
		}
		if(this.resident == false) { total *= 2; }
		System.out.printf("                            ----------\n" +
						 "            TOTAL PAYMENTS    $ %.2f" + "\n\n\n", total);
	}
}

abstract class GraduateStudent extends Student{
	private int[] crnTA;
	
	public GraduateStudent (String name, String id, int[] crn) {
		//crn is the crn that the grad student is a teaching assistant for
		super(name, id.toUpperCase());
		this.crnTA = crn;
	}
	
	public int[] getCrnTA() {
		return crnTA;
	}
}

class PhdStudent extends GraduateStudent{
	private String advisor;
	private String researchSubject;
	
	public PhdStudent (String name, String id, String advisor, String researchSubject, int[] crn) {
		//crn is the crn that the grad student is a teaching assistant for
		super(name, id, crn);
		this.advisor = advisor;
		this.researchSubject = researchSubject;
	}

	@Override
	public void printInvoice() {
		System.out.print("VALENCE COLLEGE\n" +
						 "ORLANDO FL 10101\n" +
						 "---------------------\n\n" +
						 "Fee Invoice Prepared for Student:\n" +
						 (this.getId()).toUpperCase() + "-" + (this.getName()).toUpperCase() +"\n\n");
		System.out.print("RESEARCH\n" +
						  researchSubject + "                $ 700.00\n\n" +
						  "            Health & id fees  $ 35.00\n\n" +
						  "--------------------------------------\n");
		double total = 735;
		System.out.printf("                              $ %.2f\n", total);
		if(this.getCrnTA().length == 2) {
			System.out.printf("                             -$ %.2f" + "\n", (total*0.5));
			total *= 0.5;
		}
		else if(this.getCrnTA().length >= 3) {
			System.out.printf("                             -$ %.2f\n", (total-35));
			total -= 700;
		}
		System.out.printf("            TOTAL PAYMENTS    $ %.2f\n\n\n", total);
	}
}

class MsStudent extends GraduateStudent{
private int [] gradCrnsTaken;
	
	public MsStudent (String name, String id, int [] gradCrnsTaken, int[] crn) {
		// gradCoursesTaken is the array of the crns that the Ms student is taking
		//crn is the course number that the Phd student is a teaching assistant for
		super(name, id, crn);
		this.gradCrnsTaken = gradCrnsTaken;
	}

	@Override
	public void printInvoice() {
		System.out.print("VALENCE COLLEGE\n" +
						 "ORLANDO FL 10101\n" +
						 "---------------------\n\n" +
						 "Fee Invoice Prepared for Student:\n" +
						 (this.getId()).toUpperCase() + "-" + (this.getName()).toUpperCase() +"\n\n" +
						 "1 Credit Hour = $300.00\n\n" +
						 "CRN   CR_PREFIX   CR_HOURS\n");
		double total = 35;
		for(int gradCrns : gradCrnsTaken) {
			int CrdHrs = 0;
			String courseCode = "";
			try {
				CrdHrs = getListOfCrdHrs(gradCrns);
				courseCode = getListOfCrs(gradCrns);
			} catch (FileNotFoundException e) {
				System.out.println("File error / file not found");
			}
			double cost = CrdHrs*300.00;
			total += cost;
			System.out.printf("" + gradCrns + "  " + courseCode + "     " + CrdHrs + "           $ %.2f" + "\n", cost);
		}
		System.out.printf("\n            Health & id fees  $ 35.00\n\n" +
						 "--------------------------------------\n" +
						 "            TOTAL PAYMENTS    $ %.2f" + "\n\n\n", total);
	}
}

class IdException extends Exception{
	public String getMessage() {
		return "Invalid id format or ID already exists\nTry again later!\n";
	}
}
