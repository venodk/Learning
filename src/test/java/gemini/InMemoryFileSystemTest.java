package gemini;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryFileSystemTest {

    @Test
    void testCreateAndRead() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        String path = "/dir1/file1.txt";
        String content = "Hello, world!";
        fs.create(path, content);
        assertEquals(content, fs.read(path));
        fs.delete(path);
    }

    @Test
    void testCreateWithNestedDirectories() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        String path = "/dir1/dir2/dir3/file1.txt";
        String content = "Nested file";
        fs.create(path, content);
        assertEquals(content, fs.read(path));
        fs.delete(path);
    }

    @Test
    void testCreateFileAlreadyExists() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        String path = "/file1.txt";
        fs.create(path, "Content1");
        assertThrows(IllegalArgumentException.class, () -> fs.create(path, "Content2"));
        fs.delete(path);
    }

    @Test
    void testReadFileNotFound() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        assertThrows(IllegalArgumentException.class, () -> fs.read("/file1.txt"));
    }

    @Test
    void testUpdate() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        String path = "/dir1/file1.txt";
        String content1 = "Content 1";
        String content2 = "Content 2";
        fs.create(path, content1);
        fs.update(path, content2);
        assertEquals(content2, fs.read(path));
        fs.delete(path);
    }

    @Test
    void testUpdateFileNotFound() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        assertThrows(IllegalArgumentException.class, () -> fs.update("/file1.txt", "New Content"));
    }

    @Test
    void testDelete() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        String path = "/dir1/file1.txt";
        fs.create(path, "Content");
        fs.delete(path);
        assertThrows(IllegalArgumentException.class, () -> fs.read(path));
    }

    @Test
    void testDeleteFileNotFound() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        assertThrows(IllegalArgumentException.class, () -> fs.delete("/file1.txt"));
    }

    @Test
    void testMkdir() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        String path = "/dir1/dir2";
        fs.mkdir(path);
        // Check that we can create a file in the directory
        fs.create(path + "/file1.txt", "File in dir2");
        assertEquals("File in dir2", fs.read(path + "/file1.txt"));
        fs.delete(path + "/file1.txt");
    }

    @Test
    void testMkdirWithFileAlreadyExists() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        String path = "/dir1";
        fs.create(path, "file content");
        assertThrows(IllegalArgumentException.class, () -> fs.mkdir(path));
        fs.delete(path);
    }

    @Test
    void testCreateInvalidPath() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        assertThrows(IllegalArgumentException.class, () -> fs.create("file1.txt", "Content")); // Missing leading slash
        assertThrows(IllegalArgumentException.class, () -> fs.create("", "Content"));      // Empty path
        assertThrows(IllegalArgumentException.class, () -> fs.create(null, "Content"));    // Null path
    }

    @Test
    void testReadInvalidPath() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        assertThrows(IllegalArgumentException.class, () -> fs.read("file1.txt"));
        assertThrows(IllegalArgumentException.class, () -> fs.read(""));
        assertThrows(IllegalArgumentException.class, () -> fs.read(null));
    }

    @Test
    void testUpdateInvalidPath() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        assertThrows(IllegalArgumentException.class, () -> fs.update("file1.txt", "new content"));
        assertThrows(IllegalArgumentException.class, () -> fs.update("", "new content"));
        assertThrows(IllegalArgumentException.class, () -> fs.update(null, "new content"));
    }

    @Test
    void testDeleteInvalidPath() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        assertThrows(IllegalArgumentException.class, () -> fs.delete("file1.txt"));
        assertThrows(IllegalArgumentException.class, () -> fs.delete(""));
        assertThrows(IllegalArgumentException.class, () -> fs.delete(null));
    }

    @Test
    void testMkdirInvalidPath() {
        InMemoryFileSystem fs = new InMemoryFileSystem();
        assertThrows(IllegalArgumentException.class, () -> fs.mkdir("dir1"));
        assertThrows(IllegalArgumentException.class, () -> fs.mkdir(""));
        assertThrows(IllegalArgumentException.class, () -> fs.mkdir(null));
    }
}

