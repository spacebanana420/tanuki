scalac src/*.scala src/*/*.scala -d tanuki.jar
scala-cli --power package src --assembly --preamble=false -o tanuki-fat.jar
