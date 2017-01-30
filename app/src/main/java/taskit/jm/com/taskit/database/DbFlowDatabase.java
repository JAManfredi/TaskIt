package taskit.jm.com.taskit.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Jared12 on 1/29/17.
 */

@Database(name = DbFlowDatabase.NAME, version = DbFlowDatabase.VERSION)
public class DbFlowDatabase {
    public static final String NAME = "DbFlowDatabase";
    public static final int VERSION = 1;
}
