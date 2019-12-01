package es.salesianos.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.salesianos.model.User;
import es.salesianos.model.UserTeam;
import es.salesianos.service.Service;
import es.salesianos.service.UserService;

public class ListadoServlet extends HttpServlet {
	private static final String jdbcUrl = "jdbc:h2:file:./src/main/resources/test;INIT=RUNSCRIPT FROM 'classpath:scripts/create.sql'";

	Service service = new UserService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<User> users = service.listAll();
		req.setAttribute("listOfUsers", users);
		Connection conn;
		ArrayList<UserTeam> userst = new ArrayList<UserTeam>();
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection(jdbcUrl, "sa", "");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement("SELECT * FROM TEAM");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				UserTeam user = new UserTeam();
				user.setId(resultSet.getInt("id"));
				user.setNombreEquipo(resultSet.getString("nombre"));
				userst.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		List<UserTeam> usersTeam = userst;
		req.setAttribute("listOfUsersTeam", usersTeam);
		redirect(req, resp);
	}

	protected void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/Listado.jsp");
		dispatcher.forward(req, resp);
	}


}
