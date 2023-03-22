for https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory/issues/1174

```
make build
```

--> push cloudsql-pg-iam-fail-repro:latest to artifact reg


cloud run spec:
```
spec:
  template:
    metadata:
      annotations:
        autoscaling.knative.dev/minScale: '....'
        autoscaling.knative.dev/maxScale: '.......'
        run.googleapis.com/cpu-throttling: 'false'
        run.googleapis.com/startup-cpu-boost: 'true'
    spec:
      containerConcurrency: 80
      timeoutSeconds: 10
      serviceAccountName: ..........
      containers:
      - image: us-docker.pkg.dev/....../cloudsql-pg-iam-fail-repro:latest
        ports:
        - name: http1
          containerPort: 8080
        env:
        - name: JAVA_OPTS
          value: -XX:+UseG1GC -XX:+ScavengeBeforeFullGC -Xmx768M -Xms768M
        - name: APPLICATION_DB_CONNECTION_TYPE
          value: gcp
        - name: APPLICATION_DB_DB_NAME
          value: ......
        - name: APPLICATION_DB_USER
          value: ......SA name......
        - name: APPLICATION_DB_PORT
          value: ...........
        - name: APPLICATION_DB_CLOUD_SQL_INSTANCE
          value: project:region:db_name
        - name: APPLICATION_DB_HOST
        - name: APPLICATION_DB_AUTOSAVE
          value: conservative
        resources:
          limits:
            cpu: 2000m
            memory: 1532Mi
```


has been reproducing at a rate of 1/120 days or minsScale 120 instances for 24 hours
