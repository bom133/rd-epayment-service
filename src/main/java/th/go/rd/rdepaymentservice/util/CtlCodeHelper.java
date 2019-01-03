package th.go.rd.rdepaymentservice.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CtlCodeHelper {

	public String generateCtlCode(String runningNo, LocalDate expDate, String AgentID, String totalAmount) {

		String ctl_code = "";
		// 1.1
		String ctl_code_1_2 = return2Len(Integer.toString(expDate.getDayOfMonth()));

		// 1.2
		LocalDate start_date = LocalDate.of(2002, 1, 1);
		// LocalDate exp_date =
		// expDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		long monthDiff = ChronoUnit.MONTHS.between(start_date, expDate);
		//System.out.println("Month Diff : " + monthDiff);
		String ctl_code_6_7 = Long.toString(monthDiff);
		String ctl_code_15 = "";
		if (ctl_code_6_7.length() > 2) {
			ctl_code_15 = ctl_code_6_7.substring(0, 1);
			ctl_code_6_7 = return2Len(ctl_code_6_7.substring(1, 3));
		} else {
			ctl_code_15 = "0";
			ctl_code_6_7 = return2Len(ctl_code_6_7.substring(0, ctl_code_6_7.length()));
		}
		//System.out.println(ctl_code_6_7);

		// 1.3
		String ctl_code_3_5 = runningNo.substring(0, 3);
		// 1.4
		String ctl_code_8_10 = runningNo.substring(3, 6);

		// 1.5.1
		int agentID12 = tryParseInt(AgentID.substring(0, 2));
		int agentID34 = tryParseInt(AgentID.substring(2, 4));
		int agentID56 = tryParseInt(AgentID.substring(4, 6));
		int agentID78 = tryParseInt(AgentID.substring(6, 8));
		int agentID910 = tryParseInt(AgentID.substring(8, 10));
		int agentID1113 = tryParseInt(AgentID.substring(10, 13));
		int sum = agentID12 * 13;
		sum = sum + (agentID34 * 11);
		sum = sum + (agentID56 * 7);
		sum = sum + (agentID78 * 5);
		sum = sum + (agentID910 * 3);
		sum = sum + (agentID1113 * 1);
		// 1.5.2
		sum = sum + calMoney(totalAmount);
		// 1.5.3
		sum = sum + (tryParseInt(ctl_code_1_2.substring(0, 1)) * 17);
		sum = sum + (tryParseInt(ctl_code_1_2.substring(1, 2)) * 19);
		sum = sum + (tryParseInt(ctl_code_3_5.substring(0, 1)) * 23);
		sum = sum + (tryParseInt(ctl_code_3_5.substring(1, 2)) * 29);
		sum = sum + (tryParseInt(ctl_code_3_5.substring(2, 3)) * 31);
		sum = sum + (tryParseInt(ctl_code_6_7.substring(0, 1)) * 37);
		sum = sum + (tryParseInt(ctl_code_6_7.substring(1, 2)) * 41);
		sum = sum + (tryParseInt(ctl_code_8_10.substring(0, 1)) * 43);
		sum = sum + (tryParseInt(ctl_code_8_10.substring(1, 2)) * 53);
		sum = sum + (tryParseInt(ctl_code_8_10.substring(2, 3)) * 59);
		sum = sum + (tryParseInt(ctl_code_15) * 117);
		//System.out.println("final sum : " + sum);
		// 1.5.4
		String ctl_code_11_14 = Integer.toString((sum % 10000));

		ctl_code = ctl_code_1_2 + ctl_code_3_5 + ctl_code_6_7 + ctl_code_8_10 + ctl_code_11_14 + ctl_code_15;
		return ctl_code;
	}

	private int tryParseInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw e;
		}
	}

	private static String return2Len(String s) {

		while (s.length() < 2) {
			s = "0" + s;
		}
		return s;
	}

	private int calMoney(String totalAmount) {
		int sum = 0;
		String totalAmount_str = totalAmount.replaceAll(",", "");
		int str_len = totalAmount_str.length();
		int dot_pos = totalAmount_str.indexOf('.');
		if (dot_pos > 0) {
			sum = sum + calAtPos(totalAmount_str, 61);
			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			sum = sum + calAtPos(totalAmount_str, 67);
			totalAmount_str = totalAmount_str.substring(0, dot_pos);
		}
		//System.out.println("new amount : " + totalAmount_str);
		try {
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 71);

			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 73);

			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 79);

			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 83);

			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 89);

			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 97);

			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 101);

			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 103);

			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 107);

			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 111);

			totalAmount_str = totalAmount_str.substring(0, str_len - 1);
			str_len = totalAmount_str.length();
			sum = sum + calAtPos(totalAmount_str, 113);
		} catch (Exception ex) {
		}
		return sum;
	}

	public int calAtPos(String str, int value) {
		int sum = 0;
		int str_len = str.length();
		if (str_len > 0) {
			int money = tryParseInt(str.substring(str_len - 1, str_len));
			sum = sum + money * value;
			//System.out.println("money : " + money + " * " + value + " = " + sum);
		}
		return sum;
	}
}
