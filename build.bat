cd messaging-utilities-3.3
call build.bat
cd..

cd token-service
call build.bat
cd ..

cd account-service
call build.bat
cd ..

cd payment-service
call build.bat
cd ..

cd dtu-pay-service
call build.bat
cd ..

timeout /t 5