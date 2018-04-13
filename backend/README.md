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
```

# Update
``` 
kubectl set image deployment/backend frontend=codepitbull/backend:v2
```