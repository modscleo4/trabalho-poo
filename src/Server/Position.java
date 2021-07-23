class Position {
     int x;
     int y;

     Position(int x, int y) {
          this.x = x;
          this.y = y;
     }

     public void refresh(int dx, int dy) {
          x += dx;
          y += dy;
     }
}
