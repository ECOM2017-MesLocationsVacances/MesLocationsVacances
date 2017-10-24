FROM metz/wildfly-mysql
ADD target/ROOT.war /opt/jboss/wildfly/standalone/deployments/
