CREATE OR REPLACE FUNCTION "reset_sequence" (tablename text, columnname text, sequence_name text)
    RETURNS INTEGER AS

$body$
DECLARE
    retval  INTEGER;
BEGIN

    EXECUTE 'SELECT setval( ''' || sequence_name  || ''', ' || '(SELECT MAX(' || columnname ||
            ') FROM "' || tablename || '")' || '+1)' INTO retval;
    RETURN retval;
END;

$body$  LANGUAGE 'plpgsql';


SELECT table_name || '_' || column_name || '_seq',
       reset_sequence(table_name, column_name, table_name || '_' || column_name || '_seq')
FROM information_schema.columns where column_default like 'nextval%';