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

import com.sun.star.sdbc.XConnection;
import com.sun.star.uno.XComponentContext;
import com.sun.star.uno.XInterface;


public class PreparedStatement
extends SuperPreparedStatement<PreparedStatement>
{
	private static final String m_name = PreparedStatement.class.getName();
	private static final String[] m_services = {"com.sun.star.sdbc.PreparedStatement"};
	private java.sql.PreparedStatement m_Statement;


	// The constructor method:
	public PreparedStatement(XComponentContext context,
                             XConnection connection,
                             java.sql.PreparedStatement statement)
	{
		super(context, connection, statement);
		m_Statement = statement;
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


	// com.sun.star.sdbc.XWarningsSupplier:
	@Override
	public java.sql.Wrapper _getWrapper(){
		return m_Statement;
	}
	@Override
	public XInterface _getInterface()
	{
		return this;
	}


}
