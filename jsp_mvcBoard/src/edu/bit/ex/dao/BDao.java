package edu.bit.ex.dao;

import java.sql.Connection; //sql!
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context; //�� �ڹٿ������̹� �ҷ��ð�!
import javax.naming.InitialContext;

import javax.sql.DataSource; //�� sql���� �޾ƿ���.

import edu.bit.ex.dto.BDto;

public class BDao {
	DataSource dataSource; // �̰� �ٷ� Ŀ�ؼ� Ǯ!!! -> ������ �ҽ�

	public BDao() {
		// TODO Auto-generated constructor stub

		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/oracle");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public ArrayList<BDto> list() {

		ArrayList<BDto> dtos = new ArrayList<BDto>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;// ?
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();

			String query = "select bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent from mvc_board order by bGroup desc, bStep asc";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int bId = resultSet.getInt("bId");
				String bName = resultSet.getString("bName");
				String bTitle = resultSet.getString("bTitle");
				String bContent = resultSet.getString("bContent");
				Timestamp bDate = resultSet.getTimestamp("bDate");

				int bHit = resultSet.getInt("bHit");
				int bGroup = resultSet.getInt("bGroup");
				int bStep = resultSet.getInt("bStep");
				int bIndent = resultSet.getInt("bIndent");

				BDto dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
				dtos.add(dto);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return dtos;
	}

	public void write(String bName, String bTitle, String bContent) {
		// TODO Auto-generated method stub

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			String query = "insert into mvc_board (bId, bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) values (mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0 )";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, bName);
			preparedStatement.setString(2, bTitle);
			preparedStatement.setString(3, bContent);

			int rn = preparedStatement.executeUpdate();
			// ó���� ���ڵ� �� ��ȯ sql���� �޼ҵ� ������Ʈ, �ۼ�, �� ����
			// Ȱ�� -> 1�̸� ���� -1�̸� ���� // �����ϸ� �ٸ��������� �Ѿ��� �ȳ��Ҽ��� �ִ�.

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}

	}

	public BDto reply_view(String bId) {
		// TODO Auto-generated method stub
		BDto dto = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {

			connection = dataSource.getConnection();
			String query = "select * from mvc_board where bId = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(bId));
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int bid = resultSet.getInt("bId");
				String bName = resultSet.getString("bName");
				String bTitle = resultSet.getString("bTitle");
				String bContent = resultSet.getString("bContent");
				Timestamp bDate = resultSet.getTimestamp("bDate");
				int bHit = resultSet.getInt("bHit");
				int bGroup = resultSet.getInt("bGroup");
				int bStep = resultSet.getInt("bStep");
				int bIndent = resultSet.getInt("bIndent");

				dto = new BDto(bid, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}

		return dto;
	}

	public void modify(String bId, String bName, String bTitle, String bContent) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();

			String query = "update mvc_board set bName = ?, bTitle = ?, bContent = ? where bId = ?";
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, bName);
			preparedStatement.setString(2, bTitle);
			preparedStatement.setString(3, bContent);
			preparedStatement.setInt(4, Integer.parseInt(bId));

			int rn = preparedStatement.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

	public void delete(String bId) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {

			connection = dataSource.getConnection();
			String query = "delete from mvc_board where bId = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(bId));
			int rn = preparedStatement.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

	public BDto contentView(String bId) {
		// TODO Auto-generated method stub

		upHit(bId);

		BDto dto = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = dataSource.getConnection();

			String query = "select * from mvc_board where bId = ?";
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int bid = resultSet.getInt("bId");
				String bName = resultSet.getString("bName");
				String bTitle = resultSet.getString("bTitle");
				String bContent = resultSet.getString("bContent");
				Timestamp bDate = resultSet.getTimestamp("bDate");
				int bHit = resultSet.getInt("bHit");
				int bGroup = resultSet.getInt("bGroup");
				int bStep = resultSet.getInt("bStep");
				int bIndent = resultSet.getInt("bIndent");

				dto = new BDto(bid, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return dto;
	}

	private void upHit(String bId) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			String query = "update mvc_board set bHit = bHit + 1 where bId = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, bId);

			int rn = preparedStatement.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

	public void reply(String bId, String bName, String bTitle, String bContent, String bGroup, String bStep,
			String bIndent) {
		// TODO Auto-generated method stub
		// ���� ��ĭ�� �о
		replyShape(bGroup, bStep);

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			String query = "insert into mvc_board (bId, bName, bTitle, bContent, bGroup, bStep, bIndent) values (mvc_board_seq.nextval, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(query);
			// ���� ���� ���
			preparedStatement.setString(1, bName);
			preparedStatement.setString(2, bTitle);
			preparedStatement.setString(3, bContent);
			preparedStatement.setInt(4, Integer.parseInt(bGroup));
			preparedStatement.setInt(5, Integer.parseInt(bStep) + 1);
			preparedStatement.setInt(6, Integer.parseInt(bIndent) + 1);

			int rn = preparedStatement.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}

	}

	private void replyShape(String bGroup, String bStep) {
		// TODO Auto-generated method stub
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = dataSource.getConnection();
			String query = "update mvc_board set bStep = bStep + 1 where bGroup = ? and bStep > ?";
			preparedStatement = connection.prepareStatement(query);

			int rn = preparedStatement.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}

}
