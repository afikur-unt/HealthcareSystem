#!/bin/bash

# Configuration
DB_NAME="HealthcareSystem"
DB_USER="root"
BACKUP_DIR="/path/to/backups"
BACKUP_FILE="$BACKUP_DIR/healthcare_db_$(date +%Y%m%d_%H%M%S).sql"

# Create backup directory if it doesn't exist
mkdir -p "$BACKUP_DIR"

# Perform backup
pg_dump -U "$DB_USER" -d "$DB_NAME" -F p -f "$BACKUP_FILE"

if [ $? -eq 0 ]; then
    echo "Backup successful: $BACKUP_FILE"
else
    echo "Backup failed"
    exit 1
fi
