call mvn install:install-file -Dfile=%~dp0\jogl-cg.jar -DgroupId=org.jogamp.jogl -DartifactId=jogl-cg -Dversion=2.3.2 -Dpackaging=jar
call mvn install:install-file -Dfile=%~dp0\jogl-util-gldesktop.jar -DgroupId=org.jogamp.jogl -DartifactId=jogl-util-gldesktop -Dversion=2.3.2 -Dpackaging=jar
call mvn install:install-file -Dfile=%~dp0\jogl-glu-gldesktop.jar -DgroupId=org.jogamp.jogl -DartifactId=jogl-glu-gldesktop -Dversion=2.3.2 -Dpackaging=jar
call mvn install:install-file -Dfile=%~dp0\jogl-glu.jar -DgroupId=org.jogamp.jogl -DartifactId=jogl-glu -Dversion=2.3.2 -Dpackaging=jar
call mvn install:install-file -Dfile=%~dp0\jogl-util-cdc.jar -DgroupId=org.jogamp.jogl -DartifactId=jogl-util-cdc -Dversion=2.3.2 -Dpackaging=jar
call mvn install:install-file -Dfile=%~dp0\simple-xml-1.1.1.jar -DgroupId=simple -DartifactId=xml -Dversion=1.1.1 -Dpackaging=jar
call mvn install:install-file -Dfile=%~dp0\vecmath.jar -DgroupId=javax -DartifactId=vecmath -Dversion=1 -Dpackaging=jar
call mvn install:install-file -Dfile=%~dp0\trove.jar -DgroupId=gnu -DartifactId=trove -Dversion=1 -Dpackaging=jar
call mvn install:install-file -Dfile=%~dp0\newt-all.jar -DgroupId=newt -DartifactId=newt-all -Dversion=1 -Dpackaging=jar