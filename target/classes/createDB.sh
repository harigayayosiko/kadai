mysql -u root -p <<EOF
begin;
source createDatabase.sql
commit;
EOF