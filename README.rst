uTest TCM Platform
==================

Documentation of setup (Tested on Ubuntu 10.10 desktop, 10.04 server, and Mac
OS X).

  * Build: Get Java and Maven installed and build the tcmplatform source
  * Run: Setup JBoss, and copy the tcmplatform .war file to the right place
  * Cheats: .bashrc settings to make re-building and running easier once
             you're already setup

Assumes that $TCMPLATFORM is the root directory of the checked-out tcmplatform
repo (see Cheats section).

Build
-----

First make sure we've got a JDK and Maven both available. On Ubuntu::

    $ sudo aptitude install maven2 openjdk-6-jdk

On OS X, follow http://maven.apache.org/download.html. You may need to add
something like this to your .bashrc::

    export JAVA_HOME="/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home"

Now build the project::

    $ cd $TCMPLATFORM; mvn clean install

This should generate the file ``utest-portal-webapp/target/tcm.war``. In the
next step, we'll install this into jBoss.


Run
---

Download and unzip jBoss 5.1::

    $ wget -O jboss-5.1.0.GA.zip http://sourceforge.net/projects/jboss/files/JBoss/JBoss-5.1.0.GA/jboss-5.1.0.GA.zip/download
    $ unzip jboss-5.1.GA.zip

Copy the .war file in::

    $ cp $TCMPLATFORM/utest-portal-webapp/target/tcm.war jboss-5.1.0.GA/server/default/deploy/

Add the MySQL connector jar and the datasource configuration into jBoss::

    $ cp $TCMPLATFORM/deploy-config/mysql-connector-java-5.1.12.jar jboss-5.1.0.GA/server/default/lib/
    $ cp $TCMPLATFORM/deploy-config/utest-ds.xml jboss-5.1.0.GA/server/default/deploy/

Edit the copied utest-ds.xml file (the one under
``jboss-5.1.0.GA/server/default/deploy/utest-ds.xml``) to set the name of your
MySQL database (default is ``tcm``) and your MySQL user and password (defaults
to ``root`` with no password).

Create your MySQL database schema (if you are using a MySQL user other than
root, you will probably need to comment out the ``GRANT ALL PRIVILEGES`` line
near the top of this SQL script)::

    $ mysql -uroot < $TCMPLATFORM/utest-persistence/src/main/resources/db_scripts/db_tcm_create_empty_db_script.sql

You'll need to also execute each database update script in that same directory, in order. For example::

    $ mysql -uroot < $TCMPLATFORM/utest-persistence/src/main/resources/db_scripts/db_tcm_update_db_script_1.sql

The shell script ``reset-mysql.sh`` automates setting up the initial schema and
running all update scripts. (You may need to modify this script if using a
database user other than root).

And run the server::

    $ jboss-5.1.0.GA/bin/run.sh

Now you should be able to connect to http://localhost:8080/tcm/services/ and
see the web-service WADL file links listed, and connect to
e.g. http://localhost:8080/tcm/services/v2/rest/companies/ and see the list of
companies.


Cheats
------

You can add these lines to your .bashrc to make updating and running a tad
easier.  Please modify the environment variables to match your system config::

    export TCMPLATFORM=$HOME/gitspace/tcmplatform
    export M2_HOME=/usr/local/apache-maven/apache-maven-2.2.1
    export M2=$M2_HOME/bin
    export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.6.0/Home
    export JBOSS_HOME=$HOME/CodeLibraries/jboss-5.1.0.GA
    export PATH=$M2:$PATH

    # TCM
    function tcmupdate() {
        cd $TCMPLATFORM
        mvn clean install
        cp $TCMPLATFORM/utest-portal-webapp/target/tcm.war $JBOSS_HOME/server/default/deploy/
        echo "DONE: tcm.war copied to JBoss"; echo
    }
    function tcmrun() {
        $JBOSS_HOME/bin/run.sh
    }
