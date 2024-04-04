# cns-catalog-service
클라우드 네이티브 스프링 인 액션 - 카탈로그 서비스

## Database Commands
Start an interactive PSQL console:

```bash
docker exec -it polar-postgres psql -U user -d polardb_catalog
```

| PSQL Command | Description |
|---|---|
|\list|	List all databases.
|\connect| polardb_catalog	Connect to specific database.
|\dt|	List all tables.
|\d| book	Show the book table schema.
|\d| flyway_schema_history	Show the flyway_schema_history table schema.
|\quit|	Quit interactive psql console.

From within the PSQL console, you can also fetch all the data stored in the book table.
```sql
select * from "book";
```

The following query is to fetch all the data stored in the flyway_schema_history table.
```sql
select * from "flyway_schema_history";
```
