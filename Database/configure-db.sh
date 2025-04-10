#!/bin/bash
DBSTATUS=1
ERRCODE=1
i=0
while [[ $DBSTATUS -ne 0 ]] && [[ $i -lt 60 ]] && [[ $ERRCODE -ne 0 ]]; do
	i=$i+1
	DBSTATUS=$(/opt/mssql-tools18/bin/sqlcmd -h -1 -t 1 -U sa -P $MSSQL_SA_PASSWORD -C -Q "SET NOCOUNT ON; Select SUM(state) from sys.databases");
	ERRCODE=$?
	echo "Database not ready yet"
	sleep 1
done
if [ $DBSTATUS -ne 0 ] OR [ $ERRCODE -ne 0 ]; then 
	echo "SQL Server took more than 60 seconds to start up or one or more databases are not in an ONLINE state"
	exit 1
fi
/opt/mssql-tools18/bin/sqlcmd -U sa -P $MSSQL_SA_PASSWORD -C -i OPM.sql
echo "Import default data complete"
