# Makefile for Sokoban Game Project
# Author: Kaloyan Gavrilov

# Java compiler
JC = javac

# Java runtime
JAVA = java

# Library path
LIB_PATH = lib/jline-terminal-3.30.6.jar

# Classpath
CLASSPATH = .:$(LIB_PATH)

# Source files
SOURCES = SokobanGame.java LevelParser.java

# Main class
MAIN_CLASS = SokobanGame

# Default target
all: compile

# Compile all Java files
compile:
	@echo "Compiling Sokoban Game..."
	$(JC) -cp $(CLASSPATH) $(SOURCES)
	@echo "Compilation complete!"

# Run the main game
run: compile
	@echo "Starting Sokoban Game..."
	$(JAVA) -cp $(CLASSPATH) $(MAIN_CLASS)

# Clean compiled class files
clean:
	@echo "Cleaning up class files..."
	rm -f *.class
	@echo "Clean complete!"

# Show help
help:
	@echo "Sokoban Game Makefile"
	@echo "Available targets:"
	@echo "  all      - Compile all Java files (default)"
	@echo "  compile  - Compile all Java files"
	@echo "  run      - Compile and run the main game"
	@echo "  clean    - Remove all compiled class files"
	@echo "  help     - Show this help message"

# Declare phony targets
.PHONY: all compile run test demo clean help