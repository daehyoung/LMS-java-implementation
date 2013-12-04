README For Libris Library Management System




Overview:

This file contains the installation instructions for the Libris Library Management System.


Note: you must have the Java Runtime installed in order to run the Libris client or server 
program.  You may download the latest version of the Java Runtime here:
http://www.java.com




Libris Client:

Installation Instructions for the Libris Client Program
1. Copy the installation folder onto your computer's hard drive.


How To Run the Libris Client Program
1. Open the operating system’s command prompt and navigate to the installation
directory.
2. Run the following command: javaw -jar libris_client.jar




Libris Server:

Installation Instructions for Libris Server Program
1. Ensure MySQL Server version 5.5 or greater is installed.
        Included is MySQL Server 5.5 for windows.
2. Either:
 a) Pipe the build_librisdb.sql file into mysql using the following command
                mysql -u “username” -p”password” < build_librisdb.sql
You may need to specify the path for the mysql command if it is not a part of the classpath.
 b) Copy and paste the contents of the build_librisdb.sql file into the mysql prompt
3. Run “java -jar libris_server.jar” for the first time. It will probably shut itself down but that is OK. It created a file under config/server.config to edit.
4) Open the file config/server.config and edit it to the desired settings. Namely the db_dbusername and db_password settings. There is a description of each setting in the Server Settings section below.
5) The server will create a default administrator account with a user ID of 1 and password “libris”. This account should be used to create administrator accounts and then be disabled.


How To Run the Libris Server Program
1. Open the operating system’s command prompt and navigate to the installation
directory.
2. Run the following command: java -jar libris_server.jar


Server Commands
        help - display a list of commands
        shutdown - shutdown the server application
        reset - reset the server application
        backup [destination] - backup the server’s database
        restore [source] - restore the given backup file. Exercise caution using this command as it can corrupt the current database if the source file is incorrect.


Server Settings
        listener_port - The port on which the server will listen for clients.
        db_url - The location and port that the database is accessible at.
        db_dbName - The name of the database. This should be kept at  librisDB.
        db_dbusername - The user name to which the server will access the database
        db_password - The corresponding password to the user name.
        email_host - The smtp host.of the server’s email.
        emal_port - The port for the smtp host.
        email_account - The account that the server should send emails from.
        email_password - The corresponding password to the email account
overdue_timeofdaytorun - The time of day that the overdue manager will run. The overdue manager sends out emails for overdue and almost due emails. The time is in milliseconds from 12am.
overdue_periodbetweenemails - The time between when another email should be sent to the user with overdue resources.
overdue_timeuntilstopemails - The time until the server will no longer send emails to a user for an overdue resource.
overdue_timebeforeduetoemail - The time before a resource to due to warn the user.
reservation_timestorunaday - The number of times to run the reservation manager a day. The reservation manage sends notifications to users when their reservations are ready to be picked up.
reservation_periodbetweenemails - The time between sending another notification to the user about an available reserve.
fine_timeofdaytorun - The time of day to run the fine manager. The fine manager is responsible for incrementing the fines on overdue loans.