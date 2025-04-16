FROM mcr.microsoft.com/mssql/server:2022-latest
ENV ACCEPT_EULA=Y
ENV MSSQL_SA_PASSWORD=OnlinePigMarket@1
USER root
RUN apt-get update && apt-get install -y dos2unix
WORKDIR /app
COPY . /app
RUN dos2unix *
EXPOSE 1433
ENTRYPOINT ["./entrypoint.sh"]
