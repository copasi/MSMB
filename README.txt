MultiState Model Builder (MSMB)

!!! WARNING !!!
Compiling only on WIN64 because the updated Copasi language bindings for the other systems has not be distributed yet 
(some methods connected to the CEvent class were missing, Ralph corrected the bug and generated the Win64 language bindings for me. The correct language bindings will be present in the next Copasi distribution but right now I don't have them available for testing)
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


To compile use the command:

ant -f buildRunnableJar.xml

This will create a directory MultiStateModelBuilder_dist with the appropriate Copasi library (according to the different OS/architecture).

To run the generate executable jar, from the directory "MultiStateModelBuilder_dist", use the command:

java -jar MultiStateModelBuilder.jar

