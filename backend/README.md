# Dockerize

```
eval $(minikube docker-env)
sbt docker
```

# Deploy

```
kubectl create -f k8s.yml
kubectl create -f profanity.yml 
kubectl replace -f profanity.yml 
kubectl scale --replicas=2 -f profanity.yaml
```

# Update
``` 
kubectl set image deployment/backend frontend=codepitbull/backend:v2
```