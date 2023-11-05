package projects;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.dao.DbConnection;
import projects.exception.DbException;
import projects.service.ProjectService;
import projects.entity.Project;

public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();

	//@formatter:off
	private List<String> operations = List.of(
			"1) Add a project"
			);
	//@formater:on
/**
 * 
 * @param args
 */
	public static void main(String[] args ) {
		new ProjectsApp().processuserSelection();
	
	}
	/**
	 * 
	 */
	private void processuserSelection() {
		boolean done = false;
		while(!done) {
			try {
			int operation = getUserSelection();
			switch(operation) {
			case -1:
				done = exitMenu();
				break;
		
			case 1:
				createProject();
				break;	
				default:
					System.out.println("\n" + operation + " is not valid. Try again.");
					break;
			}
			} catch(Exception e) {
				System.out.println("\nError "+ e.toString()+" Try again.");
			}
		}
}
	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours"); 
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)"); 
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setNotes(notes);
		project.setDifficulty(difficulty);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You added this project:\n" +dbProject);
		
		
	}
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput (prompt);
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return new BigDecimal(input).setScale(2);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}
	private boolean exitMenu() {
		System.out.println("\nExiting the menu. TTFN!");
		return true;
	}
	/**
	 * 
	 * @return
	 */
	private int getUserSelection() {
		printOperations();
		Integer input = getIntInput("\nEnter an operation number (press Enter to quit)");
		
		return Objects.isNull(input) ? -1 :input;
		
	}
	
	private void printOperations() {
		System.out.println();
		System.out.println("Here's what you can do:");
		
		operations.forEach(op -> System.out.println("   " + op));
		
	}

/**
 * 
 * @param prompt
 * @return
 */
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.parseInt(input);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}
	
	
	/**
	 * 
	 * @param prompt
	 * @return
	 */
	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String line = scanner.nextLine();
		
		return line.isBlank() ? null : line.trim();
	}
}