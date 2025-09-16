.PHONY: run test build docker docker-run

run:
	mvn spring-boot:run

test:
	mvn -q -DskipTests=false test

build:
	mvn -q -DskipTests clean package

docker:
	docker build -t sun-forecast .

docker-run:
	docker run -p 8080:8080 --rm -e OPENAI_API_KEY=$$OPENAI_API_KEY sun-forecast
