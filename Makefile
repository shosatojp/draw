all:
	mvn package
	jar uf target/*-jar-with-dependencies.jar src/ pom.xml