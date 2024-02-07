"Building lightweight JAR"
scalac src/*.scala src/*/*.scala -d tanuki.jar
"Building Fat JAR"
scala-cli --power package src --assembly --preamble=false -o tanuki-fat.jar
