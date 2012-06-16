#!/bin/sh

echo "************ UNDEPLOYING *******************"
asadmin undeploy osp_jsf
echo "************ BUILDING **********************"
mvn package
echo "************ DEPLOYING *********************"
asadmin deploy target/osp_jsf.war
