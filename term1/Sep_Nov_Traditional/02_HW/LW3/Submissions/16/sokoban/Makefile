SRC := src
OUT := out
LIBS := $(wildcard lib/*.jar)
SOURCES := $(wildcard $(SRC)/*.java)
CLASSPATH := $(OUT):$(subst $(eval) ,:,$(LIBS))

MAIN_CLASS := src.Main
JAR_NAME := myprogram.jar
ARGS ?=

compile:
	mkdir -p $(OUT)
	@echo "Using libs: $(LIBS)"
	javac -cp "$(CLASSPATH)" -d $(OUT) $(SOURCES)

run:

	java -cp "$(CLASSPATH)" $(MAIN_CLASS) $(ARGS)

jar: compile
	mkdir -p mp
	@echo "Main-Class: $(MAIN_CLASS)" > $(OUT)/MANIFEST.MF
	cp -r $(OUT)/* tmp/
	for lib in $(LIBS); do \
	    unzip -oq "$$lib" -d tmp; \
	done
	jar cfm $(JAR_NAME) $(OUT)/MANIFEST.MF -C tmp .
	rm -rf tmp
	@echo "Created fat JAR: $(JAR_NAME)"

clean:
	rm -rf $(OUT)/* $(JAR_NAME)

.PHONY: all compile clean 
