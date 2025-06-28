#!/bin/bash

# Variables
DB_NAME="cabin_parser"
DB_USER="root"
BACKUP_FILE="backup_20250628.dump"

PGPASSWORD="root" createdb -U root -h localhost cabin_parser_dump

echo "Restoring from backup..."
PGPASSWORD="root" pg_restore -U root -h localhost -d cabin_parser_dump backup_20250628.dump
echo "RESTORING DONE"