package michael.bigcalendar;

import java.util.Calendar;

class Lunar {
	private int gregorianYear;
	private int gregorianMonth;
	private int gregorianDate;
	private boolean isGregorianLeap;
	private int dayOfYear;
	private int dayOfWeek; // 周日一星期的第一天
	private int lunarYear;
	private int lunarMonth; // 负数表示闰月
	private int lunarDate;
	private int sectionalTerm;
	private int principleTerm;
	private static char[] daysInGregorianMonth = { 31, 28, 31, 30, 31, 30, 31,
			31, 30, 31, 30, 31 };
	private static String[] stemNames = { "甲", "乙", "丙", "丁", "戊", "己", "庚",
			"辛", "壬", "癸" };
	private static String[] branchNames = { "子", "丑", "寅", "卯", "辰", "巳", "午",
			"未", "申", "酉", "戌", "亥" };
	private static String[] animalNames = { "鼠", "牛", "虎", "兔", "龙", "蛇", "马",
			"羊", "猴", "鸡", "狗", "猪" };

	public Lunar(int year, int month, int day) {
		gregorianYear = year;
		gregorianMonth = month;
		gregorianDate = day;
		isGregorianLeap = isGregorianLeapYear(year);
		dayOfYear = dayOfYear(year, month, day);
		dayOfWeek = dayOfWeek(year, month, day);
		lunarYear = 0;
		lunarMonth = 0;
		lunarDate = 0;
		sectionalTerm = 0;
		principleTerm = 0;
		computeLunarFields();
		computeSolarTerms();
	}

