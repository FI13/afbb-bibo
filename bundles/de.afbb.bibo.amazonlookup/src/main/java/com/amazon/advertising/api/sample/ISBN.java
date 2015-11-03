package com.amazon.advertising.api.sample;

/*
 *  Copyright (C) 2009 Kilian Gaertner
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class ISBN {

	public static boolean checkISBN(String code) {
		code = code.replaceAll("-", "");
		// ISBN-10
		if (code.length() == 10) {
			int checkSum = 0;
			for (int i = 0; i < 10; ++i) {
				if (code.charAt(i) == 'X' || code.charAt(i) == 'x') {
					checkSum += 10 * (i + 1);
				} else {
					checkSum += (Integer.parseInt(code.substring(i, i + 1)) * (i + 1));
				}
			}
			if (checkSum % 11 == 0) {
				return true;
			}
		}
		// ISBN- 13
		else if (code.length() == 13) {
			int checkSum = 0;
			int digit = 0;
			for (int i = 0; i < 13; ++i) {
				if (code.charAt(i) == 'X' || code.charAt(i) == 'x') {
					digit = 10;
				} else {
					digit = Integer.parseInt(code.substring(i, i + 1));
				}
				if (i % 2 == 1) {
					digit *= 3;
				}
				checkSum += digit;
			}
			if (checkSum % 10 == 0) {
				return true;
			}
		}
		return false;
	}
}