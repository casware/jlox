JAVAC=javac
SRC_DIR=src/com/craftinginterpeters/lox
OUTPUT_DIR=bin
FIND := /usr/bin/find
MKDIR := /bin/mkdir
JVM=java
make-directories := $(shell $(MKDIR) $(OUTPUT_DIR))
all_javas := $(OUTPUT_DIR)/all.javas
class_path := $(OUTPUT_DIR)
JFLAGS=-g \
		-d $(OUTPUT_DIR) \
		-cp $(class_path)

JVMFLAGS = -cp $(class_path)

.PHONY: compile
compile: $(all_javas)
	$(JAVAC) $(JFLAGS) @$<

# all_javas - gather source file list
.INTERMEDIATE: $(all_javas)
$(all_javas):
	$(FIND) $(SRC_DIR) -name '*.java' > $@

.PHONY: clean
clean:
	rm -rf $(OUTPUT_DIR)

# Run
.PHONY: run
run:
	$(JVM) $(JVMFLAGS) Scanner