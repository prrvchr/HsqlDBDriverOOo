**Ce [document](https://prrvchr.github.io/HsqlDBDriverOOo/README_fr) en français.**

**The use of this software subjects you to our** [**Terms Of Use**](https://prrvchr.github.io/HsqlDBDriverOOo/HsqlDBDriverOOo/registration/TermsOfUse_en)

# version [0.0.4](https://prrvchr.github.io/HsqlDBDriverOOo#historical)

## Introduction:

**HsqlDBDriverOOo** is part of a [Suite](https://prrvchr.github.io/) of [LibreOffice](https://www.libreoffice.org/download/download/) and/or [OpenOffice](https://www.openoffice.org/download/index.html) extensions allowing to offer you innovative services in these office suites.  

This extension allows you to use the HsqlDB driver of your choice, with all its features, directly in Base.  
It supports all protocols natively managed by HsqlDB, namely: hsql://, hsqls://, http://, https://, mem://, file:// and res://.

Being free software I encourage you:
- To duplicate its [source code](https://github.com/prrvchr/HsqlDBDriverOOo/).
- To make changes, corrections, improvements.
- To open [issue](https://github.com/prrvchr/HsqlDBDriverOOo/issues/new) if needed.

In short, to participate in the development of this extension.  
Because it is together that we can make Free Software smarter.

## Requirement:

[HsqlDB](http://hsqldb.org/) is a database written in Java.  
The use of HsqlDB requires the installation and configuration within LibreOffice / OpenOffice of a **JRE version 1.8 minimum** (ie: Java version 8)  
I recommend [AdoptOpenJDK](https://adoptopenjdk.net/) as your Java installation source.

If you are using **LibreOffice on Linux**, then you are subject to [bug 139538](https://bugs.documentfoundation.org/show_bug.cgi?id=139538).  
To work around the problem, please uninstall the packages:
- libreoffice-sdbc-hsqldb
- libhsqldb1.8.0-java

If you still want to use the Embedded HsqlDB functionality provided by LibreOffice, then install the [HsqlDBembeddedOOo](https://prrvchr.github.io/HsqlDBembeddedOOo/) extension.  
OpenOffice and LibreOffice on Windows are not subject to this malfunction.

## Installation:

It seems important that the file was not renamed when it was downloaded.
If necessary, rename it before installing it.

- Install [HsqlDBDriverOOo.oxt](https://github.com/prrvchr/HsqlDBDriverOOo/raw/master/HsqlDBDriverOOo.oxt) extension version 0.0.4.

Restart LibreOffice / OpenOffice after installation.

## Use:

### How to create a new database:

In LibreOffice / OpenOffice go to File -> New -> Database...:

![HsqlDBDriverOOo screenshot 1](HsqlDBDriverOOo-1.png)

In step: Select database:
- select: Connect to an existing database
- choose: HsqlDB Driver
- click on button: Next

![HsqlDBDriverOOo screenshot 2](HsqlDBDriverOOo-2.png)

In step: Connection settings:

- for the protocol: **file://**
    - in Datasource URL put:
        - for **Linux**: file:///tmp/testdb;default_schema=true;shutdown=true;hsqldb.default_table_type=cached;get_column_name=false
        - for **Windows**: file:///c:/tmp/testdb;default_schema=true;shutdown=true;hsqldb.default_table_type=cached;get_column_name=false

- for the protocol: **hsql://**
    - In a terminal, go to a folder containing the hsqldb.jar archive and run:
        - for **Linux**: java -cp hsqldb.jar org.hsqldb.server.Server --database.0 file:///tmp/testdb --silent false
        - for **Windows**: java -cp hsqldb.jar org.hsqldb.server.Server --database.0 file:///c:/tmp/testdb --silent false
    - in Datasource URL put: hsql://localhost/;default_schema=true

- click on button: Next

![HsqlDBDriverOOo screenshot 3](HsqlDBDriverOOo-3.png)

In step: Set up user authentication:
- click on button: Test connection

![HsqlDBDriverOOo screenshot 4](HsqlDBDriverOOo-4.png)

If the connection was successful, you should see this dialog window:

![HsqlDBDriverOOo screenshot 5](HsqlDBDriverOOo-5.png)

Have fun...

### How to update the HsqlDB driver:

If you want to update the HsqlDB driver (hsqldb.jar) to a newer version, follow these steps:
- 1 - Make a copy (backup) of the folder containing your database.
- 2 - Start LibreOffice / OpenOffice and change the version of the HsqlDB driver in: Tools -> Options -> Base drivers -> HsqlDB driver, by a more recent version (If necessary, you must rename the jar file to hsqldb.jar so that it is taken into account).
- 3 - Restart LibreOffice / OpenOffice after changing the driver (hsqldb.jar).
- 4 - In Base, after opening your database, go to: Tools -> SQL and type the SQL command: `SHUTDOWN COMPACT` or `SHUTDOWN SCRIPT`.

Now your database is up to date.

## Has been tested with:

* OpenOffice 4.1.8 - Ubuntu 20.04 - LxQt 0.14.1

* OpenOffice 4.1.8 - Windows 7 SP1

* OpenOffice 4.1.8 - MacOS.High Sierra

* LibreOffice 7.0.4.2 - Ubuntu 20.04 - LxQt 0.14.1

* LibreOffice 6.4.4.2 - Windows 7 SP1

I encourage you in case of problem :-(  
to create an [issue](https://github.com/prrvchr/HsqlDBDriverOOo/issues/new)  
I will try to solve it ;-)

## Historical:

### Introduction:

This driver was written to work around certain problems inherent in the UNO implementation of the JDBC driver built into LibreOffice / OpenOffice, namely: 

- The inability to provide the path to the Java driver archive (hsqldb.jar) when loading the JDBC driver.

The only possible workaround for this problem is to put the driver's Java archive (hsqldb.jar) in the Java ClassPath, but the problem is that if the driver version is other than version 1.8, then Base can no longer open odb files: HsqlDB embedded database functionality is lost. This amounts to saying that you cannot use an HsqlDB driver other than version 1.8, which is now over 10 years old...

In order to take advantage of the latest features offered by HsqlDB, it was necessary to write a new driver.

This new driver is just a wrapper in Python around the UNO services provided by the faulty LibreOffice / OpenOffice JDBC driver: it loads when calling the `sdbc:hsqldb:*` protocol but uses the `jdbc:hsqldb:*` protocol internally to connect.

It also provides functionality that the JDBC driver implemented in LibreOffice / OpenOffice does not provide, namely:

- The management of rights and users in Base.
- The use of the SQL Array type in the queries.
- Everything we are ready to implement.

For now, only user management (read only) is available.

### What has been done for version 0.0.1:

- The writing of this driver was facilitated by a [discussion with Villeroy](https://forum.openoffice.org/en/forum/viewtopic.php?f=13&t=103912), on the OpenOffice forum, which I would like to thank, because knowledge is only worth if it is shared...

- Using the new version of HsqlDB 2.5.1.

- Many other fix...

### What has been done for version 0.0.2:

- Added a dialog box allowing to update the driver (hsqldb.jar) in: Tools -> Options -> Base drivers -> HsqlDB driver

- Many other fix...

### What has been done for version 0.0.3:

- I especially want to thank fredt at [hsqldb.org](http://hsqldb.org/) for:

    - His welcome for this project and his permission to use the HsqlDB logo in the extension.

    - Its involvement in the test phase which made it possible to produce this version 0.0.3.

    - The quality of its HsqlDB database.

- Now works with OpenOffice on Windows.

- An unsupported protocol now displays an accurate error.

- A non-parsable url now displays a precise error.

- Now correctly handles spaces in filenames and paths.

- Many other fix...

### What has been done for version 0.0.4:

- Modifying the [Driver](https://github.com/prrvchr/HsqlDBDriverOOo/blob/master/HsqlDBDriverOOo/Driver.py) in order to return an XConnection interface providing a DataSource service with its `URL` property having the `sbdc` protocol necessary for proper functioning.

- Modifying the [Statement](https://github.com/prrvchr/HsqlDBDriverOOo/blob/master/uno/lib/uno/sdbc/statement.py) wrapper in order to provide the properties and methods present in the `Statement`, `PreparedStatement` et `CallableStatement` services of JDBC (thanks to hanya for [MRI](https://github.com/hanya/MRI) which was of great help to me...)

- Writing the [ResultSet](https://github.com/prrvchr/HsqlDBDriverOOo/blob/master/uno/lib/uno/sdbc/resultset.py) wrapper so that its `getStatement` method returns the modified version of the [Statement](https://github.com/prrvchr/HsqlDBDriverOOo/blob/master/uno/lib/uno/sdbc/statement.py) service.

- Modifying the [Connection](https://github.com/prrvchr/HsqlDBDriverOOo/blob/master/uno/lib/uno/sdbc/connection.py) wrapper, in order:
    - To make it possible to reassign the DataSource service by its `setParent` method.
    - To make possible the use of the: `com.sun.star.sdb.RowSet` service, see the use of the `self._patched` property as a workaround.

- Many other fix...

### What remains to be done for version 0.0.4:

- Managing rights and users in Base in read and write mode.

- The use of the SQL Array type in the queries.

- Add new languages for internationalization...

- Anything welcome...
