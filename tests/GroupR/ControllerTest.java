package GroupR;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Carries out tests for the controller class. Tests for the following are performed:
 * fileExist() - ensures an arbitrary number of fles exist
 * @author  hueblergw
 */
class ControllerTest {

    Controller controller;
    File[] files;

    @BeforeEach
    public void startUp() {

        //intialize controller
        controller = new Controller();

        //definte 3 new files
        files = new File[3];

        //create the new files
        try {
            for(int i = 0; i < files.length; i++) {
                files[i] = new File(Integer.toString(i));
                files[i].createNewFile();
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testFilesExist() {
        assertFalse(controller.filesExist(new File("fake file"), new File("not here")));
        assertTrue(controller.filesExist(files[0], files[1], files[2]));
    }

    @AfterEach
    void tearDown() {

        //delete all the files created
        for(int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

}