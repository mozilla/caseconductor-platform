Case Conductor Platform
=======================

Documentation of setup (Tested on Ubuntu 10.10 desktop, 10.04 server, and Mac
OS X).

  * Build: Get Java and Maven installed and build the caseconductor-platform
    source
  * Run: Setup JBoss, and copy the caseconductor-platform .war file to the
    right place
  * Cheats: .bashrc settings to make re-building and running easier once
    you're already setup

Assumes that $CCPLATFORM is the root directory of the checked-out
caseconductor-platform repo (see Cheats section).

Build
-----

If you have a pre-built WAR file (the Case Conductor UI repo provides one),
you can skip this build step and go straight to the `Run` step.

First make sure we've got a JDK and Maven both available. On Ubuntu::

    $ sudo aptitude install maven2 openjdk-6-jdk

On OS X, follow http://maven.apache.org/download.html. You may need to add
something like this to your .bashrc::

    $ export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home"

Note that if built with Maven 3, the platform will fail to start in the
supported version of jBoss (5.1.0) due to a bug related to loading of XML
parsers. Maven 2 must be used instead.

Now build the project::

    $ cd $CCPLATFORM; mvn clean install

This should generate the file ``utest-portal-webapp/target/tcm.war``. In the
next step, we'll install this into jBoss.


Run
---

Download and unzip jBoss 5.1::

    $ wget -O jboss-5.1.0.GA.zip http://sourceforge.net/projects/jboss/files/JBoss/JBoss-5.1.0.GA/jboss-5.1.0.GA.zip/download
    $ unzip jboss-5.1.GA.zip

Copy the .war file in (source location will differ if you didn't built it
yourself but are using a pre-built .war)::

    $ cp $CCPLATFORM/utest-portal-webapp/target/tcm.war jboss-5.1.0.GA/server/default/deploy/

Add the MySQL connector jar and the datasource configuration into jBoss::

    $ cp $CCPLATFORM/deploy-config/mysql-connector-java-5.1.12.jar jboss-5.1.0.GA/server/default/lib/
    $ cp $CCPLATFORM/deploy-config/utest-ds.xml jboss-5.1.0.GA/server/default/deploy/

Edit the copied utest-ds.xml file (the one under
``jboss-5.1.0.GA/server/default/deploy/utest-ds.xml``) to set the name of your
MySQL database (default is ``tcm``) and your MySQL user and password (defaults
to ``root`` with no password).

Note that the platform currently supports MySQL 5.1 and MySQL 5.5.8 and prior releases -- due to known bugs in MySQL 5.5.<9 through 16> 
some operations will fail with foreign key constraint violations.

Create your MySQL database schema (if you are using a MySQL user other than
root, you will probably need to comment out the ``GRANT ALL PRIVILEGES`` line
near the top of this SQL script)::

    $ mysql -uroot < $CCPLATFORM/utest-persistence/src/main/resources/db_scripts/db_tcm_create_empty_db_script.sql

You'll need to also execute each database update script in that same directory, in order. For example::

    $ mysql -uroot < $CCPLATFORM/utest-persistence/src/main/resources/db_scripts/db_tcm_update_db_script_1.sql

The shell script ``reset-mysql.sh`` automates setting up the initial schema and
running all update scripts. (You may need to modify this script if using a
database user other than root).

There are 2 OPTIONAL scripts created for populating environment data related to Desktop and Mobile testing. 
You don't need to run these scripts if you have your own seed data source or you are interested in testing some other types of environments.

    db_tcm_seed_desktop_environments.sql - will populate Operating System, Operating System Version and Web Browser data and relationships between them;
    
    db_tcm_seed_mobile_environments.sql - will populate Mobile Manufacturers, Mobile Models and Mobile Operating System data and relationships between them


And run the server::

    $ jboss-5.1.0.GA/bin/run.sh

Give it a minute or two to start up - when it's ready you'll see a line in its console output that looks like this::

    17:50:59,453 INFO  [ServerImpl] JBoss (Microcontainer) [5.1.0.GA (build: SVNTag=JBoss_5_1_0_GA date=200905221053)] Started in 48s:247ms

Now you should be able to connect to http://localhost:8080/tcm/services/ and
see the web-service WADL file links listed, and connect to
e.g. http://localhost:8080/tcm/services/v2/rest/companies/ and see the list of
companies.

Future Updates
--------------

If you ``git pull`` future updates to the platform code, you'll need to rebuild it::

    $ cd $CCPLATFORM; mvn clean install

And copy the built .war file into your jBoss installation::

    $ cp $CCPLATFORM/utest-portal-webapp/target/tcm.war jboss-5.1.0.GA/server/default/deploy/

If any new database update scripts were included in the platform update, you'll need to run them::

    $ mysql -uroot < $CCPLATFORM/utest-persistence/src/main/resources/db_scripts/db_tcm_update_db_script_29.sql

Alternatively, you can just run ``reset-mysql.sh`` again, if you don't mind
losing any data in your local database and starting over with a fresh database.


Cheats
------

You can add these lines to your .bashrc to make updating and running a tad
easier.  Please modify the environment variables to match your system config::

    export CCPLATFORM=$HOME/gitspace/caseconductor-platform
    export M2_HOME=/usr/local/apache-maven/apache-maven-2.2.1
    export M2=$M2_HOME/bin
    export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home
    export JBOSS_HOME=$HOME/CodeLibraries/jboss-5.1.0.GA
    export PATH=$M2:$PATH

    # Case Conductor
    function ccupdate() {
        cd $CCPLATFORM
        mvn clean install
        cp $CCPLATFORM/utest-portal-webapp/target/tcm.war $JBOSS_HOME/server/default/deploy/
        echo "DONE: tcm.war copied to JBoss"; echo
    }
    function ccrun() {
        $JBOSS_HOME/bin/run.sh
    }
