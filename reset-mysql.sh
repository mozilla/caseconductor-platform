#!/bin/bash

tmpsql=`mktemp tmp.XXXXXX`
cat utest-persistence/src/main/resources/db_scripts/db_tcm_create_empty_db_script.sql | grep -v "GRANT ALL PRIVILEGES" > $tmpsql
cat utest-persistence/src/main/resources/db_scripts/db_tcm_update_db_script_?.sql >> $tmpsql
cat utest-persistence/src/main/resources/db_scripts/db_tcm_update_db_script_??.sql >> $tmpsql
mysql -uroot < $tmpsql
rm $tmpsql
