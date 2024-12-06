
build: MontgomeryCrashesDB.class

MontgomeryCrashesDB.class: MontgomeryCrashesDB.java
	javac MontgomeryCrashesDB.java

run: MontgomeryCrashesDB.class
	java -cp .:mssql-jdbc-11.2.0.jre11.jar MontgomeryCrashesDB

clean:
	rm MontgomeryCrashesDB.class
