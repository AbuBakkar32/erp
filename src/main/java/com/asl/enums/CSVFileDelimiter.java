package com.asl.enums;

/**
 * @author Zubayer Ahamed
 *
 */
public enum CSVFileDelimiter {

	COMMA(0, "Comma", ','),
	PIPE(1, "Pipe", '|'),
	SEMI_COLON(2, "Semi-Colon", ':'),
	TILDE(3, "Tilde", '~');

	private int delimeterIndex;
	private String code;
	private char character;

	private CSVFileDelimiter(int delimeterIndex, String code, char character) {
		this.delimeterIndex = delimeterIndex;
		this.code = code;
		this.character = character;
	}

	public int getDelimeterIndex() {
		return this.delimeterIndex;
	}

	public String getCode() {
		return this.code;
	}

	public char getCharacter() {
		return this.character;
	}
}
