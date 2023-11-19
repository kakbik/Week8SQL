package projects.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;
import projects.entity.Project;

public class ProjectService {
		private static final String SCHEMA_FILE = "projects_schema.sql";
		
		private ProjectDao projectDao = new ProjectDao();
		
		public void createAndPopulateTables() {
			LoadFromFile(SCHEMA_FILE);
			
		}
	/**
	 * @param fileName
	 */
		private void LoadFromFile(String fileName) {
			String content = readFileContent(fileName);
			List<String> sqlStatements = convertContentToSqlStatements(content);
			
			projectDao.executeBatch(sqlStatements);
		}
		
		/**
		 * 
		 * @param content
		 * @return
		 */
	private List<String> convertContentToSqlStatements(String content) {
		content = removeComments(content);
		content = replaceWhitespaceSequencesWithSingleSpace(content);
		
		return extractLinesFromContent(content);
		
	}
	/**
	 * 
	 * @param content
	 * @return
	 */
	private List<String> extractLinesFromContent(String content) {
	List<String> lines = new LinkedList<>();	

	while(!content.isEmpty()) {
		int semicolon = content.indexOf(";");
		if(semicolon == -1) {
			if (!content.isBlank()) {
				lines.add(content);
			}
			content = "";
		}
		else {
			lines.add(content.substring(0,semicolon).trim());
			content = content.substring(semicolon+1);
		}
	}
	return lines;
	}
	/**
	 * 
	 * @param content
	 * @return
	 */
	private String replaceWhitespaceSequencesWithSingleSpace(String content) {
		
		
			return content.replaceAll("\\s+", " ");
		}
	/**
	 * 
	 * @param content
	 * @return
	 */
	private String removeComments(String content) {
		StringBuilder builder = new StringBuilder(content);
		int commentPos = 0;
		while ((commentPos = builder.indexOf("-- ", commentPos))!= -1 ) {
			int eolPos = builder.indexOf("\n", commentPos +1);
			
			if(eolPos == -1) {
				builder.replace(commentPos,  builder.length(), "");
			}
			else {
				builder.replace(commentPos, eolPos+1, "");
			}
		}
		return builder.toString();
		}
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	private String readFileContent(String fileName) {
		
		try {
			Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
			return Files.readString(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		throw new DbException(e);
		}
		
	  } 
	//this main will create a table in projects schema
	//    public static void main(String[] args) {
	//	new ProjectService().createAndPopulateTables();
	//}

	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}
	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}
	public Project fetchProjectById(Integer projectId) {
		 return projectDao.fetchProjectById(projectId)
			        .orElseThrow(() -> new NoSuchElementException(
			            "Project with id=" + projectId + " does not exist"));
	}
	public void modifyProjectDetails(Project project) {
		if(!projectDao.modifyProjectDetails(project)) {
			throw new DbException("Project with ID= "+project.getProjectId()+" does not exist.");
			
		}
	}
	public void deleteProject(Integer projectId) {
		if(!projectDao.deleteProject(projectId)) {
			throw new DbException("Project with ID= " + projectId + " does not exist.");
		}
	}
	 
}
