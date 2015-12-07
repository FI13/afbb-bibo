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
import de.afbb.bibo.share.model.IconType;
import de.afbb.bibo.share.model.Medium;
import de.afbb.bibo.share.model.MediumType;

/**
 * @author fi13.pendrulat
 * @author David Becker
 */
public class DBConnector {

	private static final String NEW = "Neu";
	private static DBConnector INSTANCE;
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(DBConnector.class);

	private Connection connect = null;

	public static DBConnector getInstance() throws SQLException, NumberFormatException, IOException {
		log.debug("access database...");
		if (INSTANCE == null) {
			INSTANCE = new DBConnector();
		}
		return INSTANCE;
	}

	private DBConnector() throws SQLException, NumberFormatException, IOException {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
		} catch (final ClassNotFoundException ex) {
			Logger.getLogger(DBConnector.class.getName()).log(Level.SEVERE, null, ex);
		}
		log.info("connect to database " + Config.getInstance().getMYSQL_URL());
		// Setup the connection with the DB
		connect = DriverManager.getConnection("jdbc:mysql://" + Config.getInstance().getMYSQL_URL() + ":"
				+ Config.getInstance().getMYSQL_PORT() + "/?" + "user=" + Config.getInstance().getUSER_NAME()
				+ "&password=" + Config.getInstance().getPASSWORD());
	}

	public int getCuratorId(final String curatorName) throws SQLException, NumberFormatException, IOException {
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery("select Id from "
					+ Config.getInstance().getDATABASE_NAME() + ".benutzer where " + "Name='" + curatorName + "'")) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	public Curator getCurator(final String curatorName) throws SQLException, NumberFormatException, IOException {
		log.debug("get curator with name: " + curatorName);
		try (Statement statement = connect.createStatement()) {
			try (ResultSet mediaSet = statement.executeQuery("select Id, Name, Salt, Hash from "
					+ Config.getInstance().getDATABASE_NAME() + ".benutzer where Name='" + curatorName + "'")) {
				return mediaSet.first() ? new Curator(mediaSet.getInt(1), mediaSet.getString(2), mediaSet.getString(3),
						mediaSet.getString(4)) : null;
			}
		}
	}

	public Curator getCurator(final Integer id) throws SQLException, NumberFormatException, IOException {
		log.debug("get curator with id: " + id);
		try (Statement statement = connect.createStatement()) {
			try (ResultSet mediaSet = statement.executeQuery("select Id, Name, Salt, Hash from "
					+ Config.getInstance().getDATABASE_NAME() + ".benutzer where Id='" + id + "'")) {
				return mediaSet.first() ? new Curator(mediaSet.getInt(1), mediaSet.getString(2), mediaSet.getString(3),
						mediaSet.getString(4)) : null;
			}
		}
	}

	public int createCurator(final Curator user) throws SQLException, NumberFormatException, IOException {
		log.debug("add curator: " + user);
		try (Statement statement = connect.createStatement()) {
			statement.execute(
					"insert into " + Config.getInstance().getDATABASE_NAME() + ".benutzer (Name, Hash, Salt) values ('"
							+ user.getName() + "', '" + user.getPasswordHash() + "', '" + user.getSalt() + "')");
			return getCuratorId(user.getName());
		}
	}

	public void updateCurator(final Curator user) throws SQLException, NumberFormatException, IOException {
		log.debug("update curator: " + user);
		try (Statement statement = connect.createStatement()) {
			statement.execute("update " + Config.getInstance().getDATABASE_NAME() + ".benutzer set " + "Name='"
					+ user.getName() + "', Hash='" + user.getPasswordHash() + "', Salt='" + user.getSalt()
					+ "' where Id=" + user.getId());
		}
	}

	public boolean checkPassword(final String name, final String passwordHash)
			throws SQLException, NumberFormatException, IOException {
		log.debug("check Password: name: " + name + ", hash: " + passwordHash);
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery("select Hash from "
					+ Config.getInstance().getDATABASE_NAME() + ".benutzer where " + "Name='" + name + "'")) {
				return resultSet.first() && resultSet.getString(1).equals(passwordHash);
			}
		}
	}

	public String requestSalt(final String name) throws SQLException, NumberFormatException, IOException {
		log.debug("request salt for user: " + name);
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery("select Salt from "
					+ Config.getInstance().getDATABASE_NAME() + ".benutzer where " + "Name='" + name + "'")) {
				return resultSet.first() ? resultSet.getString(1) : null;
			}
		}
	}

	public List<Curator> getCurator() throws SQLException, NumberFormatException, IOException {
		final List<Curator> result = new ArrayList<Curator>();
		log.debug("list all users");
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(
					"select Id, Name, Salt from " + Config.getInstance().getDATABASE_NAME() + ".benutzer")) {
				while (resultSet.next()) {
					result.add(new Curator(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), null));
				}
			}
		}
		return result;
	}

	public void deleteCurator(final String id) throws SQLException, NumberFormatException, IOException {
		log.debug("delete user with id: " + id);
		try (Statement statement = connect.createStatement()) {
			statement.execute(
					"delete from " + Config.getInstance().getDATABASE_NAME() + ".benutzer where Id=" + id + "");
		}
	}

	public int createMediumType(final String name, final String iconPath) throws SQLException, IOException {
		final String sql = "insert into " + Config.getInstance().getDATABASE_NAME()
				+ ".typ (TypName, Icon) values (?, ?)";
		log.debug("create medium type: {name: " + name + ", iconPath: " + iconPath + "}");
		try (PreparedStatement statement = connect.prepareStatement(sql)) {
			statement.setString(1, name);
			statement.setString(2, iconPath);
			statement.execute();
			try (ResultSet resultSet = statement
					.executeQuery("select Id from " + Config.getInstance().getDATABASE_NAME() + ".typ where "
							+ "TypName='" + name + "' and Icon='" + iconPath + "'")) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	public List<MediumType> getMediumTypes() throws SQLException, NumberFormatException, IOException {
		final List<MediumType> result = new ArrayList<MediumType>();
		log.debug("list all medium types");
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery(
					"select Id, TypName, Icon from " + Config.getInstance().getDATABASE_NAME() + ".typ")) {
				while (resultSet.next()) {
					result.add(new MediumType(resultSet.getInt(1), resultSet.getString(2),
							IconType.fromString(resultSet.getString(3))));
				}
			}
		}
		log.debug("fetched from database: " + result);
		return result;
	}

	public MediumType getMediumType(final String id) throws SQLException, NumberFormatException, IOException {
		log.debug("get medium type with id : " + id);
		try (Statement statement = connect.createStatement()) {
			try (ResultSet mediaSet = statement.executeQuery("select Id, TypName, Icon from "
					+ Config.getInstance().getDATABASE_NAME() + ".typ where Id='" + id + "'")) {
				return mediaSet.first() ? new MediumType(mediaSet.getInt(1), mediaSet.getString(2),
						IconType.fromString(mediaSet.getString(3))) : null;
			}
		}
	}

	public void deleteMediumType(final String id) throws SQLException, NumberFormatException, IOException {
		log.debug("delete mediumtype with id: " + id);
		try (Statement statement = connect.createStatement()) {
			statement.execute("delete from " + Config.getInstance().getDATABASE_NAME() + ".typ where Id='" + id + "'");
		}
	}

	public int createMedium(final String isbn, final String title, final String author, final String language,
			final int typeId) throws SQLException, NumberFormatException, IOException {
		final String sql = "insert into " + Config.getInstance().getDATABASE_NAME()
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
			try (ResultSet resultSet = statement.executeQuery("select Id from "
					+ Config.getInstance().getDATABASE_NAME() + ".medium where " + "ISBN='" + isbn + "'")) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	public Copy getCopy(final String barcode) throws SQLException, NumberFormatException, IOException {
		log.debug("get copy with barcode: " + barcode);
		try (final Statement statement = connect.createStatement()) {
			try (final ResultSet mediaSet = statement.executeQuery(
					"select e.Id, e.Edition, e.Barcode, e.Inventarisiert, e.Zustand, e.AusleihDatum, e.LetztesAusleihDatum, e.AusleiherId, e.LetzterAusleiherId, e.AusleihBenutzerId, e.LetzterAusleihBenutzerId, e.GruppenId, m.Id, m.ISBN, m.Titel, m.Autor, m.Sprache, m.TypId, m.Herausgeber from "
							+ Config.getInstance().getDATABASE_NAME() + ".exemplar e, "
							+ Config.getInstance().getDATABASE_NAME() + ".medium m where Barcode='" + barcode
							+ "' and e.MedienId=m.Id")) {
				return mediaSet.first()
						? new Copy(mediaSet.getInt(1), mediaSet.getString(2), mediaSet.getString(3),
								mediaSet.getDate(4), mediaSet.getString(5), mediaSet.getDate(6), mediaSet.getDate(7),
								mediaSet.getInt(12), new Borrower(mediaSet.getInt(8)), new Borrower(mediaSet.getInt(9)),
								new Curator(mediaSet.getInt(10)), new Curator(mediaSet.getInt(11)), mediaSet.getInt(13),
								mediaSet.getString(14), mediaSet.getString(15), mediaSet.getString(16),
								mediaSet.getString(17), new MediumType(mediaSet.getInt(18)), mediaSet.getString(19))
						: null;
			}
		}
	}

	public Medium getMedium(final String isbn) throws SQLException, NumberFormatException, IOException {
		log.debug("get medium with isbn: " + isbn);
		try (Statement statement = connect.createStatement()) {
			try (ResultSet mediaSet = statement
					.executeQuery("select TypId, Id, ISBN, Titel, Autor, Sprache, Herausgeber from "
							+ Config.getInstance().getDATABASE_NAME() + ".medium where ISBN='" + isbn + "'")) {
				return mediaSet.first() ? new Medium(mediaSet.getInt(2), mediaSet.getString(3), mediaSet.getString(4),
						mediaSet.getString(5), mediaSet.getString(6), new MediumType(mediaSet.getInt(1)),
						mediaSet.getString(7)) : null;
			}
		}
	}

	public List<Medium> getMedium() throws SQLException, NumberFormatException, IOException {
		final List<Medium> result = new ArrayList<Medium>();
		log.debug("get all media");
		try (Statement statement = connect.createStatement()) {
			try (ResultSet mediaSet = statement
					.executeQuery("select TypId, Id, ISBN, Titel, Autor, Sprache, Herausgeber from "
							+ Config.getInstance().getDATABASE_NAME() + ".medium")) {
				while (mediaSet.next()) {
					result.add(new Medium(mediaSet.getInt(2), mediaSet.getString(3), mediaSet.getString(4),
							mediaSet.getString(5), mediaSet.getString(6), new MediumType(mediaSet.getInt(1)),
							mediaSet.getString(7)));
				}
			}
		}
		log.debug("fetched from database: " + result);
		return result;
	}

	public void deleteMedium(final String id) throws SQLException, NumberFormatException, IOException {
		log.debug("delete medium with id: " + id);
		try (Statement statement = connect.createStatement()) {
			statement.execute(
					"delete from " + Config.getInstance().getDATABASE_NAME() + ".medium where Id='" + id + "'");
		}
	}

	private int createGroup() throws SQLException, NumberFormatException, IOException {
		try (Statement st = connect.createStatement()) {
			st.execute("insert into " + Config.getInstance().getDATABASE_NAME() + ".gruppe () values ()");
			try (ResultSet resultSet = st.executeQuery("SELECT LAST_INSERT_ID()")) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	/**
	 * Create a number of copies in the "exemplar" table. The copies remain in
	 * the same group. The input are two arrays. The first input of editions is
	 * the same copy as the first input in Barcodes and so on.
	 *
	 * @param copies
	 * @return the groupId
	 * @throws SQLException
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public int createCopyGroup(final Copy[] copies) throws SQLException, NumberFormatException, IOException {
		final int groupId = createGroup();
		log.debug("create new group of copies: " + Arrays.toString(copies));
		log.debug("created new group for media. groupId: " + groupId);
		final String sql = "insert into " + Config.getInstance().getDATABASE_NAME()
				+ ".exemplar (Edition, Barcode, Inventarisiert, Zustand, GruppenId, MedienId) values (?, ?, NOW(), ?, ?, ?)";
		try (PreparedStatement statement = connect.prepareStatement(sql)) {
			int i = 0;
			for (final Copy copy : copies) {
				statement.setString(1, copy.getEdition());
				statement.setString(2, copy.getBarcode());
				statement.setString(3, NEW);
				statement.setInt(4, groupId);
				statement.setInt(5, copy.getMedium().getMediumId());
				statement.addBatch();
				i++;
				// Execute every 100 items.
				if (i % 100 == 0 || i == copies.length) {
					statement.executeBatch();
				}
			}
		}
		return groupId;
	}

	public void createCopies(final Copy[] copies) throws SQLException, NumberFormatException, IOException {
		log.debug("create new copies: " + Arrays.toString(copies));
		final String sql = "insert into " + Config.getInstance().getDATABASE_NAME()
				+ ".exemplar (Edition, Barcode, Inventarisiert, Zustand, MedienId) values (?, ?, NOW(), ?, ?)";
		try (final PreparedStatement statement = connect.prepareStatement(sql)) {
			int i = 0;
			for (final Copy copy : copies) {
				statement.setString(1, copy.getEdition());
				statement.setString(2, copy.getBarcode());
				statement.setString(3, NEW);
				statement.setInt(4, copy.getMedium().getMediumId());
				statement.addBatch();
				i++;
				// Execute every 1000 items.
				if (i % 1000 == 0 || i == copies.length) {
					statement.executeBatch();
				}
			}
		}
	}

	public Collection<List<Copy>> getCopies() throws SQLException, NumberFormatException, IOException {
		final Map<Integer, List<Copy>> result = new HashMap<Integer, List<Copy>>();
		log.debug("get all copies");
		try (Statement statement = connect.createStatement()) {
			try (ResultSet copySet = statement.executeQuery("select Id, Edition, Barcode, Inventarisiert, Zustand, "
					+ "AusleihDatum, LetztesAusleihDatum, AusleihBenutzerId, LetzterAusleihBenutzerId, AusleiherId, "
					+ "LetzerAusleiherId, MedienId, GruppenId from " + Config.getInstance().getDATABASE_NAME()
					+ ".exemplar")) {
				while (copySet.next()) {
					final int mediaId = copySet.getInt(12);
					final int groupId = copySet.getInt(13);
					try (ResultSet mediaSet = statement
							.executeQuery("select TypId, ISBN, Titel, Autor, Sprache, Herausgeber from "
									+ Config.getInstance().getDATABASE_NAME() + ".medium WHERE Id='" + mediaId + "'")) {
						mediaSet.first();
						final int copyId = copySet.getInt(1);
						final String edition = copySet.getString(2);
						final String barcode = copySet.getString(3);
						final Date inventorised = copySet.getDate(4);
						final String condition = copySet.getString(5);
						final Date borrowDate = copySet.getDate(6);
						final Date lastBorrowDate = copySet.getDate(7);
						final int curatorId = copySet.getInt(8);
						final int lastCuratorId = copySet.getInt(9);
						final int borrowerId = copySet.getInt(10);
						final int lastBorrowerId = copySet.getInt(11);
						final int typeId = mediaSet.getInt(1);
						final String isbn = mediaSet.getString(2);
						final String title = mediaSet.getString(3);
						final String author = mediaSet.getString(4);
						final String language = mediaSet.getString(5);
						final String publisher = mediaSet.getString(6);
						final Copy copy = new Copy(copyId, edition, barcode, inventorised, condition, borrowDate,
								lastBorrowDate, groupId, new Borrower(borrowerId), new Borrower(lastBorrowerId),
								new Curator(curatorId), new Curator(lastCuratorId), mediaId, isbn, title, author,
								language, new MediumType(typeId), publisher);
						if (!result.containsKey(groupId)) {
							result.put(groupId, new ArrayList<Copy>());
						}
						result.get(groupId).add(copy);
					}
				}
			}
		}

		return result.values();
	}

	public int createBorrower(final Borrower borrower) throws SQLException, NumberFormatException, IOException {
		log.debug("create new borrower: " + borrower);
		final String sql = "insert into " + Config.getInstance().getDATABASE_NAME()
				+ ".ausleiher (Vorname, Nachname, Info, EMail, Telefon) values (?, ?, ?, ?, ?)";
		try (PreparedStatement statement = connect.prepareStatement(sql)) {
			statement.setString(1, borrower.getForename());
			statement.setString(2, borrower.getSurname());
			statement.setString(3, borrower.getInfo());
			statement.setString(4, borrower.getEmail());
			statement.setString(5, borrower.getPhoneNumber());
			statement.execute();
			try (ResultSet resultSet = statement.executeQuery(
					"select Id from " + Config.getInstance().getDATABASE_NAME() + ".ausleiher where " + "Vorname='"
							+ borrower.getForename() + "' and Nachname='" + borrower.getSurname() + "'")) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	public int getBorrowerId(final String fName, final String sName)
			throws SQLException, NumberFormatException, IOException {
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement
					.executeQuery("select Id from " + Config.getInstance().getDATABASE_NAME() + ".ausleiher where "
							+ "Vorname='" + fName + "' ans Nachname='" + sName + "'")) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	public Borrower getBorrower(final Integer id) throws SQLException, NumberFormatException, IOException {
		log.debug("get curator with name: " + id);
		try (Statement statement = connect.createStatement()) {
			try (ResultSet mediaSet = statement.executeQuery("select Id, Vorname, Nachname, Info, EMail, Telefon from "
					+ Config.getInstance().getDATABASE_NAME() + ".ausleiher where Id=" + id)) {
				return mediaSet.first() ? new Borrower(mediaSet.getInt(1), mediaSet.getString(2), mediaSet.getString(3),
						mediaSet.getString(4), mediaSet.getString(5), mediaSet.getString(6)) : null;
			}
		}
	}

	public List<Borrower> getBorrowers() throws SQLException, NumberFormatException, IOException {
		final List<Borrower> result = new ArrayList<>();
		log.debug("get all borrower");
		try (Statement statement = connect.createStatement()) {
			try (ResultSet resultSet = statement.executeQuery("select Id, Vorname, Nachname, Info, EMail, Telefon from "
					+ Config.getInstance().getDATABASE_NAME() + ".ausleiher")) {
				while (resultSet.next()) {
					result.add(new Borrower(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
							resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)));
				}
			}
			return result;
		}
	}

	public void deleteBorrower(final int id) throws SQLException, NumberFormatException, IOException {
		log.debug("delete borrower with id: " + id);
		try (Statement statement = connect.createStatement()) {
			statement.execute(
					"delete from " + Config.getInstance().getDATABASE_NAME() + ".ausleiher where Id=" + id + "");
		}
	}

	public void updateBorrower(final Borrower b) throws SQLException, NumberFormatException, IOException {
		log.debug("update borrower: " + b);
		try (Statement statement = connect.createStatement()) {
			statement.execute("update " + Config.getInstance().getDATABASE_NAME() + ".ausleiher set " + "Vorname='"
					+ b.getForename() + "', Nachname='" + b.getSurname() + "', Info='" + b.getInfo() + "', EMail='"
					+ b.getEmail() + "', Telefon='" + b.getPhoneNumber() + "' where Id=" + b.getId());
		}
	}

	private int getLastBorrower(final String barcode) throws SQLException, NumberFormatException, IOException {
		try (Statement st = connect.createStatement()) {
			try (ResultSet resultSet = st.executeQuery("select AusleiherId from "
					+ Config.getInstance().getDATABASE_NAME() + ".exemplar where barcode='" + barcode + "';")) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	private int getLastCurator(final String barcode) throws SQLException, NumberFormatException, IOException {
		try (Statement st = connect.createStatement()) {
			try (ResultSet resultSet = st.executeQuery("select AusleihBenutzerId from "
					+ Config.getInstance().getDATABASE_NAME() + ".exemplar where barcode='" + barcode + "';")) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	private String getLastBorrowDate(final String barcode) throws SQLException, NumberFormatException, IOException {
		try (Statement st = connect.createStatement()) {
			try (ResultSet resultSet = st.executeQuery("select AusleihDatum from "
					+ Config.getInstance().getDATABASE_NAME() + ".exemplar where barcode='" + barcode + "';")) {
				return resultSet.first() ? resultSet.getString(1) : null;
			}
		}
	}

	public void setNewBorrower(final String barcode, final int borrowerId, final int curatorId)
			throws SQLException, NumberFormatException, IOException {
		final String sql = "update " + Config.getInstance().getDATABASE_NAME() + ".exemplar set "
				+ " AusleihBenutzerId='" + curatorId + "'" + ", AusleiherId='" + borrowerId + "'"
				+ ", AusleihDatum=NOW()" + " where Barcode='" + barcode + "'";
		log.debug(sql);
		try (Statement st = connect.createStatement()) {
			st.execute(sql);
		}
	}

	public void returnBook(final String barcode, final int curatorId)
			throws SQLException, NumberFormatException, IOException {
		final String sql = "update " + Config.getInstance().getDATABASE_NAME() + ".exemplar set "
				+ "LetzterAusleihBenutzerId='" + curatorId + "'" + ", LetzterAusleiherId='" + getLastBorrower(barcode)
				+ "'" + ", LetztesAusleihDatum=NOW() where Barcode='" + barcode + "'";
		try (Statement st = connect.createStatement()) {
			st.execute(sql);
		}
	}

	public void setCondition(final String barcode, final String condition)
			throws SQLException, NumberFormatException, IOException {
		final String sql = "update " + Config.getInstance().getDATABASE_NAME() + ".exemplar set " + "Zustand='"
				+ condition + "'" + " where Barcode='" + barcode + "'";
		try (Statement st = connect.createStatement()) {
			st.execute(sql);
		}
	}

	public boolean existsCopy(final String barcode) throws SQLException, NumberFormatException, IOException {
		try (Statement st = connect.createStatement()) {
			try (ResultSet resultSet = st.executeQuery("select count(Id) from "
					+ Config.getInstance().getDATABASE_NAME() + ".exemplar where Barcode='" + barcode + "';")) {
				return resultSet.first() && 0 < resultSet.getInt(1);
			}
		}
	}

	public int countLendCopiesByBorrower(final Integer borrowerId)
			throws NumberFormatException, SQLException, IOException {
		log.debug("count lend copies for borrower with id: " + borrowerId);
		try (Statement st = connect.createStatement()) {
			try (ResultSet resultSet = st
					.executeQuery("select count(Id) from " + Config.getInstance().getDATABASE_NAME()
							+ ".exemplar where (TIMESTAMPDIFF(SECOND,  LetztesAusleihDatum, AusleihDatum) >= 0 or (AusleihDatum is not null and LetztesAusleihDatum is null)) and AusleiherId="
							+ borrowerId)) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	public int countLendCopiesByMedium(final Integer mediumId) throws NumberFormatException, SQLException, IOException {
		log.debug("count lend copies for medium with id: " + mediumId);
		try (Statement st = connect.createStatement()) {
			try (ResultSet resultSet = st
					.executeQuery("select count(Id) from " + Config.getInstance().getDATABASE_NAME()
							+ ".exemplar where (TIMESTAMPDIFF(SECOND,  LetztesAusleihDatum, AusleihDatum) >= 0 or (AusleihDatum is not null and LetztesAusleihDatum is null)) and MedienId="
							+ mediumId)) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	public int countCopiesByMedium(final Integer mediumId) throws NumberFormatException, SQLException, IOException {
		log.debug("count copies for medium with id: " + mediumId);
		try (Statement st = connect.createStatement()) {
			try (ResultSet resultSet = st.executeQuery("select count(Id) from "
					+ Config.getInstance().getDATABASE_NAME() + ".exemplar where MedienId=" + mediumId)) {
				return resultSet.first() ? resultSet.getInt(1) : -1;
			}
		}
	}

	public List<Copy> listCopies(final Integer mediumId) throws NumberFormatException, SQLException, IOException {
		final List<Copy> result = new ArrayList<Copy>();
		log.debug("get all copies medium id: " + mediumId);
		try (Statement statement = connect.createStatement()) {
			final String string = "select e.Id, e.Edition, e.Barcode, e.Inventarisiert, e.Zustand, e.AusleihDatum, e.LetztesAusleihDatum, e.AusleiherId, e.LetzterAusleiherId, e.AusleihBenutzerId, e.LetzterAusleihBenutzerId, e.GruppenId, m.Id, m.ISBN, m.Titel, m.Autor, m.Sprache, m.TypId, m.Herausgeber from "
					+ Config.getInstance().getDATABASE_NAME() + ".exemplar e inner join "
					+ Config.getInstance().getDATABASE_NAME() + ".medium m on e.MedienId=m.Id where e.MedienId="
					+ mediumId;
			try (ResultSet mediaSet = statement.executeQuery(string)) {
				while (mediaSet.next()) {
					result.add(new Copy(mediaSet.getInt(1), mediaSet.getString(2), mediaSet.getString(3),
							mediaSet.getDate(4), mediaSet.getString(5), mediaSet.getDate(6), mediaSet.getDate(7),
							mediaSet.getInt(12), new Borrower(mediaSet.getInt(8)), new Borrower(mediaSet.getInt(9)),
							new Curator(mediaSet.getInt(10)), new Curator(mediaSet.getInt(11)), mediaSet.getInt(13),
							mediaSet.getString(14), mediaSet.getString(15), mediaSet.getString(16),
							mediaSet.getString(17), new MediumType(mediaSet.getInt(18)), mediaSet.getString(19)));
				}
			}
		}
		return result;
	}

	public List<Copy> listLendCopies(final Integer borrowerId) throws NumberFormatException, SQLException, IOException {
		final List<Copy> result = new ArrayList<Copy>();
		log.debug("get lend copies for borrower with id: " + borrowerId);
		try (Statement statement = connect.createStatement()) {
			final String string = "select e.Id, e.Edition, e.Barcode, e.Inventarisiert, e.Zustand, e.AusleihDatum, e.LetztesAusleihDatum, e.AusleiherId, e.LetzterAusleiherId, e.AusleihBenutzerId, e.LetzterAusleihBenutzerId, e.GruppenId, m.Id, m.ISBN, m.Titel, m.Autor, m.Sprache, m.TypId, m.Herausgeber from "
					+ Config.getInstance().getDATABASE_NAME() + ".exemplar e, "
					+ Config.getInstance().getDATABASE_NAME()
					+ ".medium m where (TIMESTAMPDIFF(SECOND,  LetztesAusleihDatum, AusleihDatum) >= 0 or (e.AusleihDatum is not null and e.LetztesAusleihDatum is null)) and AusleiherId="
					+ borrowerId + " and e.MedienId=m.Id";
			try (ResultSet mediaSet = statement.executeQuery(string)) {
				while (mediaSet.next()) {
					result.add(new Copy(mediaSet.getInt(1), mediaSet.getString(2), mediaSet.getString(3),
							mediaSet.getDate(4), mediaSet.getString(5), mediaSet.getDate(6), mediaSet.getDate(7),
							mediaSet.getInt(12), new Borrower(mediaSet.getInt(8)), new Borrower(mediaSet.getInt(9)),
							new Curator(mediaSet.getInt(10)), new Curator(mediaSet.getInt(11)), mediaSet.getInt(13),
							mediaSet.getString(14), mediaSet.getString(15), mediaSet.getString(16),
							mediaSet.getString(17), new MediumType(mediaSet.getInt(18)), mediaSet.getString(19)));
				}
			}
		}
		return result;
	}

	public void doInventory(final String barcode, final String condition)
			throws SQLException, NumberFormatException, IOException {
		final String sql = "update " + Config.getInstance().getDATABASE_NAME() + ".exemplar set " + "Zustand='"
				+ condition + "', Inventarisiert=NOW()" + " where Barcode='" + barcode + "'";
		try (Statement st = connect.createStatement()) {
			st.execute(sql);
		}
	}
}
