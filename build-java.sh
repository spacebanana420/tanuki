echo "Building Tanuki"
scala-cli --power package src -f --assembly --preamble=false -o build/tanuki-java.jar
