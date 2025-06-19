public class Trie {
    private final Trie[] children;
    private boolean isLeaf;

    public Trie() {
        children = new Trie[256];
        isLeaf = false;
    }

    public void add(final String text) {
        Trie root = this;
        for (Character ch : text.toLowerCase().toCharArray()) {
            int index = ch - 'a';
            if (root.children[index] == null) {
                root.children[index] = new Trie();
            }
            root = root.children[index];
        }
        root.isLeaf = true;
    }

    public boolean search(String text) {
        Trie root = this;
        for (Character ch : text.toLowerCase().toCharArray()) {
            int index = ch - 'a';
            if (root.children[index] == null) {
                return false;
            }
            root = root.children[index];
        }
        return root.isLeaf;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.add("vinod");
        trie.add("ishaan");
        trie.add("Evaan");
        trie.add("Rekha");
        System.out.println(trie.search("Vinod"));
        System.out.println(trie.search("VinodK"));
    }
}
