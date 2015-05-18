PDStore
=======

This is the PDStore project. PDStore is an advanced triplestore written in Java.
The development environments supported are IntelliJ and Eclipse.


Configuring IntelliJ
--------------------

The main settings to look at are in File -> Project Structure...

In the Project category the SDK needs to be set. In the case of plugins, this needs to be the IDEA SDK (not the default Java one).

If Scala is used, then you need to look at the Modules category, esp. at the Scala facet there. This is where you can choose a Scala compiler library.

The Scala library should be registered under Global Libraries; this is simply a matter of providing the path to the scala/lib folder, where all the Scala JAR files are.

There is a video about configuring IntelliJ for Scala here:
http://www.youtube.com/watch?v=0y4IOCAomdQ


Documentation
-------------

The most recent code documentation is contained in JavaDoc comments in the various files.
Important starting points are:
- PDCoreI and PDStoreI, the fundamental interfaces of PDStore
- pdstore.PDStore, the convenience class that one will mostly instantiate to use PDStore.
- PDSimpleWorkingCopy and PDWorkingCopy, the Interface and ConvenienceClass to work with
  the Data Access Layer.

Other important access ways to understanding PDStore are:
- The sample applications in the "apps" folder, in particular:
- The diagram editor, which exemplifies the fundamental tool architecture that all PDStore
  based tools should use.

- The views of PDStore. These are "applets" that have become so central that they are moved to
  the core pdstore package under pdstore.ui.
  Some of them can be used from sample code in the "examples" source folders
  The current views are:
  - The treeview, it can be started from:
        /pdstore/examples/book/ExampleTreeViewer.java
  - The Historyview, visualizing transactions and branches in PDStore
  - The Graphview to edit graphs of Instances in PDStore.
  
- The testcases in the source folder "test". 
  In particular the SPARQL Testcases show one of the most advances uses of PDStore as a database:
  /pdstore/test/pdstore/sparql/AllSparqlTests.java
  
- Data can be added to PDStore in a textual format:
  - PDL is a simple scripting language that can be processed with: 
        /pdstore/src/pdstore/scripting/Interpreter.java
  
There is also a collection of short tutorials and other documents in the "Documentation" folder.


Package Structure
-----------------

The core PDStore packages are:
pdstore
pdstore.generic
pdstore.changelog
pdstore.changeindex
pdstore.concurrent
pdstore.notify
pdstore.sparql
nz.ac.auckland.se.genoupe.tools
and tests in test.pdstore.AllTests.java
and test.pdstore.sparql.AllSparqlTests.java
