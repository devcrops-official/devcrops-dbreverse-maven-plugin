# DEVCROPS DB REVERSE PLUGIN

Maven plugin that allows you to make a database dbreverse and version it on a git repository.

**Installation**
-
To install the plugin, just run the command in the root project directory.

```bash
$ mvn install
```

This will allow the plugin to be installed locally.


**Goal Overview**
-
### **devcrops-dbreverse:reverse-db**
```bash
$ mvn devcrops-dbreverse:reverse-db
```
Goal to recover all DDLs of a DB.

| Parametro      | Required | Default | Description                                                                                                                          |
|----------------|--------------|---------|-------------------------------------------------------------------------------------------------------------------------------------|
| **dbConnection** | *YES*  | - | The jdbc url to use to connect to the database |
| **dbDriver** | *YES*  | - | The fully qualified classname of the jdbc driver to use to connect to the database |
| **dbSchema** | *YES*  | - | The schema of the db. |
| **dbUser** | *YES*  | - | The user to use to connect to the database |
| **dbPassword** | *YES*  | - | The password to use to connect to the database |
| **dbOutputDirectory** | *NO*  | schema_name | The output directory where the DDLs script will be written |
| **gitPush** | *NO*  | false | If you are inside a git repository you can Push automatically. |
| **gitPushMessage** | *NO*  | Commit modifiche DDL oggetti DB | Commit Message |

