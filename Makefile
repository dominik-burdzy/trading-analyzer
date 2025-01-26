# Makefile for easier handling common tasks
MVN = ./mvnw

lint:
	$(MVN) antrun:run@ktlint

lint.format:
	$(MVN) antrun:run@ktlint-format

dist: install

install:
	$(MVN) clean install

start:
	$(MVN) spring-boot:run

test:
	$(MVN) test

clean:
	$(MVN) clean
