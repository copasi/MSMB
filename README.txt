MultiState Model Builder (MSMB)

!!! WARNING !!!
Compiling only on WIN64 because the updated Copasi language bindings for the other systems has not be distributed yet 
(some methods connected to the CEvent class were missing, Ralph corrected the bug and generated the Win64 language bindings for me. The correct language bindings will be present in the next Copasi distribution but right now I don't have them available for testing)
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

----
System requirements:
- JDK (1.7) 	(make sure that $JAVA_HOME is pointing at this JDK)
- ANT (1.8)	
- Ant-contrib package (1.0b3) (saved in $ANT_HOME/lib) 

The ant-contrib package can be found at: http://sourceforge.net/projects/ant-contrib/files/
----

To compile use the command:

ant -f buildRunnableJar.xml

This will create a directory MultiStateModelBuilder_dist with the appropriate Copasi library (according to the different OS/architecture).

To run the generate executable jar, from the directory "MultiStateModelBuilder_dist", use the command:

java -jar MultiStateModelBuilder.jar



