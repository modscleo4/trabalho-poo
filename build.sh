cp src/main/java/* -r bin/main
cp src/main/resources/* -r bin/main
cp src/Server/* -r bin/Server
cd bin/main/
javac Main.java
java Main
