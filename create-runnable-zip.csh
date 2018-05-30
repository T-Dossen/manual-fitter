#!/bin/csh -f

rm -rf manfit-app manfit-app.zip

mvn install
mvn clean compile assembly:single
mkdir manfit-app
cp target/manfit-1.0-SNAPSHOT-jar-with-dependencies.jar manfit-app
chmod +x manfit-app/manfit-1.0-SNAPSHOT-jar-with-dependencies.jar
mv manfit-app/manfit-1.0-SNAPSHOT-jar-with-dependencies.jar manfit-app/manfit.jar
cp -r data manfit-app
zip -r manfit-app.zip manfit-app
rm -rf manfit-app

echo "unzip manfit-app.zip ; cd manfit-app ; java -jar manfit.jar"
