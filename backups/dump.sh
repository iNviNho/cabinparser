#!/bin/bash

# Variables
DB_NAME="cabin_parser"
DB_USER="root"
BACKUP_FILE="backup_$(date +%Y%m%d).dump"

echo "Creating a backup..."
PGPASSWORD="root" pg_dump -U "$DB_USER" -h localhost -d "$DB_NAME" -F c -f "$BACKUP_FILE"
echo "BACKUP DONE"