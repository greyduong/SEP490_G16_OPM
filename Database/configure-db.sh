#!/bin/bash
for i in {1..50};
do
	/opt/mssql-tools18/bin/sqlcmd -U sa -P $MSSQL_SA_PASSWORD -C -i OPM.sql
	if [ $? -eq 0 ]
	then
		echo "OPM.sql run completed"
		break
	else
		echo "Database not ready yet..."
		sleep 1
	fi
done
