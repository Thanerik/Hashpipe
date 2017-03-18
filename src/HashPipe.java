/**
 * HashPipe algorithm
 * Created by Troels Madsen (trma) for Algorithms and Data Structures 2017
 */
public class HashPipe {
    private Pipe root;
    private int size = 0;

    private class Pipe {
        String key;
        Integer val;
        Pipe[] pipe;
        int pipeHeight;

        public Pipe(String key, Integer val, int pipeHeight) {
            this.key = key;
            this.val = val;
            this.pipeHeight = pipeHeight;
            pipe = new Pipe[pipeHeight + 1];
        }

        private void resizePipe(int pipeHeight) {
            Pipe[] tmpRoot = new Pipe[pipeHeight + 1];
            System.arraycopy(root.pipe, 0, tmpRoot, 0, root.pipe.length);
            pipe = tmpRoot;
            root.pipeHeight = pipeHeight;
        }
    }

    // create an empty symbol table
    public HashPipe() {
        root = new Pipe(null, null, 0);
    }

    public int size() { return size; }

    // put key-value pair into the table
    public void put(String key, Integer val) {
        Pipe newPipe = new Pipe(key, val, Integer.numberOfTrailingZeros(key.hashCode()));

        // A little function to resize the root if it's less than required
        if (newPipe.pipeHeight > root.pipeHeight) {
            root.resizePipe(newPipe.pipeHeight);
        }

        put(root, newPipe, root.pipeHeight);
        size++;
    }

    private Pipe put(Pipe src, Pipe dest, int height) {
        if (src == null || height < 0) return null;

        Pipe nextPipe = src.pipe[height];
        if (nextPipe == null) {
            if (dest.pipeHeight >= height) {
                updateReference(src, dest, height);
            }
            return put(src, dest, --height);
        }

        int cmp = dest.key.compareTo(nextPipe.key);
        if (cmp < 0) {
            if (dest.pipeHeight >= height) updateReference(src, dest, height);
            return put(src, dest, --height);
        } else if (cmp > 0) {
            return put(nextPipe, dest, height);
        } else {
            nextPipe.val = dest.val;
            return nextPipe;
        }
    }

    private void updateReference(Pipe src, Pipe dest, int h) {
        Pipe nextPipe = src.pipe[h];
        src.pipe[h] = dest;
        dest.pipe[h] = nextPipe;
    }

    public Integer get(String key) {
        Pipe x = getPipe(key);
        if (x != null) return x.val;
        return null;
    }

    public String floor(String key) {
        Pipe x = floor(root, key, root.pipeHeight);
        if (x == null) return null;
        return x.key;
    }

    private Pipe floor(Pipe x, String key, int height) {
        if (x == null) return null;

        Pipe nextPipe = x.pipe[height];
        if (nextPipe == null) {
            if (height < 1) return x;
            else return floor(x, key, --height);
        }

        int cmp = key.compareTo(nextPipe.key);
        if (cmp == 0) return nextPipe;
        if (cmp < 0 && height > 0) return floor(x, key, --height);
        else if (cmp < 0) return x;

        Pipe t = floor(nextPipe, key, height);
        if (t != null) return t;
        else return nextPipe;
    }

    private Pipe getPipe(String key) {
        return floor(root, key, root.pipeHeight);
    }

    public String control(String key, int h) {
        Pipe pipe = getPipe(key);
        if (pipe == null || pipe.pipeHeight < h || pipe.pipe[h] == null) return null;
        return pipe.pipe[h].key;
    }


    // -------------
    //  Test method
    // -------------
    public static void main(String[] args) {
        // Test
        String [] in = { "S", "E", "A", "R", "C", "H", "E", "X", "A", "M", "P", "L", "E" };

        HashPipe H = new HashPipe();

        for( int j=0;j<in.length;j++ )
        {
            H.put(in[j], j);
            System.out.print("Insert: ");
            System.out.println(in[j]);
            for( int g=0;g<j;g++ ) {
                for( int h=0;h<32;h++ ) {
                    String ctrl = H.control(in[g],h);
                    if( ctrl != null ) System.out.print(ctrl);
                    else System.out.print(".");
                    System.out.print(" ");
                }
                System.out.print(" : ");
                System.out.println(in[g]);
            }
        }
    }
}