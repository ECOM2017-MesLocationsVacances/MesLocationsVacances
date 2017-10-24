FROM metz/wildfly-mysql
ADD target/MesLocationsVacances.war /opt/jboss/wildfly/standalone/deployments/
