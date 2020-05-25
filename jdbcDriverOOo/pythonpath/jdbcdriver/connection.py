#!
# -*- coding: utf_8 -*-

import uno
import unohelper

from com.sun.star.lang import XServiceInfo
from com.sun.star.lang import XComponent
from com.sun.star.sdbc import XConnection
from com.sun.star.sdbc import XCloseable
from com.sun.star.sdbc import XWarningsSupplier
from com.sun.star.sdb import XCommandPreparation
from com.sun.star.sdb import XQueriesSupplier
from com.sun.star.sdb import XSQLQueryComposerFactory
from com.sun.star.lang import XMultiServiceFactory
from com.sun.star.container import XChild
from com.sun.star.sdb.application import XTableUIProvider
from com.sun.star.sdb.tools import XConnectionTools
from com.sun.star.sdb.CommandType import TABLE
from com.sun.star.sdb.CommandType import QUERY
from com.sun.star.sdb.CommandType import COMMAND
from com.sun.star.beans.PropertyAttribute import READONLY

from com.sun.star.sdbc import SQLException

from com.sun.star.uno import XWeak
from com.sun.star.uno import XAdapter

from com.sun.star.sdbcx import XTablesSupplier
from com.sun.star.sdbcx import XViewsSupplier
from com.sun.star.sdbcx import XUsersSupplier
from com.sun.star.sdbcx import XGroupsSupplier

from com.sun.star.sdbcx import XUser
from com.sun.star.container import XNameAccess
from com.sun.star.container import XIndexAccess
from com.sun.star.container import XEnumerationAccess
from com.sun.star.container import XElementAccess

from unolib import PropertySet
from unolib import getProperty
from unolib import createService

from .dbtools import getSequenceFromResult
from .dbqueries import getSqlQuery

from .documentdatasource import DocumentDataSource
from .databasemetadata import DatabaseMetaData
from .statement import Statement
from .statement import PreparedStatement
from .statement import CallableStatement

import traceback


class Connection(unohelper.Base,
                 XServiceInfo,
                 XComponent,
                 XWarningsSupplier,
                 XConnection,
                 XCloseable,
                 XCommandPreparation,
                 XQueriesSupplier,
                 XSQLQueryComposerFactory,
                 XMultiServiceFactory,
                 XChild,
                 XTablesSupplier,
                 XViewsSupplier,
                 XUsersSupplier,
                 XGroupsSupplier,
                 XTableUIProvider,
                 XConnectionTools,
                 XWeak):
    def __init__(self, ctx, connection, protocols, username):
        self.ctx = ctx
        self._connection = connection
        self._protocols = protocols
        self._username = username

    # XComponent
    def dispose(self):
        self._connection.dispose()
    def addEventListener(self, listener):
        self._connection.addEventListener(listener)
    def removeEventListener(self, listener):
        self._connection.removeEventListener(listener)

    # XWeak
    def queryAdapter(self):
        return self._connection.queryAdapter()

    # XTableUIProvider
    def getTableIcon(self, tablename, colormode):
        return self._connection.getTableIcon(tablename, colormode)
    def getTableEditor(self, documentui, tablename):
        return self._connection.getTableEditor(documentui, tablename)

    # XConnectionTools
    def createTableName(self):
        return self._connection.createTableName()
    def getObjectNames(self):
        return self._connection.getObjectNames()
    def getDataSourceMetaData(self):
        return self._connection.getDataSourceMetaData()
    def getFieldsByCommandDescriptor(self, commandtype, command, keep):
        fields, keep = self._connection.getFieldsByCommandDescriptor(commandtype, command, keep)
        return fields, keep
    def getComposer(self, commandtype, command):
        return self._connection.getComposer(commandtype, command)

    # XCloseable
    def close(self):
        if not self._connection.isClosed():
            self._connection.close()

    # XCommandPreparation
    def prepareCommand(self, command, commandtype):
        # TODO: cannot use: self._connection.prepareCommand()
        # TODO: it trow a: java.lang.IncompatibleClassChangeError
        # TODO: in the same way when using self._connection.prepareStatement(sql)
        # TODO: fallback to: self._connection.prepareCall(sql)
        print("Connection.prepareCommand()")
        sql = None
        if commandtype == TABLE:
            sql = 'SELECT * FROM "%s"' % command
        elif commandtype == QUERY:
            if self.getQueries().hasByName(command):
                sql = self.getQueries().getByName(command).Command
        elif commandtype == COMMAND:
            sql = command
        if sql is not None:
            statement = PreparedStatement(self, sql)
            return statement
        raise SQLException()

    # XQueriesSupplier
    def getQueries(self):
        return self._connection.getQueries()

    # XSQLQueryComposerFactory
    def createQueryComposer(self):
        return self._connection.createQueryComposer()

    # XMultiServiceFactory
    def createInstance(self, service):
        return self._connection.createInstance(service)
    def createInstanceWithArguments(self, service, arguments):
        return self._connection.createInstanceWithArguments(service, arguments)
    def getAvailableServiceNames(self):
        return self._connection.getAvailableServiceNames()

    # XChild
    def getParent(self):
        parent = self._connection.getParent()
        return DocumentDataSource(parent, self._protocols, self._username)
    def setParent(self):
        pass

    # XTablesSupplier
    def getTables(self):
        return self._connection.getTables()

    # XViewsSupplier
    def getViews(self):
        return self._connection.getViews()

    # XUsersSupplier
    def getUsers(self):
        try:
            print("Connection.getUsers()1")
            query = getSqlQuery('getUsers')
            result = self._connection.createStatement().executeQuery(query)
            users = getSequenceFromResult(result)
            #mri = createService(self.ctx, 'mytools.Mri')
            #mri.inspect(result)
            #users = self._connection.getUsers()
            print("Connection.getUsers()2 %s" % users)
            return DataContainer(users, 'string')
        except Exception as e:
            print("Connection.getUsers(): %s - %s" % (e, traceback.print_exc()))

    # XGroupsSupplier
    def getGroups(self):
        return self._connection.getGroups()

    # XWarningsSupplier
    def getWarnings(self):
        warning = self._connection.getWarnings()
        return warning
    def clearWarnings(self):
        self._connection.clearWarnings()

    # XConnection
    def createStatement(self):
        return Statement(self)
    def prepareStatement(self, sql):
        print("Connection.prepareStatement() %s" % sql)
        return PreparedStatement(self, sql)
    def prepareCall(self, sql):
        print("Connection.prepareCall() %s" % sql)
        return CallableStatement(self, sql)
    def nativeSQL(self, sql):
        return self._connection.nativeSQL(sql)
    def setAutoCommit(self, auto):
        self._connection.setAutoCommit(auto)
    def getAutoCommit(self):
        return self._connection.getAutoCommit()
    def commit(self):
        self._connection.commit()
    def rollback(self):
        self._connection.rollback()
    def isClosed(self):
        return self._connection.isClosed()
    def getMetaData(self):
        metadata = self._connection.getMetaData()
        return DatabaseMetaData(self, metadata, self._protocols, self._username)
    def setReadOnly(self, readonly):
        self._connection.setReadOnly(readonly)
    def isReadOnly(self):
        return self._connection.isReadOnly()
    def setCatalog(self, catalog):
        self._connection.setCatalog(catalog)
    def getCatalog(self):
        return self._connection.getCatalog()
    def setTransactionIsolation(self, level):
        self._connection.setTransactionIsolation(level)
    def getTransactionIsolation(self):
        return self._connection.getTransactionIsolation()
    def getTypeMap(self):
        return self._connection.getTypeMap()
    def setTypeMap(self, typemap):
        self._connection.setTypeMap(typemap)

    # XServiceInfo
    def supportsService(self, service):
        return self._connection.supportsService(service)
    def getImplementationName(self):
        return self._connection.getImplementationName()
    def getSupportedServiceNames(self):
        return self._connection.getSupportedServiceNames()


