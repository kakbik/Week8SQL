package projects.dao;

import java.util.List;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;

import provided.util.DaoBase;
import projects.dao.DbConnection;
import projects.entity.Project;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectDao extends DaoBase {
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECT_TABLE = "project";
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	private static final String STEP_TABLE = "step";
	

	public Project insertProject(Project recipe) {
	//@formatter:off
		String sql = ""
			+ "INSERT INTO " + PROJECT_TABLE + " "
			+ "(project_name, estimated_hours, actual_hours, difficulty, notes) "
			+ "VALUES "
			+ "(?, ?, ?, ?, ?)";
	//@formatter:on

		try (Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				setParameter(stmt, 1, recipe.getProjectName(), String.class);
				setParameter(stmt, 2, recipe.getEstimatedHours(), BigDecimal.class);
				setParameter(stmt, 3, recipe.getActualHours(), BigDecimal.class);
				setParameter(stmt, 4, recipe.getDifficulty(), Integer.class);
				setParameter(stmt, 5, recipe.getNotes(), String.class);

				stmt.executeUpdate();
				Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
				
				commitTransaction(conn);
				recipe.setProjectId(projectId);;
				return recipe;

			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		} catch (SQLException e) {
			throw new DbException(e);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void executeBatch(List<String> sqlBatch) {
		
		try (Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			try(Statement stmt = conn.createStatement()){
				for (String sql : sqlBatch) {
					stmt.addBatch(sql); 
				}
				
				stmt.executeBatch();
				commitTransaction(conn);
			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DbException(e);
		}
	
	}
}