	public Lunar(Calendar calendar) {
		this(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
	}

	public Lunar() {
		this(Calendar.getInstance());
	}

	public static boolean isGregorianLeapYear(int year) {
		boolean isLeap = false;
		if (year % 4 == 0)
			isLeap = true;
		if (year % 100 == 0)
			isLeap = false;
		if (year % 400 == 0)
			isLeap = true;
		return isLeap;
	}

	public static int daysInGregorianMonth(int y, int m) {
		int d = daysInGregorianMonth[m - 1];
		if (m == 2 && isGregorianLeapYear(y))
			d++; // 公历闰年二月多一天
		return d;
	}

	public static int dayOfYear(int y, int m, int d) {
		int c = 0;
		for (int i = 1; i < m; i++) {
			c = c + daysInGregorianMonth(y, i);
		}
		c = c + d;
		return c;
	}

	public static int dayOfWeek(int y, int m, int d) {
		int w = 1; // 公历一年一月一日是星期一，所以起始值为星期日
		y = (y - 1) % 400 + 1; // 公历星期值分部 400 年循环一次
		int ly = (y - 1) / 4; // 闰年次数
		ly = ly - (y - 1) / 100;
		ly = ly + (y - 1) / 400;
		int ry = y - 1 - ly; // 常年次数
		w = w + ry; // 常年星期值增一
		w = w + 2 * ly; // 闰年星期值增二
		w = w + dayOfYear(y, m, d);
		w = (w - 1) % 7 + 1;
		return w;
	}

	private static char[] lunarMonths = {
			// 农历月份大小压缩表，两个字节表示一年。两个字节共十六个二进制位数，
			// 前四个位数表示闰月月份，后十二个位数表示十二个农历月份的大小。
			0x00, 0x04, 0xad, 0x08, 0x5a, 0x01, 0xd5, 0x54, 0xb4, 0x09, 0x64,
			0x05, 0x59, 0x45, 0x95, 0x0a, 0xa6, 0x04, 0x55, 0x24, 0xad, 0x08,
			0x5a, 0x62, 0xda, 0x04, 0xb4, 0x05, 0xb4, 0x55, 0x52, 0x0d, 0x94,
			0x0a, 0x4a, 0x2a, 0x56, 0x02, 0x6d, 0x71, 0x6d, 0x01, 0xda, 0x02,
			0xd2, 0x52, 0xa9, 0x05, 0x49, 0x0d, 0x2a, 0x45, 0x2b, 0x09, 0x56,
			0x01, 0xb5, 0x20, 0x6d, 0x01, 0x59, 0x69, 0xd4, 0x0a, 0xa8, 0x05,
			0xa9, 0x56, 0xa5, 0x04, 0x2b, 0x09, 0x9e, 0x38, 0xb6, 0x08, 0xec,
			0x74, 0x6c, 0x05, 0xd4, 0x0a, 0xe4, 0x6a, 0x52, 0x05, 0x95, 0x0a,
			0x5a, 0x42, 0x5b, 0x04, 0xb6, 0x04, 0xb4, 0x22, 0x6a, 0x05, 0x52,
			0x75, 0xc9, 0x0a, 0x52, 0x05, 0x35, 0x55, 0x4d, 0x0a, 0x5a, 0x02,
			0x5d, 0x31, 0xb5, 0x02, 0x6a, 0x8a, 0x68, 0x05, 0xa9, 0x0a, 0x8a,
			0x6a, 0x2a, 0x05, 0x2d, 0x09, 0xaa, 0x48, 0x5a, 0x01, 0xb5, 0x09,
			0xb0, 0x39, 0x64, 0x05, 0x25, 0x75, 0x95, 0x0a, 0x96, 0x04, 0x4d,
			0x54, 0xad, 0x04, 0xda, 0x04, 0xd4, 0x44, 0xb4, 0x05, 0x54, 0x85,
			0x52, 0x0d, 0x92, 0x0a, 0x56, 0x6a, 0x56, 0x02, 0x6d, 0x02, 0x6a,
			0x41, 0xda, 0x02, 0xb2, 0xa1, 0xa9, 0x05, 0x49, 0x0d, 0x0a, 0x6d,
			0x2a, 0x09, 0x56, 0x01, 0xad, 0x50, 0x6d, 0x01, 0xd9, 0x02, 0xd1,
			0x3a, 0xa8, 0x05, 0x29, 0x85, 0xa5, 0x0c, 0x2a, 0x09, 0x96, 0x54,
			0xb6, 0x08, 0x6c, 0x09, 0x64, 0x45, 0xd4, 0x0a, 0xa4, 0x05, 0x51,
			0x25, 0x95, 0x0a, 0x2a, 0x72, 0x5b, 0x04, 0xb6, 0x04, 0xac, 0x52,
			0x6a, 0x05, 0xd2, 0x0a, 0xa2, 0x4a, 0x4a, 0x05, 0x55, 0x94, 0x2d,
			0x0a, 0x5a, 0x02, 0x75, 0x61, 0xb5, 0x02, 0x6a, 0x03, 0x61, 0x45,
			0xa9, 0x0a, 0x4a, 0x05, 0x25, 0x25, 0x2d, 0x09, 0x9a, 0x68, 0xda,
			0x08, 0xb4, 0x09, 0xa8, 0x59, 0x54, 0x03, 0xa5, 0x0a, 0x91, 0x3a,
			0x96, 0x04, 0xad, 0xb0, 0xad, 0x04, 0xda, 0x04, 0xf4, 0x62, 0xb4,
			0x05, 0x54, 0x0b, 0x44, 0x5d, 0x52, 0x0a, 0x95, 0x04, 0x55, 0x22,
			0x6d, 0x02, 0x5a, 0x71, 0xda, 0x02, 0xaa, 0x05, 0xb2, 0x55, 0x49,
			0x0b, 0x4a, 0x0a, 0x2d, 0x39, 0x36, 0x01, 0x6d, 0x80, 0x6d, 0x01,
			0xd9, 0x02, 0xe9, 0x6a, 0xa8, 0x05, 0x29, 0x0b, 0x9a, 0x4c, 0xaa,
			0x08, 0xb6, 0x08, 0xb4, 0x38, 0x6c, 0x09, 0x54, 0x75, 0xd4, 0x0a,
			0xa4, 0x05, 0x45, 0x55, 0x95, 0x0a, 0x9a, 0x04, 0x55, 0x44, 0xb5,
			0x04, 0x6a, 0x82, 0x6a, 0x05, 0xd2, 0x0a, 0x92, 0x6a, 0x4a, 0x05,
			0x55, 0x0a, 0x2a, 0x4a, 0x5a, 0x02, 0xb5, 0x02, 0xb2, 0x31, 0x69,
			0x03, 0x31, 0x73, 0xa9, 0x0a, 0x4a, 0x05, 0x2d, 0x55, 0x2d, 0x09,
			0x5a, 0x01, 0xd5, 0x48, 0xb4, 0x09, 0x68, 0x89, 0x54, 0x0b, 0xa4,
			0x0a, 0xa5, 0x6a, 0x95, 0x04, 0xad, 0x08, 0x6a, 0x44, 0xda, 0x04,
			0x74, 0x05, 0xb0, 0x25, 0x54, 0x03 };
	// 初始日，公历农历对应日期：
	// 公历 1901 年 1 月 1 日，对应农历 4598 年 11 月 11 日
	private static int baseYear = 1901;
	private static int baseMonth = 1;
	private static int baseDate = 1;
	private static int baseIndex = 0;
	private static int baseLunarYear = 4598 - 1;
	private static int baseLunarMonth = 11;
	private static int baseLunarDate = 11;

	public int computeLunarFields() {
		if (gregorianYear < 1901 || gregorianYear > 2100)
			return 1;
		int startYear = baseYear;
		int startMonth = baseMonth;
		int startDate = baseDate;
		lunarYear = baseLunarYear;
		lunarMonth = baseLunarMonth;
		lunarDate = baseLunarDate;
		// 第二个对应日，用以提高计算效率
		// 公历 2000 年 1 月 1 日，对应农历 4697 年 11 月 25 日
		if (gregorianYear >= 2000) {
			startYear = baseYear + 99;
			startMonth = 1;
			startDate = 1;
			lunarYear = baseLunarYear + 99;
			lunarMonth = 11;
			lunarDate = 25;
		}
		int daysDiff = 0;
		for (int i = startYear; i < gregorianYear; i++) {
			daysDiff += 365;
			if (isGregorianLeapYear(i))
				daysDiff += 1; // leap year
		}
		for (int i = startMonth; i < gregorianMonth; i++) {
			daysDiff += daysInGregorianMonth(gregorianYear, i);
		}
		daysDiff += gregorianDate - startDate;

		lunarDate += daysDiff;
		int lastDate = daysInLunarMonth(lunarYear, lunarMonth);
		int nextMonth = nextLunarMonth(lunarYear, lunarMonth);
		while (lunarDate > lastDate) {
			if (Math.abs(nextMonth) < Math.abs(lunarMonth))
				lunarYear++;
			lunarMonth = nextMonth;
			lunarDate -= lastDate;
			lastDate = daysInLunarMonth(lunarYear, lunarMonth);
			nextMonth = nextLunarMonth(lunarYear, lunarMonth);
		}
		return 0;
	}

	private static int[] bigLeapMonthYears = {
			// 大闰月的闰年年份
			6, 14, 19, 25, 33, 36, 38, 41, 44, 52, 55, 79, 117, 136, 147, 150,
			155, 158, 185, 193 };

	public static int daysInLunarMonth(int y, int m) {
		// 注意：闰月 m < 0
		int index = y - baseLunarYear + baseIndex;
		int v = 0;
		int l = 0;
		int d = 30;
		if (1 <= m && m <= 8) {
			v = lunarMonths[2 * index];
			l = m - 1;
			if (((v >> l) & 0x01) == 1)
				d = 29;
		} else if (9 <= m && m <= 12) {
			v = lunarMonths[2 * index + 1];
			l = m - 9;
			if (((v >> l) & 0x01) == 1)
				d = 29;
		} else {
			v = lunarMonths[2 * index + 1];
			v = (v >> 4) & 0x0F;
			if (v != Math.abs(m)) {
				d = 0;
			} else {
				d = 29;
				for (int i = 0; i < bigLeapMonthYears.length; i++) {
					if (bigLeapMonthYears[i] == index) {
						d = 30;
						break;
					}
				}
			}
		}
		return d;
	}

	public static int nextLunarMonth(int y, int m) {
		int n = Math.abs(m) + 1;
		if (m > 0) {
			int index = y - baseLunarYear + baseIndex;
			int v = lunarMonths[2 * index + 1];
			v = (v >> 4) & 0x0F;
			if (v == m)
				n = -m;
		}
		if (n == 13)
			n = 1;
		return n;
	}

	private static char[][] sectionalTermMap = {
			{ 7, 6, 6, 6, 6, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 5, 5,
					5, 5, 5, 4, 5, 5 },
			{ 5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 4, 4, 3,
					3, 4, 4, 3, 3, 3 },
			{ 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5,
					5, 5, 4, 5, 5, 5, 5 },
			{ 5, 5, 6, 6, 5, 5, 5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4,
					4, 5, 4, 4, 4, 4, 5 },
			{ 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5,
					5, 5, 4, 5, 5, 5, 5 },
			{ 6, 6, 7, 7, 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5,
					5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 5 },
			{ 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6,
					7, 7, 6, 6, 6, 7, 7 },
			{ 8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7,
					7, 7, 6, 7, 7, 7, 6, 6, 7, 7, 7 },
			{ 8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7,
					7, 7, 6, 7, 7, 7, 7 },
			{ 9, 9, 9, 9, 8, 9, 9, 9, 8, 8, 9, 9, 8, 8, 8, 9, 8, 8, 8, 8, 7, 8,
					8, 8, 7, 7, 8, 8, 8 },
			{ 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7,
					7, 7, 6, 6, 7, 7, 7 },
			{ 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6,
					7, 7, 6, 6, 6, 7, 7 } };
	private static char[][] sectionalTermYear = {
			{ 13, 49, 85, 117, 149, 185, 201, 250, 250 },
			{ 13, 45, 81, 117, 149, 185, 201, 250, 250 },
			{ 13, 48, 84, 112, 148, 184, 200, 201, 250 },
			{ 13, 45, 76, 108, 140, 172, 200, 201, 250 },
			{ 13, 44, 72, 104, 132, 168, 200, 201, 250 },
			{ 5, 33, 68, 96, 124, 152, 188, 200, 201 },
			{ 29, 57, 85, 120, 148, 176, 200, 201, 250 },
			{ 13, 48, 76, 104, 132, 168, 196, 200, 201 },
			{ 25, 60, 88, 120, 148, 184, 200, 201, 250 },
			{ 16, 44, 76, 108, 144, 172, 200, 201, 250 },
			{ 28, 60, 92, 124, 160, 192, 200, 201, 250 },
			{ 17, 53, 85, 124, 156, 188, 200, 201, 250 } };
	private static char[][] principleTermMap = {
			{ 21, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20,
					20, 20, 20, 20, 20, 19, 20, 20, 20, 19, 19, 20 },
			{ 20, 19, 19, 20, 20, 19, 19, 19, 19, 19, 19, 19, 19, 18, 19, 19,
					19, 18, 18, 19, 19, 18, 18, 18, 18, 18, 18, 18 },
			{ 21, 21, 21, 22, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21,
					20, 20, 20, 21, 20, 20, 20, 20, 19, 20, 20, 20, 20 },
			{ 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 20, 20, 20, 20,
					19, 20, 20, 20, 19, 19, 20, 20, 19, 19, 19, 20, 20 },
			{ 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21,
					20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 21 },
			{ 22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22,
					21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 21 },
			{ 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23, 22, 23, 23, 23,
					22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 23 },
			{ 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23,
					22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23 },
			{ 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23,
					22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23 },
			{ 24, 24, 24, 24, 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24,
					23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 23 },
			{ 23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23,
					22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 22 },
			{ 22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 21, 22, 22, 22,
					21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21, 22 } };
	private static char[][] principleTermYear = {
			{ 13, 45, 81, 113, 149, 185, 201 },
			{ 21, 57, 93, 125, 161, 193, 201 },
			{ 21, 56, 88, 120, 152, 188, 200, 201 },
			{ 21, 49, 81, 116, 144, 176, 200, 201 },
			{ 17, 49, 77, 112, 140, 168, 200, 201 },
			{ 28, 60, 88, 116, 148, 180, 200, 201 },
			{ 25, 53, 84, 112, 144, 172, 200, 201 },
			{ 29, 57, 89, 120, 148, 180, 200, 201 },
			{ 17, 45, 73, 108, 140, 168, 200, 201 },
			{ 28, 60, 92, 124, 160, 192, 200, 201 },
			{ 16, 44, 80, 112, 148, 180, 200, 201 },
			{ 17, 53, 88, 120, 156, 188, 200, 201 } };

	public int computeSolarTerms() {
		if (gregorianYear < 1901 || gregorianYear > 2100)
			return 1;
		sectionalTerm = sectionalTerm(gregorianYear, gregorianMonth);
		principleTerm = principleTerm(gregorianYear, gregorianMonth);
		return 0;
	}

	public static int sectionalTerm(int y, int m) {
		if (y < 1901 || y > 2100)
			return 0;
		int index = 0;
		int ry = y - baseYear + 1;
		while (ry >= sectionalTermYear[m - 1][index])
			index++;
		int term = sectionalTermMap[m - 1][4 * index + ry % 4];
		if ((ry == 121) && (m == 4))
			term = 5;
		if ((ry == 132) && (m == 4))
			term = 5;
		if ((ry == 194) && (m == 6))
			term = 6;
		return term;
	}

	public static int principleTerm(int y, int m) {
		if (y < 1901 || y > 2100)
			return 0;
		int index = 0;
		int ry = y - baseYear + 1;
		while (ry >= principleTermYear[m - 1][index])
			index++;
		int term = principleTermMap[m - 1][4 * index + ry % 4];
		if ((ry == 171) && (m == 3))
			term = 21;
		if ((ry == 181) && (m == 5))
			term = 21;
		return term;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Gregorian Year: " + gregorianYear + "\n");
		buf.append("Gregorian Month: " + gregorianMonth + "\n");
		buf.append("Gregorian Date: " + gregorianDate + "\n");
		buf.append("Is Leap Year: " + isGregorianLeap + "\n");
		buf.append("Day of Year: " + dayOfYear + "\n");
		buf.append("Day of Week: " + dayOfWeek + "\n");
		buf.append("Lunar Year: " + lunarYear + "\n");
		buf.append("Heavenly Stem: " + ((lunarYear - 1) % 10) + "\n");
		buf.append("Earthly Branch: " + ((lunarYear - 1) % 12) + "\n");
		buf.append("Lunar Month: " + lunarMonth + "\n");
		buf.append("Lunar Date: " + lunarDate + "\n");
		buf.append("Sectional Term: " + sectionalTerm + "\n");
		buf.append("Principle Term: " + principleTerm + "\n");
		return buf.toString();
	}

	private static String[] lunarMonthNames = { "正", "二", "三", "四", "五", "六",
			"七", "八", "九", "十", "冬", "腊" };
	private static String[] principleTermNames = { "大寒", "雨水", "春分", "谷雨",
			"夏满", "夏至", "大暑", "处暑", "秋分", "霜降", "小雪", "冬至" };
	private static String[] sectionalTermNames = { "小寒", "立春", "惊蛰", "清明",
			"立夏", "芒种", "小暑", "立秋", "白露", "寒露", "立冬", "大雪" };

	private static String[] lunarDateName = { "初一", "初二", "初三", "初四",
			"初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三",
			"十四", "十五", "十六", "十七", "十八", "十九", "二十", "廿一", "廿二",
			"廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十", "三十一" };

	String getLunarDate() {
		String date = new String();
		int realLunarMonth = lunarMonth;
		if (realLunarMonth < 0) {
			realLunarMonth = -realLunarMonth;
			date += "闰 ";
		}
		date = date + lunarMonthNames[realLunarMonth - 1] + "月" + lunarDateName[lunarDate - 1];
		if (gregorianDate == sectionalTerm) {
			date = date + " " + sectionalTermNames[gregorianMonth - 1];
		} else if (gregorianDate == principleTerm) {
			date = date + " " + principleTermNames[gregorianMonth - 1];
		}
		return date;
	}
}
