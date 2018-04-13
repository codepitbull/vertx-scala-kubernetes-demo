minikube start --memory 4048 --cpus 4
minikube addons enable ingress

kubectl config use-context minikube
eval $(minikube docker-env)  

# Create Hazelcast service
kubectl create -f kubernetes/hazelcast.yaml

# Create Deployment and scale
kubectl create -f kubernetes/frontend.yaml
kubectl scale --replicas=2 -f kubernetes/deployment.yaml
kubectl expose deployment/vertx-frontend

# Publish via Ingress  
kubectl create -f kubernetes/ingress.yaml

# Create config map
kubectl create configmap vertx-config --from-file=vertx=kubernetes/vertx.properties

# Update image
kubectl set image deployment/vertx-backend vertx-backend=codepitbull/vertx-java-kubernetes-backend:4


mvn clean package docker:build

kubectl delete pods <pod> --grace-period=0 --force

kubectl exec -it shell-demo -- /bin/bash


curl --insecure https://192.168.99.100/hello/world


# MQTT
kubectl create -f kubernetes/mqtt.yaml

kubectl expose deployment vertx-mqtt --type="LoadBalancer" --port=1883 --target-port=1883

minikube service vertx-mqtt



