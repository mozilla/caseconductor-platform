uTest TCM Platform
==================

Documentation of setup on an Ubuntu 10.10 (Maverick) desktop system.

Assumes that $TCMPLATFORM is the root directory of the checked-out tcmplatform
repo.

Build
-----

First make sure we've got a JDK and Maven both available::

    $ sudo aptitude install maven2 openjdk-6-jdk

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

    $ mysql < $TCMPLATFORM/utest-persistence/src/main/resources/db_scripts/db_tcm_create_empty_db_script.sql

And run the server::

    $ jboss-5.1.0.GA/bin/run.sh

Now you should be able to connect to http://localhost:8080/tcm/services/ and
see the web-service WADL file links listed, and connect to
e.g. http://localhost:8080/tcm/services/v2/rest/companies/ and see the list of
companies.