class DataContainer(unohelper.Base,
                    XWeak,
                    XAdapter,
                    XNameAccess,
                    XIndexAccess,
                    XEnumerationAccess):
    def __init__(self, names, typename):
        self._elements = {name: DataBaseUser(name) for name in names}
        self._typename = typename
        print("DataContainer.__init__()")

    # XWeak
    def queryAdapter(self):
        print("DataContainer.queryAdapter()")
        return self
    # XAdapter
    def queryAdapted(self):
        print("DataContainer.queryAdapter()")
        return self
    def addReference(self, reference):
        pass
    def removeReference(self, reference):
        pass

    # XNameAccess
    def getByName(self, name):
        print("DataContainer.getByName() %s" % name)
        return self._elements[name]
    def getElementNames(self):
        elements = tuple(self._elements.keys())
        print("DataContainer.getElementNames() %s" % (elements, ))
        return elements
    def hasByName(self, name):
        print("DataContainer.hasByName() %s" % name)
        return name in self._elements

    # XIndexAccess
    def getCount(self):
        print("DataContainer.getCount()")
        return len(self._elements)
    def getByIndex(self, index):
        print("DataContainer.getByIndex() %s" % index)
        return None

    # XEnumerationAccess
    def createEnumeration(self):
        print("DataContainer.createEnumeration()")

    # XElementAccess
    def getElementType(self):
        print("DataContainer.getElementType()")
        return uno.getTypeByName(self._typename)
    def hasElements(self):
        print("DataContainer.hasElements()")
        return len(self._elements) != 0


class DataBaseUser(unohelper.Base,
                   XUser,
                   XWeak,
                   XAdapter,
                   XGroupsSupplier,
                   PropertySet):
    def __init__(self, username):
        self.Name = username
        print("DataBaseUser.__init__()")

    # XWeak
    def queryAdapter(self):
        print("DataBaseUser.queryAdapter()")
        return self
    # XAdapter
    def queryAdapted(self):
        print("DataBaseUser.queryAdapted()")
        return self
    def addReference(self, reference):
        pass
    def removeReference(self, reference):
        pass

    # XUser, 
    def changePassword(self, oldpwd, newpwd):
        print("DataBaseUser.changePassword()")
        pass
    def getPrivileges(self, objname, objtype):
        print("DataBaseUser.getPrivileges()")
        pass
    def getGrantablePrivileges(self, objname, objtype):
        print("DataBaseUser.getGrantablePrivileges()")
        pass
    def grantPrivileges(self, objname, objtype, objprivilege):
        print("DataBaseUser.grantPrivileges()")
        pass
    def revokePrivileges(self, objname, objtype, objprivilege):
        print("DataBaseUser.revokePrivileges()")
        pass

    # XGroupsSupplier
    def getGroups(self):
        print("DataBaseUser.getGroups()")
        return None

    # XPropertySet
    def _getPropertySetInfo(self):
        properties = {}
        properties['Name'] = getProperty('Name', 'string', READONLY)
        return properties