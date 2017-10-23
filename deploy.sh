#!/bin/sh

# Inspired from https://github.com/circleci/go-ecs-ecr
# more bash-friendly output for jq
JQ="jq --raw-output --exit-status"

configure_aws_cli(){
	aws --version
	aws configure set default.region eu-west-2
	aws configure set default.output json
}

deploy_cluster() {

    family="meslocationsvacances-webapp-task"

    make_task_def
    register_definition
    if [[ $(aws ecs update-service --cluster meslocationsvacances-cluster --service meslocationsvacances-service --task-definition $revision | \
                   $JQ '.service.taskDefinition') != $revision ]]; then
        echo "Error updating service."
        return 1
    fi

    # wait for older revisions to disappear
    for attempt in $(seq 1 30); do
        if stale=$(aws ecs describe-services --cluster meslocationsvacances-cluster --services meslocationsvacances-service | \
                       $JQ ".services[0].deployments | .[] | select(.taskDefinition != \"$revision\") | .taskDefinition"); then
            echo "Waiting for stale deployments:"
            echo "$stale"
            sleep 5
        else
            echo "Deployed!"
            return 0
        fi
    done
    echo "Service update took too long."
    return 1
}

make_task_def(){
	task_template='[
		{
			"name": "meslocationsvacances-container",
			"image": "%s.dkr.ecr.eu-west-2.amazonaws.com/meslocationsvacances:%s",
			"essential": true,
			"memory": 300,
			"cpu": 0,
			"portMappings": [
				{
					"containerPort": 8080,
					"hostPort": 80,
			                "protocol": "tcp"
				}
			]
		}
	]'
	
	task_def=$(printf "$task_template" $AWS_ACCOUNT_ID $CIRCLE_SHA1)
}

push_ecr_image(){
	eval $(aws ecr get-login --no-include-email --region eu-west-2)
	docker push $AWS_ACCOUNT_ID.dkr.ecr.eu-west-2.amazonaws.com/meslocationsvacances:$CIRCLE_SHA1
}

register_definition() {

    if revision=$(aws ecs register-task-definition --container-definitions "$task_def" --family $family | $JQ '.taskDefinition.taskDefinitionArn'); then
        echo "Revision: $revision"
    else
        echo "Failed to register task definition"
        return 1
    fi

}

configure_aws_cli
push_ecr_image
deploy_cluster
