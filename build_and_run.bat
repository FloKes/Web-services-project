call build.bat

cd end-to-end-tests
call deploy.bat
timeout /t 10
call test.bat

cd ..

call docker image prune -f

pause