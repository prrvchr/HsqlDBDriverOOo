/*
╔════════════════════════════════════════════════════════════════════════════════════╗
║                                                                                    ║
║   Copyright (c) 2020 https://prrvchr.github.io                                     ║
║                                                                                    ║
║   Permission is hereby granted, free of charge, to any person obtaining            ║
║   a copy of this software and associated documentation files (the "Software"),     ║
║   to deal in the Software without restriction, including without limitation        ║
║   the rights to use, copy, modify, merge, publish, distribute, sublicense,         ║
║   and/or sell copies of the Software, and to permit persons to whom the Software   ║
║   is furnished to do so, subject to the following conditions:                      ║
║                                                                                    ║
║   The above copyright notice and this permission notice shall be included in       ║
║   all copies or substantial portions of the Software.                              ║
║                                                                                    ║
║   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,                  ║
║   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES                  ║
║   OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.        ║
║   IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY             ║
║   CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,             ║
║   TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE       ║
║   OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                                    ║
║                                                                                    ║
╚════════════════════════════════════════════════════════════════════════════════════╝
*/
package io.github.prrvchr.comp.sdbc;

import java.util.HashMap;
import java.util.Map;

import com.sun.star.beans.Property;
import com.sun.star.sdbc.SQLException;
import com.sun.star.sdbc.XCloseable;
import com.sun.star.sdbc.XConnection;
import com.sun.star.sdbc.XMultipleResults;
import com.sun.star.sdbc.XResultSet;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;
import com.sun.star.util.XCancellable;

import io.github.prrvchr.comp.helper.UnoHelper;


