FROM mcr.microsoft.com/mssql/server:2022-latest
ENV TZ="Asia/Ho_Chi_Minh"
ENV ACCEPT_EULA=Y
ENV MSSQL_SA_PASSWORD=OnlinePigMarket@1
USER root
RUN apt-get update && apt-get install -y dos2unix
WORKDIR /app
COPY --chmod=0755 . /app
RUN dos2unix *
EXPOSE 1433
ENTRYPOINT ["./entrypoint.sh"]
