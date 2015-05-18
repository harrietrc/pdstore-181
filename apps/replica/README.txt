The RMI system requires several components to be configured and active before applications can run.

* `java.rmi.registry.LocateRegistry.getRegistry()', used in the construction of a PDStoreServer, relies on the Java rmiregistry tool being currently running with a configured classpath. From the command line, the following command in the main binary directory (that is, above pdstore) suffices.
	$ rmiregistry -J-Djava.rmi.server.codebase=file:C:\Users\Jesse\Development\pdstore-i2\bin\


* ReplicaDemo, as the entry point, establishes a SecurityManager. If this is not set, the default manager will reject code that would in other circumstances function. Although from a security standpoint it is extremely flawed, it is sufficient to construct a policy file offering java.security.AllPermission to the codebase file:/- , then invoke ReplicaDemo with parameter -Djava.security.policy=fileloc