# Dockerize

```
eval $(minikube docker-env)
sbt docker
```

# Deploy

```
kubectl create -f hazelcast.yaml
kubectl create -f k8s.yml
kubectl expose -f k8s.yml
kubectl create -f ingress.yml
```

# Update
``` 
kubectl set image deployment/frontend frontend=codepitbull/frontend:v2
```