echo "Building Tanuki"
scalac src/*.scala src/*/*.scala -d test.jar && echo "Running Tanuki" && scala test.jar && rm test.jar
