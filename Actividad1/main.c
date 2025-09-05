#include <stdio.h>
#include <stdlib.h>
#include <mysql.h>

/* Config de conexión */
#define DB_HOST "10.101.0.12"
#define DB_USER "195309"
#define DB_PASSWORD "MundoCruel"
#define DB_NAME "PRIMER_EVALUACION"
#define DB_PORT 3306

int main(void)
{
  MYSQL *conn = NULL;
  MYSQL_RES *res = NULL;
  MYSQL_ROW row;

  printf("Iniciando conexión a MySQL...\n");

  conn = mysql_init(NULL);
  if (conn == NULL)
  {
    fprintf(stderr, "mysql_init() falló (memoria insuficiente)\n");
    return;
  }

  if (mysql_real_connect(conn, DB_HOST, DB_USER, DB_PASSWORD, DB_NAME, DB_PORT, NULL, 0) == NULL)
  {
    fprintf(stderr, "No se pudo conectar: %s\n", mysql_error(conn));
    mysql_close(conn);
    return;
  }

  printf("¡Conexión exitosa!\n");
  printf("Servidor: %s\n", DB_HOST);
  printf("Base de datos: %s\n", DB_NAME);

  // ACA PODEMOS MODIFICAR LA QUERY
  char *query = "CALL PRO_AGREGAR_CONSULTAR_ESTUDIANTE(195311,'','','C');";
  if (mysql_query(conn, query))
  {
    fprintf(stderr, "EL LLAMADO FALLO %s", mysql_error(conn));
    mysql_close(conn);
    return;
  }

  res = mysql_store_result(conn);

  if (res)
  {
    int num_fields = mysql_num_fields(res);
    while ((row = mysql_fetch_row(res)))
    {
      for (int i = 0; i < num_fields; i++)
      {
        printf("%s\t", row[i] ? row[i] : "NULL");
      }
      printf("\n");
    }
  }

  if (res != NULL)
  {
    mysql_free_result(res);
  }
  mysql_close(conn);
  printf("Conexión cerrada.\n");
  return 0;
}