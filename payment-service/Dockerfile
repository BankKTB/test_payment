FROM maven:3.6.3-slim AS m2builder
RUN mkdir -m 777 -p /usr/src/mymaven /root/.m2
WORKDIR /usr/src/mymaven

COPY pom.xml ./
COPY src/main/resources/idempiere/ ./src/main/resources/idempiere/
COPY src/main/resources/oracle/ ./src/main/resources/oracle/
COPY src/main/resources/jasper/ ./src/main/resources/jasper/
COPY src/main/resources/token/ ./src/main/resources/token/
RUN mvn install:install-file -Dfile=src/main/resources/idempiere/idempiere-wsclient.jar -DgroupId=org.idempiere -DartifactId=org.idempiere.webservice -Dversion=6.2.0-SNAPSHOT -Dpackaging=jar
RUN mvn install:install-file -Dfile=src/main/resources/oracle/ojdbc8-12.2.0.1.jar -DgroupId=com.oracle -DartifactId=ojdbc -Dversion=12.2.0.1 -Dpackaging=jar
RUN mvn install:install-file -Dfile=src/main/resources/jasper/jthaipdf-r6.jar -DgroupId=com.yit.jasperreports -DartifactId=jthaipdf-r6 -Dversion=1.0.0 -Dpackaging=jar
RUN mvn install:install-file -Dfile=src/main/resources/token/validate-gfmis-token-1.0.3.jar -DgroupId=com.validate-gfmis-token -DartifactId=validate-gfmis-token -Dversion=1.0.3 -Dpackaging=jar

# Resolve dependency and skip compiling main class.
# Try not to invalidate all previous build layers if pom.xml and lib/ not change.
RUN mvn clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -rf target/
# Alternative.
# RUN mvn dependency:resolve

FROM m2builder AS builder
WORKDIR /usr/src/mymaven
COPY src/ ./src/
RUN mvn clean package -Dmaven.test.skip=true -B -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn

FROM openjdk:11
WORKDIR /app

# Create a user and group 'appadmin'
RUN groupadd -g 1003 appadmin \
    && useradd --no-create-home -u 1003 -g 1003 appadmin \
    && chown -R appadmin:appadmin /app
USER appadmin

COPY --from=builder --chown=appadmin:appadmin /usr/src/mymaven/target/*.jar /app/SNAPSHOT.jar
CMD ["java", "-jar", "SNAPSHOT.jar"]
