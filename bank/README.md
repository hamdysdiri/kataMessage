# kataMessage

To avoid the complexities of manual installation and configuration, we can run IBM MQ inside a Docker container. We can use the following command to run the container with a basic configuration:

docker run -d --name my-mq -e LICENSE=accept -e MQ_QMGR_NAME=QM1 MQ_QUEUE_NAME=QUEUE1 -p 1414:1414 -p 9443:9443 ibmcom/mq
