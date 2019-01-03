package th.go.rd.rdepaymentservice.util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.hibernate.ScrollableResults;
import org.hibernate.internal.AbstractScrollableResults;

public class SqlUtil {

	public static String[] getColumnNamesResultSetFromHackishScrollAbleResult(ScrollableResults scroll) {
		try {
			Method method = AbstractScrollableResults.class.getDeclaredMethod("getResultSet");
			method.setAccessible(true);
			ResultSet resultSet = (ResultSet) method.invoke(scroll);
			ResultSetMetaData metaData = resultSet.getMetaData();
			String[] colName = new String[metaData.getColumnCount()];
			for (int i = 0; i < metaData.getColumnCount(); i++) {
				colName[i] = metaData.getColumnLabel(i + 1); // sql column index by 1
			}
			return colName;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
