package org.db;

import java.sql.SQLException;
import org.h2.tools.Server;
public class DBLauncher {
    public static void main(String[] args) throws SQLException {
        Server.main();
        //jdbc:h2:C:\Users\mea\IdeaProjects\reflection\db-files\db
        System.out.println("DB Launched");

    }
}
