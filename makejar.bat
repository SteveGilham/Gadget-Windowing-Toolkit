del gwt.jar
del gwt.zip
del gwt.cab
mkdir bla
mv dtai java11 bla
copy TestGadget.class bla
cd bla
zip -rq0 ..\gwt.zip * -i *.class
copy ..\*.gif .
copy ..\*.jpg .
cabarc -r -s 6144 -p n ..\gwt.cab *.class *.gif *.jpg
cd ..
zigbert -k"DTAI, Incorporated's VeriSign Trust Network ID" -d"C:\Program Files\Netscape\Users\kadel" bla
cd bla
zip -rq9 ..\gwt.jar *
rm -rf META-INF
mv dtai java11 ..
del *.class
del *.gif
del *.jpg
cd ..
rmdir bla
