package gemini;

import java.util.HashMap;
import java.util.Map;

public class InMemoryFileSystem {

    // Use a Node class to represent files and directories
    private static class Node {
        String name;
        boolean isDirectory;
        String content;
        Map<String, Node> children;

        public Node(String name, boolean isDirectory) {
            this.name = name;
            this.isDirectory = isDirectory;
            this.content = "";
            this.children = new HashMap<>();
        }
    }

    private final Node root;

    public InMemoryFileSystem() {
        this.root = new Node("", true); // Root directory
    }

    public void create(String path, String value) {
        String[] parts = getPathParts(path);
        Node current = root;
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (!current.children.containsKey(part)) {
                current.children.put(part, new Node(part, true));
            }
            if ( !current.children.get(part).isDirectory) {
                throw new IllegalArgumentException("Invalid path: " + path);
            }
            current = current.children.get(part);
        }
        String fileName = parts[parts.length - 1];
        if (current.children.containsKey(fileName)) {
            throw new IllegalArgumentException("File or directory already exists: " + path);
        }
        Node fileNode = new Node(fileName, false);
        fileNode.content = value;
        current.children.put(fileName, fileNode);
    }

    public String read(String path) {
        Node fileNode = getNode(path);
        if (fileNode == null || fileNode.isDirectory) {
            throw new IllegalArgumentException("Invalid path or not a file: " + path);
        }
        return fileNode.content;
    }

    public void update(String path, String value) {
        Node fileNode = getNode(path);
        if (fileNode == null || fileNode.isDirectory) {
            throw new IllegalArgumentException("Invalid path or not a file: " + path);
        }
        fileNode.content = value;
    }

    public void delete(String path) {
        String[] parts = getPathParts(path);
        Node current = root;
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (!current.children.containsKey(part) || !current.children.get(part).isDirectory) {
                throw new IllegalArgumentException("Invalid path: " + path);
            }
            current = current.children.get(part);
        }
        String name = parts[parts.length - 1];
        if (!current.children.containsKey(name)) {
            throw new IllegalArgumentException("Invalid path: " + path);
        }
        current.children.remove(name);
    }

    public void mkdir(String path) {
        String[] parts = getPathParts(path);
        Node current = root;
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (!current.children.containsKey(part)) {
                Node newDir = new Node(part, true);
                current.children.put(part, newDir);
                current = newDir;
            } else {
                Node node = current.children.get(part);
                if (!node.isDirectory) {
                    throw new IllegalArgumentException("Path already exists and is not a directory: " + path);
                }
                current = node;
            }
        }
    }

    private Node getNode(String path) {
        String[] parts = getPathParts(path);
        Node current = root;
        for (String part : parts) {
            if (!current.children.containsKey(part)) {
                return null;
            }
            current = current.children.get(part);
        }
        return current;
    }

    private String[] getPathParts(String path) {
        if (path == null || path.isEmpty() || !path.startsWith("/")) {
            throw new IllegalArgumentException("Invalid path: " + path);
        }
        return path.substring(1).split("/");
    }
}

/**
* My implementation, but Gemini suggested a better approach with Node DS
 * that will help on hierarchical structure like deleting a dir delete the whole sub dir and files.
**/
/*public class InMemoryFileSystem {
    private final HashMap<String, String> filesWithContent;
    private final HashSet<String> directories;

    InMemoryFileSystem() {
        this.filesWithContent = new HashMap<>();
        this.directories = new HashSet<>();
    }


    public void create(String path, String value) {
        validatePath(path);
        if (filesWithContent.containsKey(path)) {
            throw new IllegalArgumentException("file " + path + " already exists.");
        }
        int index = path.lastIndexOf("/");
        if (-1 != index) {
            String dir = path.substring(0, index);
            if (!isDirExists(dir)) {
                mkdir(dir);
            }
        }
        filesWithContent.put(path, value);
    }

    public String read(String path) {
        if (!filesWithContent.containsKey(path)) {
            throw new IllegalArgumentException("file " + path + " doesn't exists.");
        }
        return filesWithContent.get(path);
    }

    public void update(String path, String value) {
        if (!filesWithContent.containsKey(path)) {
            throw new IllegalArgumentException("file " + path + " doesn't exists.");
        }
        filesWithContent.put(path, value);
    }

    public void delete(String path) {
        if (!filesWithContent.containsKey(path)) {
            throw new IllegalArgumentException("path " + path + " doesn't exists.");
        }
        filesWithContent.remove(path);
    }

    public void mkdir(String path) {
        if (isDirExists(path)) {
            throw new IllegalArgumentException("Directory " + path + " already exists.");
        }
        directories.add(path);
    }

    private boolean isDirExists(String path) {
        return directories.contains(path);
    }

    private void validatePath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("path " + path + " cannot be null or empty.");
        }
    }
}*/
