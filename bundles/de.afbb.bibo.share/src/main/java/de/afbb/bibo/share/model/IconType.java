package de.afbb.bibo.share.model;

/**
 * types of icons
 */
public enum IconType {
	USER_MANAGE("USER_MANAGE"), BOOK_GROUP("BOOK_GROUP"), BOOK_DAMAGED("BOOK_DAMAGED"), BOOK_LENT("BOOK_LENT"), BOOK_AVAILABLE(
			"BOOK_AVAILABLE"), BOOK("BOOK"), CD("CD"), LOGIN("LOGIN"), PUPIL("PUPIL"), TEACHER("TEACHER"), USER("USER"), ARROW_UP(
					"ARROW_UP"), ARROW_DOWN("ARROW_DOWN"), PLUS("PLUS"), MINUS("MINUS"), SAVE("SAVE"), LOGO("LOGO"), MEDIA("MEDIA"), HELP(
							"HELP"), USER_MANAGE_ADD("USER_MANAGE_ADD"), BOOK_ADD("BOOK_ADD"), BOOK_GROUP_ADD("BOOK_GROUP_ADD"), CD_ADD(
									"CD_ADD"), TEACHER_ADD("TEACHER_ADD"), PUPIL_ADD("PUPIL_ADD");

	private String code;

	IconType(final String code) {
		this.code = code;
	}

	/**
	 * get the string representation for this enum
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * returns an instance for this enum given the correct code for it
	 * 
	 * @param code
	 *            representation for this enum
	 * @return null if code is invalid, enum otherwise
	 */
	public static IconType fromString(final String code) {
		if (code != null) {
			for (final IconType b : IconType.values()) {
				if (code.equalsIgnoreCase(b.code)) {
					return b;
				}
			}
		}
		return null;
	}
}
