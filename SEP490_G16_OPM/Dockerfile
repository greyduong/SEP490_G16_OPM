FROM netbeans AS build
WORKDIR /app
COPY . /app
RUN ant -propertyfile /opt/netbeans/build.properties dist
FROM tomcat
COPY --from=build ./app/dist/SEP490_G16_OPM.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
