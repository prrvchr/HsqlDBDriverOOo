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

import com.sun.star.beans.PropertyValue;
import com.sun.star.container.XNameAccess;
import com.sun.star.sdbc.SQLException;
import com.sun.star.sdbc.XConnection;
import com.sun.star.sdbc.XDatabaseMetaData;
import com.sun.star.sdbc.XPreparedStatement;
import com.sun.star.sdbc.XStatement;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;

import io.github.prrvchr.comp.sdb.CallableStatement;
import io.github.prrvchr.comp.sdb.PreparedStatement;
import io.github.prrvchr.comp.sdbcx.Statement;
import io.github.prrvchr.comp.sdbc.DatabaseMetaData;
import io.github.prrvchr.comp.helper.UnoHelper;


public class Connection
extends WarningsSupplierComponent
implements XConnection
{
	private final XComponentContext m_xContext;
	private final java.sql.Connection m_Connection;
	private final PropertyValue[] m_info;
	private final String m_url;
	private static String m_name = Connection.class.getName();
	private static String[] m_services = {"com.sun.star.sdbc.Connection"};

	// The constructor method:
	public Connection(XComponentContext ctx,
                      java.sql.Connection connection,
                      PropertyValue[] info,
                      String url)
	{
		System.out.println("Connection.Connection() 1");
		m_xContext = ctx;
		m_Connection = connection;
		m_info = info;
		m_url = url;
		System.out.println("Connection.Connection() 2");
	}
	public Connection(String name,
                      String[] services,
                      XComponentContext ctx,
                      java.sql.Connection connection,
                      PropertyValue[] info,
                      String url)
	{
		m_name = name;
		m_services = services;
		m_xContext = ctx;
		m_Connection = connection;
		m_info = info;
		m_url = url;
	}
	

	// com.sun.star.lang.XServiceInfo:
	@Override
	public String _getImplementationName()
	{
		return m_name;
	}
	@Override
	public String[] _getServiceNames() {
		return m_services;
	}

	// com.sun.star.sdbc.XWarningsSupplier:
	@Override
	public java.sql.Wrapper _getWrapper(){
		return m_Connection;
	}
	@Override
	public XInterface _getInterface()
	{
		return this;
	}


	// com.sun.star.sdbc.XConnection:
	@Override
	public XDatabaseMetaData getMetaData() throws SQLException
	{
		try
		{
			java.sql.DatabaseMetaData metadata = m_Connection.getMetaData();
			return new DatabaseMetaData(m_xContext, this, metadata, m_info, m_url);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public void close() throws SQLException
	{
		try
		{
			if (! m_Connection.isClosed())
			{
				m_Connection.close();
				dispose();
			}
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
		
	}

	@Override
	public void commit() throws SQLException
	{
		try
		{
			m_Connection.commit();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public boolean getAutoCommit() throws SQLException
	{
		try
		{
			return m_Connection.getAutoCommit();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public String getCatalog() throws SQLException
	{
		try
		{
			return m_Connection.getCatalog();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public int getTransactionIsolation() throws SQLException
	{
		try
		{
			return m_Connection.getTransactionIsolation();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public boolean isClosed() throws SQLException
	{
		try
		{
			return m_Connection.isClosed();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public boolean isReadOnly() throws SQLException
	{
		try
		{
			return m_Connection.isReadOnly();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public String nativeSQL(String sql) throws SQLException
	{
		try
		{
			return m_Connection.nativeSQL(sql);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public void rollback() throws SQLException
	{
		try
		{
			m_Connection.rollback();
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public void setAutoCommit(boolean commit) throws SQLException
	{
		try
		{
			m_Connection.setAutoCommit(commit);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public void setCatalog(String catalog) throws SQLException
	{
		try
		{
			m_Connection.setCatalog(catalog);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public void setReadOnly(boolean readonly) throws SQLException
	{
		try
		{
			m_Connection.setReadOnly(readonly);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public void setTransactionIsolation(int isolation) throws SQLException
	{
		try
		{
			m_Connection.setTransactionIsolation(isolation);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public XNameAccess getTypeMap() throws SQLException
	{
		// TODO: Implement me!!!
		return null;
	}

	@Override
	public void setTypeMap(XNameAccess typemap) throws SQLException
	{
		// TODO: Implement me!!!
	}

	@Override
	public XStatement createStatement() throws SQLException
	{
		try
		{
			int type, concurrency;
			type = Statement.m_ResultSetType;
			concurrency = Statement.m_ResultSetConcurrency;
			java.sql.Statement statement = m_Connection.createStatement(type, concurrency);
			return new Statement(m_xContext, this, statement);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public XPreparedStatement prepareCall(String sql) throws SQLException
	{
		try
		{
			int type, concurrency;
			type = CallableStatement.m_ResultSetType;
			concurrency = CallableStatement.m_ResultSetConcurrency;
			java.sql.CallableStatement statement = m_Connection.prepareCall(sql, type, concurrency);
			return new CallableStatement(m_xContext, this, statement);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}

	@Override
	public XPreparedStatement prepareStatement(String sql) throws SQLException
	{
		try
		{
			int type, concurrency;
			type = PreparedStatement.m_ResultSetType;
			concurrency = PreparedStatement.m_ResultSetConcurrency;
			java.sql.PreparedStatement statement = m_Connection.prepareStatement(sql, type, concurrency);
			return new PreparedStatement(m_xContext, this, statement);
		} catch (java.sql.SQLException e)
		{
			throw UnoHelper.getException(e, this);
		}
	}


}