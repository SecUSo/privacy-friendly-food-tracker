package org.secuso.privacyfriendlyexample;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.secuso.privacyfriendlyfoodtracker.database.ApplicationDatabase;

import java.io.IOException;

public class ApplicationDatabaseTest {

        ApplicationDatabase applicationDatabase = null ;

        @Before
        public void createDb() {
            this.applicationDatabase =  ApplicationDatabase.getInstance(mContext);

        }

        @After
        public void closeDb() throws IOException {
            applicationDatabase.close();
        }

        @Test
        public void writeUserAndReadInList() throws Exception {
            User user = TestUtil.createUser(3);
            user.setName("george");
            mUserDao.insert(user);
            List<User> byName = mUserDao.findUsersByName("george");
            assertThat(byName.get(0), equalTo(user));
        }
    }


