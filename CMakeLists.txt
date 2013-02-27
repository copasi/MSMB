cmake_minimum_required (VERSION 2.8.6)
find_package(Java)
project(MSMB Java)

include(UseJava)
include(JarHelperMSMB.cmake)

# the java source files
set(SRC
	${MSMB_SOURCE_DIR}/src/msmb/gui/*.java
	${MSMB_SOURCE_DIR}/src/msmb/model/*.java
	${MSMB_SOURCE_DIR}/src/msmb/commonUtilities/*.java
	${MSMB_SOURCE_DIR}/src/msmb/commonUtilities/tables/*.java
	${MSMB_SOURCE_DIR}/src/msmb/debugTab/*.java
	${MSMB_SOURCE_DIR}/src/msmb/testing/*.java
	${MSMB_SOURCE_DIR}/src/msmb/utility/*.java
	${MSMB_SOURCE_DIR}/src/msmb/parsers/chemicalReaction/*.java
	${MSMB_SOURCE_DIR}/src/msmb/parsers/chemicalReaction/syntaxtree/*.java
	${MSMB_SOURCE_DIR}/src/msmb/parsers/chemicalReaction/visitor/*.java
	${MSMB_SOURCE_DIR}/src/msmb/parsers/mathExpression/*.java
	${MSMB_SOURCE_DIR}/src/msmb/parsers/mathExpression/syntaxtree/*.java
	${MSMB_SOURCE_DIR}/src/msmb/parsers/mathExpression/visitor/*.java
	${MSMB_SOURCE_DIR}/src/msmb/parsers/multistateSpecies/*.java
	${MSMB_SOURCE_DIR}/src/msmb/parsers/multistateSpecies/syntaxtree/*.java
	${MSMB_SOURCE_DIR}/src/msmb/parsers/multistateSpecies/visitor/*.java
)

file(GLOB MY_JARS "${MSMB_SOURCE_DIR}/libs/*.jar") 

#set(CMAKE_JAVA_INCLUDE_PATH 
#	${MSMB_SOURCE_DIR}/libs/*.jar
#)
	
set(CMAKE_JAVA_INCLUDE_PATH 
	${MY_JARS}
)
# create an executable jar
create_jar(MSMB ${SRC})
