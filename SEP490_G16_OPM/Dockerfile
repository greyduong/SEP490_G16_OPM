FROM netbeans as build
WORKDIR /app
COPY . /app
RUN ant -propertyfile /opt/netbeans/build.properties dist
FROM tomcat
COPY --from=build ./app/dist/*.war /usr/local/tomcat/webapps/
EXPOSE 8080
