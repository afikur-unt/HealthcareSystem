#!/bin/bash

# Configuration
DB_NAME="HealthcareSystem"
DB_USER="your_username"
BACKUP_FILE="/path/to/backups/healthcare_db_backup.sql"

# Drop and recreate database
psql -U "$DB_USER" -c "DROP DATABASE IF EXISTS $DB_NAME;"
psql -U "$DB_USER" -c "CREATE DATABASE $DB_NAME;"

# Restore database
psql -U "$DB_USER" -d "$DB_NAME" -f "$BACKUP_FILE"

if [ $? -eq 0 ]; then
    echo "Restore successful"
else
    echo "Restore failed"
    exit 1
fi