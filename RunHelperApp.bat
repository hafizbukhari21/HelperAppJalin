@echo off
set /p arg1=Masukkan source : 
set /p type= Masukan 1 Source_date dan 2 data_key :
if %type%==1 (
    set /p arg2=Masukkan source date "yyyyMMdd" :
    set /p arg3=Masukkan Env 1 VIT dan 2 Staging :  
    set /a arg4=
)

if %type%==2 (
    set /a arg2=
    set /p arg3=Masukkan Env 1 VIT dan 2 Staging :  
    set /p arg4=Masukkan Datakey, [Kalo menggunakan 'source date' field ini boleh dikosongin] :
)


java -Xms512m -Xmx6048m -jar HelperApp.jar "%arg1%" "%arg2%" "%arg3%" "%arg4%"  
pause

