public class Level {
    private int[][] map;
    private final int tileSize;

    public Level(int width, int height, int tileSize) {
        this.tileSize = tileSize;
        generateLevel(width, height);
    }

    private void generateLevel(int width, int height) {
        map = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = weighted_random(0, 5, 24, 0);
            }
        }
    }

    public static int weighted_random(int start, int end, int weight, int weightedNum) {
        int range = end - start;
        int totalWeight = range - 1 + weight;

        int r = (int)(Math.random() * totalWeight);
        int current = 0;

        for (int i = start; i < end; i++) {
            int w = (i == weightedNum) ? weight : 1;
            if (r < current + w)
                return i;
            current += w;
        }
        return start; // fallback
    }

    public int getWallType(double worldX, double worldY) {

        int mapX = (int)(worldX / tileSize);
        int mapY = (int)(worldY / tileSize);

        if (mapX < 0 || mapY < 0 ||
                mapY >= map.length ||
                mapX >= map[0].length)
            return 1;

        return map[mapY][mapX];
    }
}