public abstract class SuperStatement<T>
extends WarningsSupplierProperty
implements XCancellable,
           XCloseable,
           XMultipleResults
{
	public final XComponentContext m_xContext;
	public final XConnection m_xConnection;
	public final java.sql.Statement m_Statement;
	private String m_CursorName = "";
	public static int m_ResultSetConcurrency = java.sql.ResultSet.CONCUR_READ_ONLY;
	public static int m_ResultSetType = java.sql.ResultSet.TYPE_FORWARD_ONLY;


	private static Map<String, Property> _getPropertySet()
	{
		Map<String, Property> map = new HashMap<String, Property>();
		Property p1 = UnoHelper.getProperty("CursorName", "string");
		map.put(UnoHelper.getPropertyName(p1), p1);
		Property p2 = UnoHelper.getProperty("FetchDirection", "long");
		map.put(UnoHelper.getPropertyName(p2), p2);
		Property p3 = UnoHelper.getProperty("FetchSize", "long");
		map.put(UnoHelper.getPropertyName(p3), p3);
		Property p4 = UnoHelper.getProperty("MaxFieldSize", "long");
		map.put(UnoHelper.getPropertyName(p4), p4);		
		Property p5 = UnoHelper.getProperty("MaxRows", "long");
		map.put(UnoHelper.getPropertyName(p5), p5);		
		Property p6 = UnoHelper.getProperty("QueryTimeout", "long");
		map.put(UnoHelper.getPropertyName(p6), p6);
		Property p7 = UnoHelper.getProperty("ResultSetConcurrency", "long");
		map.put(UnoHelper.getPropertyName(p7), p7);
		Property p8 = UnoHelper.getProperty("ResultSetType", "long");
		map.put(UnoHelper.getPropertyName(p8), p8);
		return map;
	}
	private static Map<String, Property> _getPropertySet(Map<String, Property> properties)
	{
		Map<String, Property> map = _getPropertySet();
		map.putAll(properties);
		return map;
	}


	// The constructor method:
	public SuperStatement(XComponentContext context,
                          XConnection connection,
                          java.sql.Statement statement,
                          Map<String, Property> properties)
	{
		super(_getPropertySet(properties));
		m_xContext = context;
		m_xConnection = connection;
		m_Statement = statement;
	}
	public SuperStatement(XComponentContext context,
                          XConnection connection,
                          java.sql.PreparedStatement statement)
	{
		super(_getPropertySet());
		m_xContext = context;
		m_xConnection = connection;
		m_Statement = statement;
	}
	public SuperStatement(XComponentContext context,
                          XConnection connection,
                          java.sql.PreparedStatement statement,
                          Map<String, Property> properties)
	{
		super(_getPropertySet(properties));
		m_xContext = context;
		m_xConnection = connection;
		m_Statement = statement;
	}
	public SuperStatement(XComponentContext context,
                          XConnection connection,
                          java.sql.CallableStatement statement)
	{
		super(_getPropertySet());
		m_xContext = context;
		m_xConnection = connection;
		m_Statement = statement;
	}
	public SuperStatement(XComponentContext context,
                          XConnection connection,
                          java.sql.CallableStatement statement,
                          Map<String, Property> properties)
	{
		super(_getPropertySet(properties));
		m_xContext = context;
		m_xConnection = connection;
		m_Statement = statement;
	}


	public String getCursorName() throws SQLException
	{
		return m_CursorName;
	}
	public void setCursorName(String name) throws SQLException
	{
		try
		{
			m_CursorName = name;
			m_Statement.setCursorName(m_CursorName);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	public int getQueryTimeout() throws SQLException
	{
		try
		{
			return m_Statement.getQueryTimeout();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}
	public void setQueryTimeout(int timeout) throws SQLException
	{
		try
		{
			m_Statement.setQueryTimeout(timeout);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	public int getMaxFieldSize() throws SQLException
	{
		try
		{
			return m_Statement.getMaxFieldSize();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
		
	}
	public void setMaxFieldSize(int size) throws SQLException
	{
		try
		{
			m_Statement.setMaxFieldSize(size);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	public int getMaxRows() throws SQLException
	{
		try
		{
			return m_Statement.getMaxRows();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
		
	}
	public void setMaxRows(int row) throws SQLException
	{
		try
		{
			m_Statement.setMaxRows(row);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	public int getFetchDirection() throws SQLException
	{
		try
		{
			return m_Statement.getFetchDirection();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
		
	}
	public void setFetchDirection(int direction) throws SQLException
	{
		try
		{
			m_Statement.setFetchDirection(direction);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
		
	}

	public int getFetchSize() throws SQLException
	{
		try
		{
			return m_Statement.getFetchSize();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
		
	}
	public void setFetchSize(int size) throws SQLException
	{
		try
		{
			m_Statement.setFetchSize(size);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
		
	}

	public int getResultSetConcurrency() throws SQLException
	{
		try
		{
			return m_Statement.getResultSetConcurrency();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
		
	}
	public void setResultSetConcurrency(int value)
	{
		m_ResultSetConcurrency = value;
	}
	
	public int getResultSetType() throws SQLException
	{
		try
		{
			return m_Statement.getResultSetType();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
		
	}
	public void setResultSetType(int value)
	{
		m_ResultSetType = value;
	}


	// com.sun.star.util.XCancellable:
	@Override
	public void cancel()
	{
		try
		{
			m_Statement.cancel();
		} catch (java.sql.SQLException e)
		{
			e.printStackTrace();
		}
	}

	// com.sun.star.sdbc.XCloseable
	@Override
	public void close() throws SQLException
	{
		try
		{
			m_Statement.close();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}


	// com.sun.star.sdbc.XMultipleResults:
	@Override
	public boolean getMoreResults() throws SQLException
	{
		try
		{
			return m_Statement.getMoreResults();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public XResultSet getResultSet() throws SQLException
	{
		try
		{
			java.sql.ResultSet resultset = m_Statement.getResultSet();
			XInterface component = _getInterface();
			return new ResultSet(m_xContext, component, resultset);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public int getUpdateCount() throws SQLException
	{
		try
		{
			return m_Statement.getUpdateCount();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}


}
