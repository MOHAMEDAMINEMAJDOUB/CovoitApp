@echo off
echo ========================================
echo Database Reset Script for BDCovoiturage
echo ========================================
echo.
echo WARNING: This will DELETE ALL DATA!
echo.
pause

echo Connecting to MySQL and resetting database...
mysql -u root -p -e "USE BDCovoiturage; SET FOREIGN_KEY_CHECKS = 0; DROP TABLE IF EXISTS reservation, trajet, voiture, conducteur_voitures, conducteurs, passagers, utilisateur_roles, utilisateurs, roles, refresh_token; SET FOREIGN_KEY_CHECKS = 1;"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Database reset successful!
    echo ========================================
    echo.
    echo Next steps:
    echo 1. Restart your backend server
    echo 2. Hibernate will auto-create all tables
    echo 3. Default roles will be initialized
    echo.
) else (
    echo.
    echo ========================================
    echo ERROR: Database reset failed!
    echo ========================================
    echo.
    echo If mysql command is not found:
    echo 1. Open MySQL Workbench
    echo 2. Run the SQL file: reset-database.sql
    echo.
)

pause
