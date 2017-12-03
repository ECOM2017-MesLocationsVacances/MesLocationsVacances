FROM antoine38660/wildfly10-mariadb
ADD target/ROOT.war /opt/jboss/wildfly/standalone/deployments/
