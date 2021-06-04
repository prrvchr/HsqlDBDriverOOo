package io.github.prrvchr.comp.helper;

import java.lang.Exception;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import com.sun.star.beans.Property;
import com.sun.star.beans.PropertyValue;
import com.sun.star.deployment.XPackageInformationProvider;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sdbc.DriverPropertyInfo;
import com.sun.star.sdbc.SQLException;
import com.sun.star.sdbc.SQLWarning;
import com.sun.star.sdbc.XArray;
import com.sun.star.sdbc.XBlob;
import com.sun.star.sdbc.XClob;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;
import com.sun.star.util.Date;
import com.sun.star.util.DateTime;
import com.sun.star.util.Time;


public class UnoHelper
{

	public static Object createService(XComponentContext context, String identifier)
	{
		Object service = null;
		try
		{
			XMultiComponentFactory manager = context.getServiceManager();
			service = manager.createInstanceWithContext(identifier, context);
		}
		catch (Exception e) { e.printStackTrace(); }
		return service;
	}


	public static String getPackageLocation(XComponentContext context, String identifier, String path)
	{
		String location = getPackageLocation(context, identifier);
		return location + "/" + path + "/";
	}

	public static String getPackageLocation(XComponentContext context, String identifier)
	{
		String location = "";
		XPackageInformationProvider xProvider = null;
		try
		{
			Object oProvider = context.getValueByName("/singletons/com.sun.star.deployment.PackageInformationProvider");
			xProvider = (XPackageInformationProvider) UnoRuntime.queryInterface(XPackageInformationProvider.class, oProvider);
		}
		catch (Exception e) { e.printStackTrace(); }
		if (xProvider != null) location = xProvider.getPackageLocation(identifier);
		return location;
	}

	public static URL getDriverURL(String location, String jar)
	{
		URL url = null;
		try
		{
			url = new URL("jar:" + location + jar + "!/");
		}
		catch (Exception e) { e.printStackTrace(); }
		return url;
	}
	
	
	public static URL getDriverURL(String location, String path, String jar)
	throws MalformedURLException
	{
		URL url = new URL("jar:" + location + "/" + path + "/" + jar + "!/");
		return url;
	}


	public static DriverPropertyInfo[] getDriverPropertyInfos()
	{
		ArrayList<DriverPropertyInfo> infos = new ArrayList<>();
		DriverPropertyInfo info1 = getDriverInfo("AutoIncrementCreation",
                                                 "GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY");
		infos.add(0, info1);
		DriverPropertyInfo info2 = getDriverInfo("AutoRetrievingStatement",
                                                 "CALL IDENTITY()");
		infos.add(0, info2);
		int len = infos.size();
		return infos.toArray(new DriverPropertyInfo[len]);
	}

	public static DriverPropertyInfo getDriverInfo(String name, String value)
	{
		DriverPropertyInfo info = new DriverPropertyInfo();
		info.Name = name;
		info.Value = value;
		info.IsRequired = true;
		info.Choices = new String[0];
		return info;
	}


	public static Properties getConnectionProperties(PropertyValue[] infos)
	{
		System.out.println("UnoHelper.getProperties() 1 ");
		Properties properties = new Properties();
		int len = infos.length;
		for (int i = 0; i < len; i++)
		{
			PropertyValue info = infos[i];
			System.out.println("UnoHelper.getProperties() 2 " + info.Name + " -" + (String)info.Value + "-");
			String value = (String) info.Value;
			// FIXME: JDBC doesn't seem to like <Properties> with empty values!!!
			if (!value.isEmpty()) properties.setProperty(info.Name, value);
		}
		System.out.println("UnoHelper.getProperties() 2 " + properties);
		return properties;
	}


	public static String getPropertyName(Property property)
	{
		return String.format("m_%s", property.Name);
	}


	public static Property getProperty(String name, String type)
	{
		short attributes = 0;
		return getProperty(name,  type, attributes);
	}

	public static Property getProperty(String name, String type, short attributes)
	{
		int handle = -1;
		return getProperty(name,  handle, type, attributes);
	}

	public static Property getProperty(String name, int handle, String type, short attributes)
	{
		Property property = new Property();
		property.Name = name;
		property.Handle = handle;
		property.Type = new Type(type);
		property.Attributes = attributes;
		return property;
	}


	public static SQLException getException(java.sql.SQLException e, XInterface component)
	{
		return getSQLException(e, component);
	}

	public static SQLException getSQLException(java.sql.SQLException e, XInterface component)
	{
		SQLException exception = null;
		if (e != null)
		{
			String message = e.getMessage();
			exception = new SQLException(message);
			exception.Context = component;
			exception.SQLState = e.getSQLState();
			exception.ErrorCode = e.getErrorCode();
			exception.NextException = getSQLException(e.getNextException(), component);
		}
		return exception;
	}


	public static Object getWarning(java.sql.SQLWarning w, XInterface component)
	{
		return getSQLWarning(w, component);
	}

	public static Object getSQLWarning(java.sql.SQLWarning w, XInterface component)
	{
		SQLWarning warning = null;
		if (w != null)
		{
			String message = w.getMessage();
			warning = new SQLWarning(message);
			warning.Context = component;
			warning.SQLState = w.getSQLState();
			warning.ErrorCode = w.getErrorCode();
			warning.NextException = getSQLWarning(w.getNextWarning(), component);
		}
		return warning;
	}


	public static String getObjectString(Object object)
	{
		String value = "";
		if (AnyConverter.isString(object))
		{
			value = AnyConverter.toString(object);
		}
		return value;
	}


