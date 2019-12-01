package es.salesianos.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.salesianos.model.UserTeam;

public class UserTeamRepository extends AbstractRepository implements Repository<UserTeam> {
	


	public List<UserTeam> listAll() {
		Connection conn = manager.open(jdbcUrl);
		PreparedStatement preparedStatement = null;
		ArrayList<UserTeam> users = new ArrayList<UserTeam>();
		try {
			preparedStatement = conn
					.prepareStatement("SELECT u.name as nb, u.apellido as ap, t.nombreEquipo as ne* "
							+ "FROM User u JOIN Team t ON u.codEquipo=t.id");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				UserTeam user = new UserTeam();
			
				user.setNombreEquipo(resultSet.getString("ne"));
				users.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			manager.close(preparedStatement);
			manager.close(conn);
		}
		return users;
	}

	@Override
	public UserTeam findBy(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(UserTeam user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(UserTeam user) {
		// TODO Auto-generated method stub

	}

}
