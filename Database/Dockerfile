FROM mcr.microsoft.com/mssql/server:2022-latest
ENV ACCEPT_EULA=Y
ENV MSSQL_SA_PASSWORD=OnlinePigMarket@1
WORKDIR /app
COPY --chmod=0755 . /app
EXPOSE 1433
ENTRYPOINT /bin/bash entrypoint.sh