	@SuppressWarnings("deprecation")
	public static Date getUnoDate(Date value, java.sql.Date date)
	{
		value.Year = (short) date.getYear();
		value.Month = (short) date.getMonth();
		value.Day = (short) date.getDay();
		return value;
	}


	@SuppressWarnings("deprecation")
	public static java.sql.Date getJavaDate(Date date)
	{
		int year = date.Year;
		int month = date.Month;
		int day = date.Day;
		java.sql.Date value = new java.sql.Date(year, month, day);
		return value;
	}


	@SuppressWarnings("deprecation")
	public static Time getUnoTime(Time value, java.sql.Time time)
	{
		value.Hours = (short) time.getHours();
		value.Minutes = (short) time.getMinutes();
		value.Seconds = (short) time.getSeconds();
		//value.NanoSeconds = 0;
		//value.HundredthSeconds = 0;
		return value;
	}


	@SuppressWarnings("deprecation")
	public static java.sql.Time getJavaTime(Time time)
	{
		int hours = time.Hours;
		int minutes = time.Minutes;
		int seconds = time.Seconds;
		java.sql.Time value = new java.sql.Time(hours, minutes, seconds);
		return value;
	}


	@SuppressWarnings("deprecation")
	public static DateTime getUnoDateTime(DateTime value, java.sql.Timestamp timestamp)
	{
		value.Day = (short) timestamp.getDay();
		value.Month = (short) timestamp.getMonth();
		value.Year = (short) timestamp.getYear();
		value.Hours = (short) timestamp.getHours();
		value.Minutes = (short) timestamp.getMinutes();
		value.Seconds = (short) timestamp.getSeconds();
		//value.NanoSeconds = 0;
		//value.HundredthSeconds = 0;
		return value;
	}


	@SuppressWarnings("deprecation")
	public static java.sql.Timestamp getJavaDateTime(DateTime timestamp)
	{
		int year = timestamp.Year;
		int month = timestamp.Month;
		int day = timestamp.Day;
		int hours = timestamp.Hours;
		int minutes = timestamp.Minutes;
		int seconds = timestamp.Seconds;
		java.sql.Timestamp value = new java.sql.Timestamp(year, month, day, hours, minutes, seconds, 0);
		return value;
	}


	public static Object getObjectFromResult(java.sql.ResultSet result, int index)
	{
		Object value = null;
		try
		{
			value = result.getObject(index);
		}
		catch (java.sql.SQLException e) {e.getStackTrace();}
		return value;
	}


	public static String getResultSetValue(java.sql.ResultSet result, int index)
	{
		String value = null;
		try
		{
			value = result.getString(index);
		}
		catch (java.sql.SQLException e) {e.getStackTrace();}
		return value;
	}


	public static Object getValueFromResult(java.sql.ResultSet result, int index)
	{
		// TODO: 'TINYINT' is buggy: don't use it
		Object value = null;
		try
		{
			String dbtype = result.getMetaData().getColumnTypeName(index);
			if (dbtype == "VARCHAR")
			{
				value = result.getString(index);
			}
			else if (dbtype == "BOOLEAN")
			{
				value = result.getBoolean(index);
			}
			else if (dbtype == "TINYINT"){
				value = result.getShort(index);
			}
			else if (dbtype == "SMALLINT"){
				value = result.getShort(index);
			}
			else if (dbtype == "INTEGER"){
				value = result.getInt(index);
			}
			else if (dbtype == "BIGINT"){
				value = result.getLong(index);
			}
			else if (dbtype == "FLOAT"){
				value = result.getFloat(index);
			}
			else if (dbtype == "DOUBLE"){
				value = result.getDouble(index);
			}
			else if (dbtype == "TIMESTAMP"){
				value = result.getTimestamp(index);
			}
			else if (dbtype == "TIME"){
				value = result.getTime(index);
			}
			else if (dbtype == "DATE"){
				value = result.getDate(index);
			}
		}
		catch (java.sql.SQLException e) {e.getStackTrace();}
		return value;
	}


	public static XArray getUnoArray(java.sql.CallableStatement statement, int index)
	throws java.sql.SQLException
	{
		XArray value = null;
		System.out.println("UnoHelper.getUnoArray() 1");
		java.sql.Array array = statement.getArray(index);
		System.out.println("UnoHelper.getUnoArray() 2");
		if (!statement.wasNull()) value = new ArrayToXArrayAdapter(array);
		System.out.println("UnoHelper.getUnoArray() 3");
		return value;
	}


	public static java.sql.Array getJavaArray(java.sql.Statement statement, XArray array)
	throws java.sql.SQLException, SQLException
	{
		String type = array.getBaseTypeName();
		Object[] value = array.getArray(null);
		return statement.getConnection().createArrayOf(type, value);
	}


	public static java.sql.Clob getJavaClob(java.sql.Statement statement, XClob clob)
	throws java.sql.SQLException, SQLException
	{
		System.out.println("UnoHelper.getJavaClob() 1");
		String value = clob.toString();
		System.out.println("UnoHelper.getJavaClob() 2");
		java.sql.Clob c = statement.getConnection().createClob();
		c.setString(1, value);
		System.out.println("UnoHelper.getJavaClob() 3");
		return c;
	}

	public static java.sql.Blob getJavaBlob(java.sql.Statement statement, XBlob blob)
	throws java.sql.SQLException, SQLException
	{
		System.out.println("UnoHelper.getJavaBlob() 1");
		int len =  (int) blob.length();
		byte[] value = blob.getBytes(1, len);
		System.out.println("UnoHelper.getJavaBlob() 2");
		java.sql.Blob b = statement.getConnection().createBlob();
		b.setBytes(1, value);
		System.out.println("UnoHelper.getJavaBlob() 3");
		return b;
	}
	

}
