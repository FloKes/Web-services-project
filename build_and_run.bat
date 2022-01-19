call build.bat

cd end-to-end-tests
call deploy.bat
timeout /t 5
call test.bat
timeout /t 5
call stop_service.bat


cd ..

call docker image prune -f

pause