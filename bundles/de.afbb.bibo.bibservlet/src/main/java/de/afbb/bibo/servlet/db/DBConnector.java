/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servlet.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;

import de.afbb.bibo.servlet.Config;
import de.afbb.bibo.share.model.Borrower;
import de.afbb.bibo.share.model.Copy;
import de.afbb.bibo.share.model.Curator;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.MediumType;

/**
 *
 * @author fi13.pendrulat
 */
public class DBConnector {

	private static DBConnector INSTANCE;
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(DBConnector.class);

	private Connection connect = null;
	// private String url = "192.168.168.156";
	// private String userName = "bibo";
	// private String password = "b1b0";

	public static DBConnector getInstance() throws SQLException {
		log.debug("access database...");
		if (INSTANCE == null) {
			INSTANCE = new DBConnector();
		}
		return INSTANCE;
	}

	private DBConnector() throws SQLException {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
		}
		// Setup the connection with the DB
		connect = DriverManager.getConnection(
				"jdbc:mysql://" + Config.URL + "?" + "user=" + Config.USER_NAME + "&password=" + Config.PASSWORD);
	}

	public int getCuratorId(String curatorName) throws SQLException {
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(
					"select Id from " + Config.DATABASE_NAME + ".benutzer where " + "Name='" + curatorName + "'")) {
				resultSet.first();
				return resultSet.getInt(1);
			}
		}
	}

	public int createCurator(Curator user) throws SQLException {
		log.debug("add curator: " + user);
		try (Statement statement = connect.createStatement()) {
			statement.execute("insert into " + Config.DATABASE_NAME + ".benutzer (Name, Hash, Salt) values ("
					+ user.getName() + ", '" + user.getPasswordHash() + "', '" + user.getSalt() + "')");
			return getCuratorId(user.getName());
		}
	}

	public void updateCurator(Curator user) throws SQLException {
		log.debug("update curator: " + user);
		try (Statement statement = connect.createStatement()) {
			statement.execute(
					"update " + Config.DATABASE_NAME + ".benutzer set " + "Name='" + user.getName() + "', Hash='"
							+ user.getPasswordHash() + "', Salt='" + user.getSalt() + "' where Id=" + user.getId());
		}
	}

	public boolean checkPassword(String name, String passwordHash) throws SQLException {
		log.debug("check Password: name: " + name + ", hash: " + passwordHash);
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(
					"select Hash from " + Config.DATABASE_NAME + ".benutzer where " + "Name='" + name + "'")) {
				resultSet.first();
				return resultSet.getString(1).equals(passwordHash);
			}
		}
	}

	public String requestSalt(String name) throws SQLException {
		log.debug("request salt for user: " + name);
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(
					"select Salt from " + Config.DATABASE_NAME + ".benutzer where " + "Name='" + name + "'")) {
				resultSet.first();
				return resultSet.getString(1);
			}
		}
	}

	public List<Curator> getCurator() throws SQLException {
		List<Curator> result = new ArrayList();
		log.debug("list all users");
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement
					.executeQuery("select Id, Name, Salt from " + Config.DATABASE_NAME + ".benutzer")) {
				while (resultSet.next()) {
					result.add(new Curator(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), null));
				}
			}
		}
		return result;
	}

	public void deleteCurator(String id) throws SQLException {
		log.debug("delete user with id: " + id);
		try (Statement statement = connect.createStatement()) {
			statement.execute("delete from " + Config.DATABASE_NAME + ".benutzer where Id=" + id + "");
		}
	}

	public int createMediumType(String name, String iconPath) throws SQLException, IOException {
		String sql = "insert into " + Config.DATABASE_NAME + ".typ (TypName, Icon) values (?, ?)";
		log.debug("create medium type: {name: " + name + ", iconPath: " + iconPath + "}");
		try (PreparedStatement statement = connect.prepareStatement(sql)) {
			statement.setString(1, name);
			statement.setString(2, iconPath);
			statement.execute();
			try (ResultSet resultSet = statement
					.executeQuery("select Id from " + Config.DATABASE_NAME + ".typ where " + "Name='" + name + "'")) {
				resultSet.first();
				return resultSet.getInt(1);
			}
		}
	}

	public List<MediumType> getMediumTypes() throws SQLException {
		List<MediumType> result = new ArrayList();
		log.debug("list all medium types");
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement
					.executeQuery("select Id, TypName, Icon from " + Config.DATABASE_NAME + ".typ")) {
				while (resultSet.next()) {
					result.add(new MediumType(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
				}
			}
		}
		log.debug("fetched from database: " + result);
		return result;
	}

	public void deleteMediumType(String id) throws SQLException {
		log.debug("delete mediumtype with id: " + id);
		try (Statement statement = connect.createStatement()) {
			statement.execute("delete from " + Config.DATABASE_NAME + ".typ where Id='" + id + "'");
		}
	}

	public int createMedium(String isbn, String title, String author, String language, int typeId) throws SQLException {
		String sql = "insert into " + Config.DATABASE_NAME
				+ ".medium (ISBN, Titel, Autor, Sprache, TypId) values (?, ?, ?, ?, ?)";
		log.debug("create new medium: {isbn: " + isbn + ", title: " + title + ", author: " + author + ", language: "
				+ language + "}");
		try (PreparedStatement statement = connect.prepareStatement(sql)) {
			statement.setString(1, isbn);
			statement.setString(2, title);
			statement.setString(3, author);
			statement.setString(4, language);
			statement.setInt(5, typeId);
			statement.execute();
			try (ResultSet resultSet = statement.executeQuery(
					"select Id from " + Config.DATABASE_NAME + ".medium where " + "ISBN='" + isbn + "'")) {
				resultSet.first();
				return resultSet.getInt(1);
			}
		}
	}

	public Medium getMedium(String isbn) throws SQLException {
		log.debug("get medium with isbn: " + isbn);
		try (Statement statement = connect.createStatement()) {
			try (ResultSet mediaSet = statement
					.executeQuery("select TypId, Id, ISBN, Titel, Autor, Sprache, Herausgeber from "
							+ Config.DATABASE_NAME + ".medium where ISBN='" + isbn + "'")) {
				mediaSet.first();
				return new Medium(mediaSet.getInt(2), mediaSet.getString(3), mediaSet.getString(4),
						mediaSet.getString(5), mediaSet.getString(6), mediaSet.getInt(1), mediaSet.getString(7));

			}
		}
	}

	public List<Medium> getMedium() throws SQLException {
		List<Medium> result = new ArrayList();
		log.debug("get all media");
		try (Statement statement = connect.createStatement()) {
			try (ResultSet mediaSet = statement
					.executeQuery("select TypId, Id, ISBN, Titel, Autor, Sprache, Herausgeber from "
							+ Config.DATABASE_NAME + ".medium")) {
				while (mediaSet.next()) {
					result.add(new Medium(mediaSet.getInt(2), mediaSet.getString(3), mediaSet.getString(4),
							mediaSet.getString(5), mediaSet.getString(6), mediaSet.getInt(1), mediaSet.getString(7)));
				}
			}
		}
		log.debug("fetched from database: " + result);
		return result;
	}

	public void deleteMedium(String id) throws SQLException {
		log.debug("delete medium with id: " + id);
		try (Statement statement = connect.createStatement()) {
			statement.execute("delete from " + Config.DATABASE_NAME + ".medium where Id='" + id + "'");
		}
	}

	private int createGroup() throws SQLException {
		try (Statement st = connect.createStatement()) {
			st.execute("insert into " + Config.DATABASE_NAME + ".gruppe () values ()");
			try (ResultSet groupResultSet = st.executeQuery("SELECT LAST_INSERT_ID()")) {
				groupResultSet.first();
				return groupResultSet.getInt(0);
			}
		}
	}

	/**
	 * Create a number of copies in the "exemplar" table. The copies remain in
	 * the same group. The input are two arrays. The first input of editions is
	 * the same copy as the first input in barcodeIds and so on.
	 *
	 * @param copies
	 * @return the groupId
	 * @throws SQLException
	 */
	public int createCopyGroup(Copy[] copies) throws SQLException {
		int groupId = createGroup();
		log.debug("create new group of copies: " + Arrays.toString(copies));
		log.debug("created new group for media. groupId: " + groupId);
		for (Copy copy : copies) {
			String sql = "insert into " + Config.DATABASE_NAME
					+ ".exemplar (Edition, Barcode, Inventarisiert, Zustand, GruppenId, MedienId) values (?, ?, ?, ?, ?, ?)";

			try (PreparedStatement statement = connect.prepareStatement(sql)) {
				statement.setString(1, copy.getEdition());
				statement.setString(2, copy.getBarcode());
				statement.setDate(3, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
				statement.setString(4, "neu");
				statement.setInt(5, groupId);
				statement.setInt(6, copy.getMediumId());
				statement.execute();
			}
		}

		return groupId;
	}

	public Collection<List<Copy>> getCopies() throws SQLException {
		Map<Integer, List<Copy>> result = new HashMap();
		log.debug("get all copies");
		try (Statement statement = connect.createStatement()) {
			try (ResultSet copySet = statement.executeQuery("select Id, Edition, Barcode, Inventarisiert, Zustand, "
					+ "AusleihDatum, LetztesAusleihDatum, AusleihBenutzerId, LetzterAusleihBenutzerId, AusleiherId, "
					+ "LetzerAusleiherId, MedienId, GruppenId from " + Config.DATABASE_NAME + ".exemplar")) {
				while (copySet.next()) {
					int mediaId = copySet.getInt(12);
					int groupId = copySet.getInt(13);
					try (ResultSet mediaSet = statement
							.executeQuery("select TypId, ISBN, Titel, Autor, Sprache, Herausgeber from "
									+ Config.DATABASE_NAME + ".medium WHERE Id='" + mediaId + "'")) {
						mediaSet.first();
						int copyId = copySet.getInt(1);
						String edition = copySet.getString(2);
						String barcode = copySet.getString(3);
						Date inventorised = copySet.getDate(4);
						String condition = copySet.getString(5);
						Date borrowDate = copySet.getDate(6);
						Date lastBorrowDate = copySet.getDate(7);
						int curatorId = copySet.getInt(8);
						int lastCuratorId = copySet.getInt(9);
						int borrowerId = copySet.getInt(10);
						int lastBorrowerId = copySet.getInt(11);
						int typeId = mediaSet.getInt(1);
						String isbn = mediaSet.getString(2);
						String title = mediaSet.getString(3);
						String author = mediaSet.getString(4);
						String language = mediaSet.getString(5);
						String publisher = mediaSet.getString(6);
						Copy copy = new Copy(copyId, edition, barcode, inventorised, condition, borrowDate,
								lastBorrowDate, groupId, borrowerId, lastBorrowerId, curatorId, lastCuratorId, mediaId,
								isbn, title, author, language, typeId, publisher);
						if (!result.containsKey(groupId)) {
							result.put(groupId, new ArrayList());
						}
						result.get(groupId).add(copy);
					}
				}
			}
		}

		return result.values();
	}

	public int createBorrower(Borrower borrower) throws SQLException {
		log.debug("create new borrower: " + borrower);
		String sql = "insert into " + Config.DATABASE_NAME
				+ ".ausleiher (Vorname, Nachname, Info, EMail, Telefon) values (?, ?, ?, ?, ?)";
		try (PreparedStatement statement = connect.prepareStatement(sql)) {
			statement.setString(1, borrower.getForename());
			statement.setString(2, borrower.getSurname());
			statement.setString(3, borrower.getInfo());
			statement.setString(4, borrower.getEmail());
			statement.setString(5, borrower.getPhoneNumber());
			statement.execute();
			try (ResultSet resultSet = statement
					.executeQuery("select Id from " + Config.DATABASE_NAME + ".ausleiher where " + "Vorname='"
							+ borrower.getForename() + "' and Nachname='" + borrower.getSurname() + "'")) {
				resultSet.first();
				return resultSet.getInt(1);
			}
		}
	}
	
	public int getBorrowerId(String fName, String sName) throws SQLException {
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(
					"select Id from " + Config.DATABASE_NAME + ".ausleiher where " + "Vorname='" + fName + "' ans Nachname='" + sName + "'")) {
				resultSet.first();
				return resultSet.getInt(1);
			}
		}
	}

	public List<Borrower> getBorrower() throws SQLException {
		List<Borrower> result = new ArrayList();
		log.debug("get all borrower");
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(
					"select Id, Vorname, Nachname, Info, EMail, Telefon from " + Config.DATABASE_NAME + ".ausleiher")) {
				while (resultSet.next()) {
					result.add(new Borrower(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
							resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)));
				}
			}
			return result;
		}
	}
	
	public void deleteBorrower(int id) throws SQLException {
		log.debug("delete borrower with id: " + id);
		try (Statement statement = connect.createStatement()) {
			statement.execute("delete from " + Config.DATABASE_NAME + ".ausleiher where Id=" + id + "");
		}
	}
	
	public void updateBorrower(Borrower b) throws SQLException {
		log.debug("update borrower: " + b);
		try (Statement statement = connect.createStatement()) {
			statement.execute(
					"update " + Config.DATABASE_NAME + ".benutzer set " 
			+ "Vorname='" + b.getForename() 
			+ "', Nachname='" + b.getSurname()
			+ "', Info='" + b.getInfo() 
			+ "', EMail='" + b.getEmail()
			+ "', Telefon'" + b.getPhoneNumber() 
			+ "' where Id=" + b.getId());
		}
	}

	private int getLastBorrower(String barcode) throws SQLException {
		try (Statement st = connect.createStatement()) {
			try (ResultSet resultSet = st.executeQuery(
					"select AusleiherId from " + Config.DATABASE_NAME + ".exemplar where barcode='" + barcode + "';")) {
				return resultSet.getInt(1);
			}
		}
	}

	private int getLastCurator(String barcode) throws SQLException {
		try (Statement st = connect.createStatement()) {
			try (ResultSet resultSet = st.executeQuery("select AusleihBenutzerId from " + Config.DATABASE_NAME
					+ ".exemplar where barcode='" + barcode + "';")) {
				return resultSet.getInt(1);
			}
		}
	}

	private String getLastBorrowDate(String barcode) throws SQLException {
		try (Statement st = connect.createStatement()) {
			try (ResultSet resultSet = st.executeQuery("select AusleihDatum from " + Config.DATABASE_NAME
					+ ".exemplar where barcode='" + barcode + "';")) {
				return resultSet.getString(1);
			}
		}
	}

	public void setNewBorrower(String barcode, int borrowerId, int curatorId) throws SQLException {
		String sql = "update " + Config.DATABASE_NAME + ".exemplar set " + ", AusleihBenutzerId='" + curatorId + "'"
				+ ", AusleiherId='" + borrowerId + "'" + ", AusleihDatum=NOW()" + " where Barcode='" + barcode + "'";
		try (Statement st = connect.createStatement()) {
			st.execute(sql);
		}
	}

	public void returnBook(String barcode) throws SQLException {
		String sql = "update " + Config.DATABASE_NAME + ".exemplar set " + "LetzterAusleihBenutzerId='"
				+ getLastCurator(barcode) + "'" + ", LetzterAusleiherId='" + getLastBorrower(barcode) + "'"
				+ ", LetztesAusleihDatum='" + getLastBorrowDate(barcode) + "'" + ", AusleihBenutzerId=''"
				+ ", AusleiherId=''" + ", AusleihDatum=''" + ", Inventarisiert=NOW()" + " where Barcode='" + barcode
				+ "'";
		try (Statement st = connect.createStatement()) {
			st.execute(sql);
		}
	}

	public void setCondition(String barcode, String condition) throws SQLException {
		String sql = "update " + Config.DATABASE_NAME + ".exemplar set " + "Zustand='" + condition + "'"
				+ " where Barcode='" + barcode + "'";
		try (Statement st = connect.createStatement()) {
			st.execute(sql);
		}
	}
}
