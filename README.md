# iot498

1. Install JDK, Maven, Eclipse JDT

2. Use the following command to setup an Eclipse project.

   ```
   mvn eclipse:eclipse -DdownloadSources -DdownloadJavadocs
   ```

3. Use the following command to build a package for deployment.

   ```
   mvn package
   ```

4. Test your server by running `server/bat` and then pointing your browser
   to [127.0.0.1:8080](http://127.0.0.1:8080).
