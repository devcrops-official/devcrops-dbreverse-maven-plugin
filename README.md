# DEVCROPS DB REVERSE PLUGIN

Maven plugin that allows you to make a database dbreverse and version it on a git repository.

**Installation**
-
To install the plugin, just run the command in the root project directory.

```bash
$ mvn install
```

This will allow the plugin to be installed locally.

```

**Goal Overview**
-
### **devops-data-dbreverse:reverse-db**
```bash
$ mvn devops-data-dbreverse:reverse-db
```
Goal to recover all DDLs of a DB.

| Parametro      | Obbligatorio | Default |Descrizione                                                                                                                          |
|----------------|--------------|---------|-------------------------------------------------------------------------------------------------------------------------------------|
| **dbConnection** | *SI*  | - | Url del db con il quale ci si vuole connettere |
| **dbDriver** | *SI*  | - | Driver del db con il quale si vuole interagire |
| **dbSchema** | *SI*  | - | Schema del db al quale si vuole fare riferimento |
| **dbUser** | *SI*  | - | Username utenza db |
| **dbPassword** | *SI*  | - | Password utenza db |
| **dbOutputDirectory** | *NO*  | src/main/resources |La directory target sul quale verranno scritti i DDL ottenuti dal DB|
| **gitPush** | *NO*  | false | Abilita il push automatico del contenuto della outputDirectory sul git del progetto |
| **gitPushMessage** | *NO*  | Commit modifiche DDL oggetti DB | Testo del messaggio di commit |

