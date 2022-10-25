import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }

        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width() of current picture
    public int width() {
        return picture.width();
    }

    // height() of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000;
        }

        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        Color up = picture.get(x, y - 1);
        Color down = picture.get(x, y + 1);
        double deltaX = Math.pow((left.getRed() - right.getRed()), 2)
                + Math.pow((left.getGreen() - right.getGreen()), 2)
                + Math.pow((left.getBlue() - right.getBlue()), 2);
        double deltaY = Math.pow((up.getRed() - down.getRed()), 2)
                + Math.pow((up.getGreen() - down.getGreen()), 2)
                + Math.pow((up.getBlue() - down.getBlue()), 2);
        return Math.sqrt(deltaX + deltaY);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = new int[width()];
        if (height() == 1) {
            for (int i = 0; i < width(); i++) {
                seam[i] = 0;
            }
            return seam;
        }

        // to store the smallest accumulated energy of a point
        double[][] energies = new double[width()][height()];

        for (int j = 0; j < height(); j++) {
            energies[0][j] = 1000;
        }
        for (int i = 1; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                // use mod since when it comes to board,
                // we will not choose it since its large energy
                double up = energies[i - 1][mod(j - 1, height())];
                double mid = energies[i - 1][j];
                double down = energies[i - 1][mod(j + 1, height())];
                energies[i][j] = energy(i, j) + Math.min(Math.min(up, mid), down);
            }
        }

        // pick out the min seam
        int minEnd = minIndex(energies[width() - 1]);
        seam[width() - 1] = minEnd;
        for (int i = width() - 2; i >= 0; i--) {
            if (minEnd >= height() - 1) {
                minEnd += offset(energies[i][minEnd - 1], energies[i][minEnd], Double.POSITIVE_INFINITY);
            } else if (minEnd <= 0) {
                minEnd += offset(Double.POSITIVE_INFINITY, energies[i][minEnd], energies[i][minEnd + 1]);
            } else {
                minEnd += offset(energies[i][minEnd - 1], energies[i][minEnd], energies[i][minEnd + 1]);
            }
            seam[i] = minEnd;
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height()];
        if (width() == 1) {
            for (int i = 0; i < height(); i++) {
                seam[i] = 0;
            }
            return seam;
        }

        // to store the smallest accumulated energy of a point
        double[][] energies = new double[height()][width()];

        for (int i = 0; i < width(); i++) {
            energies[0][i] = 1000;
        }
        for (int j = 1; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                // use mod since when it comes to board,
                // we will not choose it since its large energy
                double up = energies[j - 1][mod(i - 1, width())];
                double mid = energies[j - 1][i];
                double down = energies[j - 1][mod(i + 1, width())];
                energies[j][i] = energy(i, j) + Math.min(Math.min(up, mid), down);
            }
        }

        // pick out the min seam
        int minEnd = minIndex(energies[height() - 1]);
        seam[height() - 1] = minEnd;
        for (int i = height() - 2; i >= 0; i--) {
            if (minEnd >= width() - 1 && width() > 1) {
                minEnd += offset(energies[i][minEnd - 1], energies[i][minEnd], Double.POSITIVE_INFINITY);
            } else if (minEnd <= 0) {
                minEnd += offset(Double.POSITIVE_INFINITY, energies[i][minEnd], energies[i][minEnd + 1]);
            } else {
                minEnd += offset(energies[i][minEnd - 1], energies[i][minEnd], energies[i][minEnd + 1]);
            }
            seam[i] = minEnd;
        }
        return seam;
    }

    private int mod(int n, int mod) {
        return (n + mod) % mod;
    }

    // return min index of an array
    private int minIndex(double[] array) {
        double min = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                index = i;
            }
        }
        return index;
    }

    private int offset(double a, double b, double c) {
        double min = Math.min(Math.min(a, b), c);
        if (min == a) {
            return -1;
        } else if (min == b) {
            return 0;
        } else {
            return 1;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1) {
            throw new IllegalArgumentException();
        }
        checkSeam(seam, width(), height());

        Picture p = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                if (j < seam[i]) {
                    p.set(i, j, picture.get(i, j));
                }
                if (j > seam[i]) {
                    p.set(i, j - 1, picture.get(i, j));
                }
            }
        }
        picture = p;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width() <= 1) {
            throw new IllegalArgumentException();
        }
        checkSeam(seam, height(), width());

        Picture p = new Picture(width() - 1, height());
        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                if (i < seam[j]) {
                    p.set(i, j, picture.get(i, j));
                }
                if (i > seam[j]) {
                    p.set(i - 1, j, picture.get(i, j));
                }
            }
        }
        picture = p;
    }

    private void checkSeam(int[] seam, int length, int range) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < length; i++) {
            if (seam[i] < 0 || seam[i] >= range) {
                throw new IllegalArgumentException();
            }
            if (i != length - 1 && Math.abs(seam[i + 1] - seam[i]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private static void printEnergy(String[] args) {
        Picture picture = new Picture(args[0]);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());

        SeamCarver sc = new SeamCarver(picture);

        StdOut.printf("Printing energy calculated for each pixel.\n");

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                StdOut.printf("%9.0f ", sc.energy(col, row));
            StdOut.println();
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        printEnergy(args);
    }
}