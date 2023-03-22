stage:
	docker run -it -v `pwd`:/src -w /src sbtscala/scala-sbt:eclipse-temurin-11.0.17_8_1.8.2_2.13.10 sbt Docker/stage

build: stage
	docker build -t cloudsql-pg-iam-fail-repro-repro:latest service/target/docker/stage/
