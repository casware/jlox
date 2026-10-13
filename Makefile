JAVAC				:=	javac
SRC_DIR				:=	src/com/craftinginterpreters/lox
OUTPUT_DIR			:=	bin
FIND 				:= 	/usr/bin/find
MKDIR 				:= 	/bin/mkdir
JVM					:=	java
all_javas 			:= 	$(OUTPUT_DIR)/all.javas
class_path 			:= 	$(OUTPUT_DIR)
SCANNER_TEST_FILE 	:= 	test_scanner.txt
JFLAGS				:= 	-g \
						-d $(OUTPUT_DIR) \
						-cp $(class_path)
JVMFLAGS 			:= 	-cp $(class_path)

.PHONY: compile
compile: $(all_javas)
	$(JAVAC) $(JFLAGS) @$<

# all_javas - gather source file list
.INTERMEDIATE: $(all_javas)
$(all_javas):
	$(MKDIR) -p $(OUTPUT_DIR)
	$(FIND) $(SRC_DIR) -name '*.java' > $@

.PHONY: clean
clean:
	rm -rf $(OUTPUT_DIR)

# Run
.PHONY: run
run:
	$(JVM) $(JVMFLAGS) com.craftinginterpreters.lox.Lox

# Run
.PHONY: run_file
run_file:
	$(JVM) $(JVMFLAGS) com.craftinginterpreters.lox.Lox test_scanner.txt

# Test scanner
.PHONY: test_scanner
test_scanner:
	$(JVM) $(JVMFLAGS) com.craftinginterpreters.lox.Lox $(SCANNER_TEST_FILE)

.PHONY: generate_ast
generate_ast:
	$(JVM) $(JVMFLAGS) src/com/craftinginterpreters/tool/GenerateAst.java .