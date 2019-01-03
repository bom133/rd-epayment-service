package th.go.rd.rdepaymentservice.manager;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OrmXmlManager {
	private static Map<String, String> queryMap = new HashMap<String, String>();

//	public static void init(String configFile) {
	public static void init(InputStream stream) {

		class OrmXmlHandler extends DefaultHandler {
			private Map<String, String> queryMap = new HashMap<String, String>();
			private String _qName;
			private String _queryName;
			private String _queryString = "";

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes)
					throws SAXException {

				this._qName = qName;

				if ("named-native-query".equals(qName) || "named-query".equals(qName)) {
					this._queryName = attributes.getValue("name");
				}
			}

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {

				if ("query".equals(qName)) {
					queryMap.put(_queryName, _queryString.trim());
					_queryString = "";
				}

			}

			@Override
			public void characters(char[] ch, int start, int length) throws SAXException {

				if ("query".equals(this._qName)) {
					this._queryString += new String(ch, start, length);

				}
			}

			public Map<String, String> getQueryMap() {
				return queryMap;
			}
		}

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;

		try {
			parser = factory.newSAXParser();

			OrmXmlHandler handler = new OrmXmlHandler();
//			parser.parse(new File(configFile), handler);
			parser.parse(stream, handler);

			queryMap = handler.getQueryMap();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static class NoSuchSQLException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		NoSuchSQLException(String key) {
			super("No SQL map with key " + key);
		}
	}

	public static String getQuery(String key) {
		if (queryMap.containsKey(key)) {
			return queryMap.get(key);
		} else {
			throw new NoSuchSQLException(key);
		}
	}

}