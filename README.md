minikube start --memory 4048 --cpus 4
minikube addons enable ingress

kubectl config use-context minikube
eval $(minikube docker-env)  

kubectl delete pods <pod> --grace-period=0 --force

kubectl exec -it shell-demo -- /bin/bash
