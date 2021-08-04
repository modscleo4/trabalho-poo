find . -type f -path "./main/*/*" -name "*.class" -type f
find . -type f -path "./main/*" -name "*.class" -type f
find . -type f -path "./Server/*" -name "*.class" -type f
find . -type f -path "./main/*/*" -name "*.class" -delete
find . -type f -path "./main/*" -name "*.class" -delete
find . -type f -path "./Server/*" -name "*.class" -delete
cd Server
javac Server.java
java Server

