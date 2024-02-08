echo "Building lightweight JAR"
scalac src/*.scala src/*/*.scala src/*/*/*.scala -d tanuki.jar
echo "Building Fat JAR"
scala-cli --power package src --assembly --preamble=false -o tanuki-java.jar
