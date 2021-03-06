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
package io.github.prrvchr.comp.sdb;

import com.sun.star.container.XNameAccess;
import com.sun.star.sdbc.XConnection;
import com.sun.star.sdbcx.XColumnsSupplier;
import com.sun.star.uno.XComponentContext;

import io.github.prrvchr.comp.sdbcx.BasePreparedStatement;
import io.github.prrvchr.comp.sdbcx.ColumnsSupplier;


public final class PreparedStatement
extends BasePreparedStatement<PreparedStatement>
implements XColumnsSupplier
{
	private static String m_name = PreparedStatement.class.getName();
	private static String[] m_services = {"com.sun.star.sdb.PreparedStatement",
                                          "com.sun.star.sdbc.PreparedStatement",
                                          "com.sun.star.sdbcx.PreparedStatement"};
	private final java.sql.PreparedStatement m_Statement;

	// The constructor method:
	public PreparedStatement(XComponentContext context,
                             XConnection connection,
                             java.sql.PreparedStatement statement)
	{
		super(context, connection, statement);
		m_Statement = statement;
	}

	// com.sun.star.sdbcx.XColumnsSupplier:
	@Override
	public XNameAccess getColumns()
	{
		try
		{
			java.sql.ResultSetMetaData metadata = m_Statement.getMetaData();
			return ColumnsSupplier.getColumns(metadata);
		}
		catch (java.sql.SQLException e)
		{
			// pass
		}
		return null;
	}


	// com.sun.star.lang.XServiceInfo:
	@Override
	public String _getImplementationName()
	{
		return m_name;
	}
	@Override
	public String[] _getServiceNames()
	{
		return m_services;
	}


}
