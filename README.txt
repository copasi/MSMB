MultiState Model Builder (MSMB)

!!! WARNING !!!
Compiling only on WIN64 because the updated Copasi language bindings for the other systems has not be distributed yet 
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

This will create a directory MSMB_dist and copy the appropriate (w.r.t OS/architecture) Copasi library in the libs folder.

To run the generate executable jar, from the directory "MSMB_dist", use the command:

java -jar MSMB.jar

or double click on the MSMB.jar file


